package com.dotcms.publisher.assets.business;

import com.dotcms.publisher.assets.bean.HistoricalPushedAsset;
import com.dotcms.publisher.assets.bean.LastPush;
import com.dotcms.publisher.environment.bean.Environment;
import com.dotcms.publisher.util.PublisherUtil;
import com.dotcms.repackage.com.google.common.base.Preconditions;
import com.dotcms.repackage.com.google.common.base.Strings;
import com.dotmarketing.business.CacheLocator;
import com.dotmarketing.business.FactoryLocator;
import com.dotmarketing.common.db.DotConnect;
import com.dotmarketing.db.DbConnectionFactory;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.util.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class LastPushFactoryImpl implements LastPushFactory {

    private LastPushCache cache= CacheLocator.getLastPushCache();

    public Optional<LastPush> getLastPush(String assetId, String environmentId) throws DotDataException{
        Preconditions.checkArgument(!Strings.isNullOrEmpty(assetId), "Asset Id can't be null or empty");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(environmentId), "Environment Id can't be null or empty");

        Optional<LastPush> asset = Optional.ofNullable(cache.getPushedItem(assetId, environmentId));

        if(!asset.isPresent()){
            DotConnect dc = new DotConnect();
            dc.setSQL("SELECT * FROM publishing_last_push WHERE asset_id = ? and environment_id = ?");
            dc.addParam(assetId);
            dc.addParam(environmentId);
            List<Map<String, Object>> res = dc.loadObjectResults();

            if(res!=null && !res.isEmpty()) {
                asset = Optional.of(PublisherUtil.getPushedItemByMap(res.get(0)));
                cache.add(asset.get());
            }
        }

        return asset;
    }

    @Override
    public void saveLastPush(LastPush asset) throws DotDataException {
        Preconditions.checkNotNull(asset, "Asset cannot be null");

        Optional<LastPush> existingPushedItem = getLastPush(asset.getAssetId(), asset.getEnvironmentId());
        final DotConnect db = new DotConnect();

        if(existingPushedItem.isPresent()) {
            db.setSQL("UPDATE publishing_last_push SET push_date = ? WHERE asset_id = ? AND environment_id = ?");
            db.addParam(asset.getPushDate());
            db.addParam(asset.getAssetId());
            db.addParam(asset.getEnvironmentId());
            db.loadResult();
            cache.removePushedItemById(asset.getAssetId(), asset.getEnvironmentId());
        } else {
            db.setSQL("INSERT INTO publishing_last_push VALUES (?,?,?)");
            db.addParam(asset.getAssetId());
            db.addParam(asset.getEnvironmentId());
            db.addParam(asset.getPushDate());
            db.loadResult();
        }
    }

    @Override
    public void deleteLastPushesInBundle(String bundleId, String environmentId) throws DotDataException {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(bundleId), "Bundle Id can't be null or empty");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(environmentId), "Environment Id can't be null or empty");

        List<HistoricalPushedAsset> assets = FactoryLocator.getHistoricalPushedAssetsFactory().getPushedAssets(bundleId, environmentId);

        PreparedStatement statement = null;
        ResultSet rs = null;
        Connection conn = DbConnectionFactory.getConnection();

        try {

            statement = conn.prepareCall("DELETE FROM publishing_last_push WHERE asset_id = ? AND environment_id = ? ");

            for (HistoricalPushedAsset asset : assets) {
                statement.setObject(1, asset.getAssetId());
                statement.setObject(2, environmentId);
                statement.addBatch();
            }

            statement.executeBatch();

            // it all good let's remove the entries from cache
            for (HistoricalPushedAsset asset : assets) {
                cache.removePushedItemById(asset.getAssetId(), environmentId);
            }

        } catch(SQLException e) {
            throw new DotDataException(String.format("Error reseting push date for assets in bundle %s and environment &s", bundleId, environmentId), e);
        } finally {
            try { if (conn != null) conn.close(); } catch (Exception e) { Logger.error(this, "Error closing connection", e); }
            try { if (rs != null) rs.close(); } catch (Exception e) {  Logger.error(this, "Error closing result set", e); }
            try { if (statement != null) statement.close(); } catch (Exception e) { Logger.error(this, "Error closing statement", e);  }
        }
    }

    @Override
    public void deleteAllLastPushes() throws DotDataException {
        final DotConnect db = new DotConnect();
        db.setSQL("TRUNCATE publishing_last_push ");
        db.loadResult();
        cache.clearCache();
    }

    @Override
    public void deleteLastPushesByAsset(String assetId) throws DotDataException {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(assetId), "Bundle Id can't be null or empty");

        final DotConnect db = new DotConnect();
        db.setSQL("DELETE FROM publishing_last_push WHERE asset_id = ? ");
        db.addParam(assetId);
        db.loadResult();

        // remove from cache for all environments
        List<Environment> environments = FactoryLocator.getEnvironmentFactory().getEnvironments();
        for (Environment environment : environments) {
            cache.removePushedItemById(assetId, environment.getId());
        }

    }

    @Override
    public void deleteLastPushesByEnvironment(String environmentId) throws DotDataException {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(environmentId), "Environmetn Id can't be null or empty");

        List<String> assetsToRemoteFromCache = new ArrayList<>();
        final DotConnect db = new DotConnect();

        db.setSQL("SELECT asset_id FROM publishing_last_push WHERE environment_id = ?");
        db.addParam(environmentId);
        List<Map<String, Object>> res = db.loadObjectResults();

        for (Map<String, Object> re : res) {
            assetsToRemoteFromCache.add(re.get("asset_id").toString());
        }

        db.setSQL("DELETE FROM publishing_last_push WHERE environment_id = ? ");
        db.addParam(environmentId);
        db.loadResult();


        // remove assets from cache
        for (String assetId : assetsToRemoteFromCache) {
            cache.removePushedItemById(assetId, environmentId);
        }

    }

    @Override
    public void removeLastPush(String assetId, String environmentId) throws DotDataException {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(assetId), "Asset Id can't be null or empty");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(environmentId), "Environment Id can't be null or empty");

        final DotConnect db = new DotConnect();
        db.setSQL("DELETE FROM publishing_last_push WHERE asset_id = ? AND environment_id = ? ");
        db.addParam(assetId);
        db.addParam(environmentId);
        db.loadResult();

        // remove from cache
        cache.removePushedItemById(assetId, environmentId);
    }
}
