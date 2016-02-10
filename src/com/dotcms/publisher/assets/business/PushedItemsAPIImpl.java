package com.dotcms.publisher.assets.business;

import com.dotcms.publisher.assets.bean.PushedItem;
import com.dotmarketing.business.FactoryLocator;
import com.dotmarketing.exception.DotDataException;

import java.util.Optional;

public class PushedItemsAPIImpl implements PushedItemsAPI {

    private PushedItemsFactory pushedItemsFactory;

    public PushedItemsAPIImpl() {
        pushedItemsFactory = FactoryLocator.getPushedItemsFactory();
    }

    @Override
    public Optional<PushedItem> getPushedItem(String assetId, String environmentId) throws DotDataException {
        return pushedItemsFactory.getPushedItem(assetId, environmentId);
    }

    @Override
    public void savePushedAsset(PushedItem asset) throws DotDataException {
        pushedItemsFactory.savePushedAsset(asset);
    }

    @Override
    public void resetPushDateOfItemsInBundle(String bundleId, String environmentId) throws DotDataException {
        pushedItemsFactory.resetPushDateOfItemsInBundle(bundleId, environmentId);
    }

    @Override
    public void deleteAllPushedItems() throws DotDataException {
        pushedItemsFactory.deleteAllPushedItems();
    }

    @Override
    public void resetPushDateOfItem(String assetId) throws DotDataException {
        pushedItemsFactory.resetPushDateOfItem(assetId);
    }

    @Override
    public void resetPushDateOfItemsInEnvironment(String environmentId) throws DotDataException {
        pushedItemsFactory.resetPushDateOfItemsInEnvironment(environmentId);
    }
}
