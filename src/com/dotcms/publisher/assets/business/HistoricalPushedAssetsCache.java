/**
 *
 */
package com.dotcms.publisher.assets.business;

import com.dotcms.publisher.assets.bean.HistoricalPushedAsset;

public interface HistoricalPushedAssetsCache {
	HistoricalPushedAsset getPushedAsset(String assetId, String environmentId);
	void add(HistoricalPushedAsset anAsset);
	void removePushedAssetById(String assetId, String environmentId);
	void clearCache();
}
