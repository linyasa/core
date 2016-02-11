package com.dotcms.publisher.assets.business;

import com.dotcms.publisher.assets.bean.PushedItem;
import com.dotmarketing.exception.DotDataException;

import java.util.Optional;

public interface PushedItemsAPI {

    /**
     * Returns an {@code Optional} with a {@code PushedItem} that has the asset id and environment id provided,
     * if non-null, otherwise returns an empty {@code Optional}.
     *
     * @param assetId the id of the item to get
     * @param environmentId to environment id of the item to get
     * @return an {@code Optional} with a {@code PushedItem} if exists, otherwise an empty {@code Optional}
     * @throws DotDataException when an error in the underlying data layer occurs
     */
    Optional<PushedItem> getPushedItem(String assetId, String environmentId) throws DotDataException;

    /**
     * Persists the given {@code PushedItem} object, if valid.
     *
     * @param asset the asset object to persist
     * @throws DotDataException when an error in the underlying data layer occurs
     */
    void savePushedAsset(PushedItem asset) throws DotDataException;

    /**
     * Resets the push date of the items included in the bundle with the given bundle id
     * and that were sent to environment with the given environment id
     *
     * @param bundleId the id of the bundle whose items' push dates will be reset
     * @param environmentId the id of the environment
     * @throws DotDataException when an error in the underlying data layer occurs
     */
    void deletePushedItemsInBundle(String bundleId, String environmentId) throws DotDataException;

    /**
     * Deletes all the {@code PushedItem} objects from the persistence layer
     *
     * @throws DotDataException when an error in the underlying persistence layer occurs
     */
    void deleteAllPushedItems() throws DotDataException;

    /**
     * Resets the push date of the {@code PushedAsset} with the given id
     *
     * @param assetId id of the asset whose push date will be reset
     * @throws DotDataException when an error in the underlying persistence layer occurs
     */
    void deletePushedItemByAsset(String assetId) throws DotDataException;

    /**
     * Resets the push date of the {@code PushedAsset} objects sent to the environment with
     * the given environment id
     *
     * @param environmentId the id of the environment
     * @throws DotDataException when an error in the underlying persistence layer occurs
     */
    void deletePushedItemsByEnvironment(String environmentId) throws DotDataException;
}
