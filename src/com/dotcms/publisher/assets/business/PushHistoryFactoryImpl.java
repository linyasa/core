package com.dotcms.publisher.assets.business;

import com.dotcms.publisher.assets.bean.HistoricalPushedAsset;
import com.dotcms.publisher.assets.bean.PushHistory;
import com.dotmarketing.common.db.DotConnect;
import com.dotmarketing.exception.DotDataException;

public class PushHistoryFactoryImpl implements PushHistoryFactory {
    @Override
    public PushHistory getPushHistoryForAsset(String assetId) {
        return null;
    }

    @Override
    public void savePushedAsset(HistoricalPushedAsset asset) throws DotDataException {
        final DotConnect db = new DotConnect();
        db.setSQL("INSERT INTO publishing_pushed_assets VALUES (?,?,?,?,?)");
        db.addParam(asset.getBundleId());
        db.addParam(asset.getAssetId());
        db.addParam(asset.getAssetType());
        db.addParam(asset.getPushDate());
        db.addParam(asset.getEnvironmentId());
        db.loadResult();
    }
}
