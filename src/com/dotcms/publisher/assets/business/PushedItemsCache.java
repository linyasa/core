/**
 *
 */
package com.dotcms.publisher.assets.business;

import com.dotcms.publisher.assets.bean.PushedItem;

public interface PushedItemsCache {
	PushedItem getPushedItem(String assetId, String environmentId);
	void add(PushedItem pushedItem);
	void removePushedItemById(String assetId, String environmentId);
	void clearCache();
}
