package com.dotcms.publisher.assets.business;

import com.dotcms.publisher.assets.bean.HistoricalPushedAsset;
import com.dotmarketing.business.Cachable;
import com.dotmarketing.business.CacheLocator;
import com.dotmarketing.business.DotCacheAdministrator;
import com.dotmarketing.business.DotCacheException;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.UtilMethods;


public class HistoricalPushedAssetsCacheImpl implements HistoricalPushedAssetsCache, Cachable {
	private final static String cacheGroup = "PushedAssetsCache";
	private final static String[] cacheGroups = {cacheGroup};
	private DotCacheAdministrator cache;

	public HistoricalPushedAssetsCacheImpl() {
		cache = CacheLocator.getCacheAdministrator();
	}


	public HistoricalPushedAsset getPushedAsset(String assetId, String environmentId) {
		HistoricalPushedAsset asset = null;
		try {
			asset = (HistoricalPushedAsset) cache.get(assetId + "|" + environmentId, cacheGroup);
		}
		catch(DotCacheException e) {
			Logger.debug(this, "PublishingEndPoint cache entry not found for: " + assetId + "|" + environmentId);
		}
		return asset;
	}

	public  void add(HistoricalPushedAsset asset) {
		if(asset != null) {
			cache.put(asset.getAssetId() + "|" + asset.getEnvironmentId() , asset, cacheGroup);
		}
	}

	public  void removePushedAssetById(String assetId, String environmentId) {
		if(UtilMethods.isSet(assetId) && UtilMethods.isSet(environmentId) )
			cache.remove(assetId + "|" + environmentId, cacheGroup);
	}

	public String getPrimaryGroup() {
		return cacheGroup;
	}

	public String[] getGroups() {
		return cacheGroups;
	}

	public synchronized void clearCache() {
		cache.flushGroup(cacheGroup);
	}

}
