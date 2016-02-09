package com.dotcms.publisher.assets.business;

import com.dotcms.publisher.assets.bean.HistoricalPushedAsset;
import com.dotcms.publisher.assets.bean.PushHistory;
import com.dotmarketing.business.FactoryLocator;

public class PushHistoryAPIImpl implements PushHistoryAPI {

    private PushHistoryFactory pushHistoryFactory;

    public PushHistoryAPIImpl() {
        pushHistoryFactory = FactoryLocator.getPushHistoryFactory();
    }
    @Override
    public PushHistory getPushHistoryForAsset(String assetId) {
        return pushHistoryFactory.getPushHistoryForAsset(assetId);
    }

    @Override
    public void savePushedAsset(HistoricalPushedAsset asset) {
        pushHistoryFactory.savePushedAsset(asset);
    }
}
