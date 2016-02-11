package com.dotcms.publisher.assets.business;

import com.dotcms.publisher.assets.bean.LastPush;
import com.dotmarketing.business.FactoryLocator;
import com.dotmarketing.exception.DotDataException;

import java.util.Optional;

public class LastPushAPIImpl implements LastPushAPI {

    private LastPushFactory lastPushFactory;

    public LastPushAPIImpl() {
        lastPushFactory = FactoryLocator.getLastPushFactory();
    }

    @Override
    public Optional<LastPush> getPushedItem(String assetId, String environmentId) throws DotDataException {
        return lastPushFactory.getLastPush(assetId, environmentId);
    }

    @Override
    public void savePushedAsset(LastPush asset) throws DotDataException {
        lastPushFactory.saveLastPush(asset);
    }

    @Override
    public void deletePushedItemsInBundle(String bundleId, String environmentId) throws DotDataException {
        lastPushFactory.deleteLastPushesInBundle(bundleId, environmentId);
    }

    @Override
    public void deleteAllPushedItems() throws DotDataException {
        lastPushFactory.deleteAllLastPushes();
    }

    @Override
    public void deletePushedItemByAsset(String assetId) throws DotDataException {
        lastPushFactory.deleteLastPushesByAsset(assetId);
    }

    @Override
    public void deletePushedItemsByEnvironment(String environmentId) throws DotDataException {
        lastPushFactory.deleteLastPushesByEnvironment(environmentId);
    }
}
