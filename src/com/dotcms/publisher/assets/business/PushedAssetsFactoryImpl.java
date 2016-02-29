package com.dotcms.publisher.assets.business;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.dotcms.publisher.assets.bean.PushedAsset;
import com.dotcms.publisher.util.PublisherUtil;
import com.dotcms.repackage.com.google.common.base.Preconditions;
import com.dotmarketing.business.CacheLocator;
import com.dotmarketing.common.db.DotConnect;
import com.dotmarketing.db.DbConnectionFactory;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.UtilMethods;

public class PushedAssetsFactoryImpl extends PushedAssetsFactory {

	private PushedAssetsCache cache = CacheLocator.getPushedAssetsCache();

	public void savePushedAsset(PushedAsset asset) throws DotDataException {
		final DotConnect db = new DotConnect();
		db.setSQL(INSERT_ASSETS);
		db.addParam(asset.getBundleId());
		db.addParam(asset.getAssetId());
		db.addParam(asset.getAssetType());
		db.addParam(asset.getPushDate());
		db.addParam(asset.getEnvironmentId());
		db.loadResult();
		cache.removePushedAssetById(asset.getAssetId(), asset.getEnvironmentId());
	}

	@Override
	public void deletePushedAssets(String bundleId, String environmentId)
			throws DotDataException {
		final DotConnect db = new DotConnect();
		db.setSQL(DELETE_ASSETS_BY_BUNDLE_ENV);
		db.addParam(bundleId);
		db.addParam(environmentId);
		db.loadResult();
		cache.clearCache();

	}

	@Override
	public void deletePushedAssets(String assetId)
			throws DotDataException {
		final DotConnect db = new DotConnect();
		db.setSQL(DELETE_ASSETS_BY_ASSET_ID);
		db.addParam(assetId);
		db.loadResult();
		cache.clearCache();

	}

	@Override
	public void deletePushedAssetsByEnvironment(String environmentId)
			throws DotDataException {
		final DotConnect db = new DotConnect();
		db.setSQL(DELETE_ASSETS_BY_ENVIRONMENT_ID);
		db.addParam(environmentId);
		db.loadResult();
		cache.clearCache();

	}

	@Override
	public List<PushedAsset> getPushedAssets(String bundleId, String environmentId)
			throws DotDataException {
		List<PushedAsset> assets = new ArrayList<PushedAsset>();

		if(!UtilMethods.isSet(bundleId) || !UtilMethods.isSet(environmentId)) {
			return assets;
		}

		DotConnect dc = new DotConnect();
		dc.setSQL(SELECT_ASSETS_BY_BUNDLE_ENV);
		dc.addParam(bundleId);
		dc.addParam(environmentId);

		List<Map<String, Object>> res = dc.loadObjectResults();

		for(Map<String, Object> row : res){
			PushedAsset asset = PublisherUtil.getPushedAssetByMap(row);
			assets.add(asset);
		}

		return assets;

	}



	@Override
	public void deleteAllPushedAssets() throws DotDataException {
		final DotConnect db = new DotConnect();
		db.setSQL(DELETE_ALL_ASSETS);
		db.loadResult();
		cache.clearCache();
	}

	@Override
	public List<PushedAsset> getPushedAssets(String assetId)
			throws DotDataException {
		List<PushedAsset> assets = new ArrayList<PushedAsset>();

		if(!UtilMethods.isSet(assetId)) {
			return assets;
		}

		DotConnect dc = new DotConnect();
		dc.setSQL(SELECT_ASSETS_BY_ASSET_ID);
		dc.addParam(assetId);

		List<Map<String, Object>> res = dc.loadObjectResults();

		for(Map<String, Object> row : res){
			PushedAsset asset = PublisherUtil.getPushedAssetByMap(row);
			assets.add(asset);
		}

		return assets;
	}

	@Override
	public List<PushedAsset> getPushedAssetsByEnvironment(String environmentId)
			throws DotDataException {
		List<PushedAsset> assets = new ArrayList<PushedAsset>();

		if(!UtilMethods.isSet(environmentId)) {
			return assets;
		}

		DotConnect dc = new DotConnect();
		dc.setSQL(SELECT_ASSETS_BY_ENV_ID);
		dc.addParam(environmentId);

		List<Map<String, Object>> res = dc.loadObjectResults();

		for(Map<String, Object> row : res){
			PushedAsset asset = PublisherUtil.getPushedAssetByMap(row);
			assets.add(asset);
		}

		return assets;
	}

	public PushedAsset getLastPushForAsset(String assetId, String environmentId)  throws DotDataException{
		
		PushedAsset asset = cache.getPushedAsset(assetId, environmentId);
		if(asset==null){
			DotConnect dc = new DotConnect();
			dc.setSQL(SELECT_ASSET_LAST_PUSHED);
			dc.addParam(assetId);
			dc.addParam(environmentId);
			dc.setMaxRows(1);
			List<Map<String, Object>> res = dc.loadObjectResults();
	
			for(Map<String, Object> row : res){
				asset = PublisherUtil.getPushedAssetByMap(row);
				cache.add(asset);
			}
		}
		
		return asset;
		
		
	}

	@Override
	public void savePushedAsset(Collection<PushedAsset> pushedAssets) throws DotDataException {
		Preconditions.checkNotNull(pushedAssets, "Null pushedAssets collection was passed");
		Preconditions.checkArgument(!pushedAssets.isEmpty(), "Emtpy pushedAssets collection was passed");


		PreparedStatement statement = null;
		Connection conn = DbConnectionFactory.getConnection();
		String bundleId = "";
		String environmentId = "";

		try {

			statement = conn.prepareCall(INSERT_ASSETS);
			int i = 0;

			for (PushedAsset asset : pushedAssets) {

				if(i==0) {
					bundleId = asset.getBundleId();
					environmentId = asset.getEnvironmentId();
				}

				statement.setObject(1, asset.getBundleId());
				statement.setObject(2, asset.getAssetId());
				statement.setObject(3, asset.getAssetType());
				statement.setObject(4, asset.getPushDate());
				statement.setObject(5, asset.getEnvironmentId());
				statement.addBatch();
				i++;

			}

			statement.executeBatch();
//
//			// it all good let's remove the entries from cache
//			for (PushedAsset asset : pushedAssets) {
//				cache.removePushedAssetById(asset.getAssetId(), asset.getEnvironmentId());
//			}

		} catch(SQLException e) {
			throw new DotDataException(String.format("Error saving pushed assets for BundleId: %s and EnvironmentId: &s", bundleId, environmentId), e);
		} finally {
			try { if (conn != null) conn.close(); } catch (Exception e) { Logger.error(this, "Error closing connection", e); }
			try { if (statement != null) statement.close(); } catch (Exception e) { Logger.error(this, "Error closing statement", e);  }
		}
	}

}
