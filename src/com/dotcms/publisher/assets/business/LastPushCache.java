/**
 *
 */
package com.dotcms.publisher.assets.business;

import com.dotcms.publisher.assets.bean.LastPush;

public interface LastPushCache {
	LastPush getPushedItem(String assetId, String environmentId);
	void add(LastPush lastPush);
	void removePushedItemById(String assetId, String environmentId);
	void clearCache();
}
