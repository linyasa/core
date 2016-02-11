package com.dotcms.publisher.assets.business;

import com.dotcms.publisher.assets.bean.LastPush;
import com.dotmarketing.business.Cachable;
import com.dotmarketing.business.CacheLocator;
import com.dotmarketing.business.DotCacheAdministrator;
import com.dotmarketing.business.DotCacheException;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.UtilMethods;


public class LastPushCacheImpl implements LastPushCache, Cachable {
	private final static String cacheGroup = "PushedItemsCache";
	private final static String[] cacheGroups = {cacheGroup};
	private DotCacheAdministrator cache;

	public LastPushCacheImpl() {
		cache = CacheLocator.getCacheAdministrator();
	}


	public LastPush getPushedItem(String assetId, String environmentId) {
		LastPush asset = null;
		try {
			asset = (LastPush) cache.get(assetId + "|" + environmentId, cacheGroup);
		}
		catch(DotCacheException e) {
			Logger.debug(this, "PushedItem cache entry not found for: " + assetId + "|" + environmentId);
		}
		return asset;
	}

	public  void add(LastPush asset) {
		if(asset != null) {
			cache.put(asset.getAssetId() + "|" + asset.getEnvironmentId() , asset, cacheGroup);
		}
	}

	public  void removePushedItemById(String assetId, String environmentId) {
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
