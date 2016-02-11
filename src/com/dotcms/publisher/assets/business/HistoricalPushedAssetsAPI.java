package com.dotcms.publisher.assets.business;

import com.dotcms.publisher.assets.bean.HistoricalPushedAsset;
import com.dotmarketing.exception.DotDataException;

import java.util.List;

public interface HistoricalPushedAssetsAPI {

	/**
	 * persists the given PushedAsset object to the underlying data layer.
	 *
	 * @param	asset	the pushed asset to be persisted
	 * @throws DotDataException    thrown when an error in the underlying data layer occurs
	 */

	void savePushedAsset(HistoricalPushedAsset asset)  throws DotDataException;

	/**
	 * deletes the push assets entries for the given Bundle Id and Environment Id.
	 *
	 * @param	bundleId	the id of the bundle
	 * @param	environmentId	the id of the environment
	 * @throws DotDataException    thrown when an error in the underlying data layer occurs
	 */

	void deletePushedAssets(String bundleId, String environmentId)  throws DotDataException;

	/**
	 * deletes all the push assets entries
	 *
	 * @throws DotDataException    thrown when an error in the underlying data layer occurs
	 */

	void deleteAllPushedAssets()  throws DotDataException;

	/**
	 * deletes the push assets entries for the given Asset Id.
	 *
	 * @param	assetId	the id of the asset whose pushed assets records will be deleted
	 *
	 * @throws DotDataException    thrown when an error in the underlying data layer occurs
	 */

	void deletePushedAssets(String assetId)  throws DotDataException;

	/**
	 * deletes the push assets entries for the given environment Id.
	 *
	 * @param	environmentId	the id of the environment whose pushed assets records will be deleted
	 *
	 * @throws DotDataException    thrown when an error in the underlying data layer occurs
	 */

	void deletePushedAssetsByEnvironment(String environmentId)  throws DotDataException;

	/**
	 * returns all the push assets for a given assetId
	 *
	 * @param	assetId	the id of the asset
	 * @throws DotDataException    thrown when an error in the underlying data layer occurs
	 */

	List<HistoricalPushedAsset> getPushedAssets(String assetId)  throws DotDataException;


}
