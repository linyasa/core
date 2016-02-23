package com.dotcms.publisher.util;

import java.util.*;

import com.dotcms.publisher.assets.bean.HistoricalPushedAsset;
import com.dotcms.publisher.assets.bean.LastPush;
import com.dotcms.publisher.assets.business.HistoricalPushedAssetsCache;
import com.dotcms.publisher.assets.business.LastPushAPI;
import com.dotcms.publisher.bundle.bean.Bundle;
import com.dotcms.publisher.environment.bean.Environment;
import com.dotcms.repackage.com.google.common.base.Optional;
import com.dotmarketing.beans.VersionInfo;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.CacheLocator;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.portlets.contentlet.model.ContentletVersionInfo;
import com.dotmarketing.portlets.languagesmanager.model.Language;
import com.dotmarketing.util.InodeUtils;
import com.dotmarketing.util.Logger;

public class DependencySet extends HashSet<String> {

	/**
	 *
	 */
	private static final long serialVersionUID = 3048299770146564147L;
	private List<Environment> envs = new ArrayList<Environment>();
	private String assetType;
	private String bundleId;
	private Bundle bundle;
	private boolean isDownload;
	private boolean isPublish;
	private LastPushAPI lastPushAPI;

	public DependencySet(String bundleId, String assetType, boolean isDownload, boolean isPublish) {
		super();
		this.assetType = assetType;
		this.bundleId = bundleId;
		this.isDownload = isDownload;
		this.isPublish = isPublish;

		try {
			envs = APILocator.getEnvironmentAPI().findEnvironmentsByBundleId(bundleId);
		} catch (DotDataException e) {
			Logger.error(getClass(), "Can't get environments", e);
		}

		try {
			bundle = APILocator.getBundleAPI().getBundleById(bundleId);
		} catch (DotDataException e) {
			Logger.error(getClass(), "Can't get bundle. Bundle Id: " + bundleId , e);
		}

		lastPushAPI = APILocator.getLastPushAPI();
	}

	public boolean add(String assetId, Date assetModDate) {
		return addOrClean( assetId, assetModDate, false );
	}

	/**
	 * Is this method is called and in case of an <strong>UN-PUBLISH</strong> instead of adding elements it will remove them
	 * from cache.<br>
	 * For <strong>PUBLISH</strong> do the same as the <strong>add</strong> method.
	 *
	 * @param assetId
	 * @param assetModDate
	 * @return
	 */
	public boolean addOrClean ( String assetId, Date assetModDate) {
		return addOrClean( assetId, assetModDate, true );
	}

	private boolean addOrClean ( String assetId, Date assetModDate, Boolean cleanForUnpublish ) {

		if ( !isPublish ) {

			//For un-publish we always remove the asset from cache
			for ( Environment env : envs ) {
				try {
					lastPushAPI.removeLastPush(assetId, env.getId());
				} catch(DotDataException e) {
					Logger.error(this, String.format("Error deleting Last Push. AssetId: %s. EnvId: %s", assetId, env.getId()), e);
				}
			}

			//Return if we are here just to clean up dependencies from cache
			if ( cleanForUnpublish ) {
				return true;
			}
		}

		// check if it was already added
		if(super.contains(assetId)) return false;

		boolean push = false;

		// we need to check if all environments have the last version of the asset in
		// order to skip adding it to the Set

		// if the asset hasn't been sent to at least one environment or an older version was sen't,
		// we need to add it to the cache

		boolean forcePush = false;
		if ( bundle != null ) {
			forcePush = bundle.isForcePush();
		}

		if ( !forcePush && !isDownload && isPublish ) {
			for (Environment env : envs) {
				Optional<LastPush> lastPush;
				try {
					lastPush = lastPushAPI.getLastPush(assetId, env.getId());
				} catch (DotDataException e1) {
					// Asset does not exist in db or cache, return true;
					return true;
				}

				boolean modified = (!lastPush.isPresent()
						|| lastPush.get().getPushDate()==null
						|| (assetModDate!=null && (lastPush.get().getPushDate().before(assetModDate))));

				push = push || modified;

				try {
					if(!modified && assetType.equals("content")) {
						// check for versionInfo TS on content
						for(Language lang : APILocator.getLanguageAPI().getLanguages()) {
							ContentletVersionInfo info=APILocator.getVersionableAPI().getContentletVersionInfo(assetId, lang.getId());
							if(info!=null && InodeUtils.isSet(info.getIdentifier())) {
								modified = modified || (null == info.getVersionTs()) || lastPush.get().getPushDate().before(info.getVersionTs());
							}
						}
					}
					if(!modified && (assetType.equals("template") || assetType.equals("links") || assetType.equals("container") || assetType.equals("htmlpage"))) {
						// check for versionInfo TS
						VersionInfo info=APILocator.getVersionableAPI().getVersionInfo(assetId);
						if(info!=null && InodeUtils.isSet(info.getIdentifier())) {
							modified = lastPush.get().getPushDate().before(info.getVersionTs());
						}
					}
				} catch (Exception e) {
					Logger.warn(getClass(), "Error checking versionInfo for assetType:"+assetType+" assetId:"+assetId+
							" process continues without checking versionInfo.ts",e);
				}

				if(modified) {
					try {
						if(lastPush.isPresent()) {
							LastPush existingLastPush = lastPush.get();
							existingLastPush.setPushDate(new Date());
							lastPushAPI.saveLastPush(existingLastPush);
						} else {
							LastPush newPushItem = new LastPush(assetId, env.getId(), new Date());
							lastPushAPI.saveLastPush(newPushItem);
						}
					} catch (DotDataException e) {
						Logger.error(getClass(), "Could not save LastPush. "
								+ "AssetId: " + assetId + ". Env Id: " + env.getId(), e);
					}

					// This is merely for historical purposes
					saveHistorical(assetId, env);
				}
			}
		}

		if(forcePush) {
			for (Environment env : envs) {
				saveHistorical(assetId, env);
			}
		}

		if ( forcePush || isDownload || !isPublish || push ) {
			super.add( assetId );
			return true;
		}

		return false;
	}

	private void saveHistorical(String assetId, Environment env)  {
		try {
			HistoricalPushedAsset newHistoricalAsset = new HistoricalPushedAsset(bundleId, assetId, assetType, new Date(), env.getId());
			APILocator.getHistoricalPushedAssetsAPI().savePushedAsset(newHistoricalAsset);
		} catch (DotDataException e) {
			Logger.error(getClass(), "Could not save HistoricalPushedAsset. "
					+ "AssetId: " + assetId + ". AssetType: " + assetType + ". Env Id: " + env.getId(), e);
		}
	}

}
