package com.dotcms.publisher.assets.business;

import com.dotcms.publisher.assets.bean.LastPush;
import com.dotmarketing.business.FactoryLocator;
import com.dotmarketing.exception.DotDataException;

import java.util.Optional;

public class LastPushAPIImpl implements LastPushAPI {

    private LastPushFactory lastPushFactory;

    public LastPushAPIImpl() {
        lastPushFactory = FactoryLocator.getPushedItemsFactory();
    }

    @Override
    public Optional<LastPush> getPushedItem(String assetId, String environmentId) throws DotDataException {
        return lastPushFactory.getPushedItem(assetId, environmentId);
    }

    @Override
    public void savePushedAsset(LastPush asset) throws DotDataException {
        lastPushFactory.savePushedAsset(asset);
    }

    @Override
    public void deletePushedItemsInBundle(String bundleId, String environmentId) throws DotDataException {
        lastPushFactory.deletePushedItemsInBundle(bundleId, environmentId);
    }

    @Override
    public void deleteAllPushedItems() throws DotDataException {
        lastPushFactory.deleteAllPushedItems();
    }

    @Override
    public void deletePushedItemByAsset(String assetId) throws DotDataException {
        lastPushFactory.deletePushedItemByAsset(assetId);
    }

    @Override
    public void deletePushedItemsByEnvironment(String environmentId) throws DotDataException {
        lastPushFactory.deletePushedItemsByEnvironment(environmentId);
    }
}
