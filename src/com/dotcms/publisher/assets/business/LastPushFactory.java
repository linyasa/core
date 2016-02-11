package com.dotcms.publisher.assets.business;

import com.dotcms.publisher.assets.bean.LastPush;
import com.dotmarketing.exception.DotDataException;

import java.util.Optional;

public interface LastPushFactory {

    Optional<LastPush> getPushedItem(String assetId, String environmentId) throws DotDataException;

    void savePushedAsset(LastPush asset) throws DotDataException;

    void deletePushedItemsInBundle(String bundleId, String environmentId) throws DotDataException;

    void deleteAllPushedItems() throws DotDataException;

    void deletePushedItemByAsset(String assetId) throws DotDataException;

    void deletePushedItemsByEnvironment(String environmentId) throws DotDataException;
}
