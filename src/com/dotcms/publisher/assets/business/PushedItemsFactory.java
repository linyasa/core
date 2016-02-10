package com.dotcms.publisher.assets.business;

import com.dotcms.publisher.assets.bean.PushedItem;
import com.dotmarketing.exception.DotDataException;

import java.util.Optional;

public interface PushedItemsFactory {

    Optional<PushedItem> getPushedItem(String assetId, String environmentId) throws DotDataException;

    void savePushedAsset(PushedItem asset) throws DotDataException;

    void resetPushDateOfItemsInBundle(String bundleId, String environmentId) throws DotDataException;

    void deleteAllPushedItems() throws DotDataException;

    void resetPushDateOfItem(String assetId) throws DotDataException;

    void resetPushDateOfItemsInEnvironment(String environmentId) throws DotDataException;
}
