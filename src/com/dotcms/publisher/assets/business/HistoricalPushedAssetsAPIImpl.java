package com.dotcms.publisher.assets.business;

import com.dotcms.publisher.assets.bean.HistoricalPushedAsset;
import com.dotmarketing.business.CacheLocator;
import com.dotmarketing.business.FactoryLocator;
import com.dotmarketing.exception.DotDataException;

import java.util.List;

public class HistoricalPushedAssetsAPIImpl implements HistoricalPushedAssetsAPI {

	private HistoricalPushedAssetsFactory pushedAssetsFactory;

	public HistoricalPushedAssetsAPIImpl() {
		pushedAssetsFactory = FactoryLocator.getHistoricalPushedAssetsFactory();
	}

	@Override
	public void savePushedAsset(HistoricalPushedAsset asset)
			throws DotDataException {
		pushedAssetsFactory.savePushedAsset(asset);

	}

	@Override
	public void deletePushedAssets(String bundleId, String environmentId)
			throws DotDataException {

		List<HistoricalPushedAsset> assets = pushedAssetsFactory.getPushedAssets(bundleId, environmentId);

		pushedAssetsFactory.deletePushedAssets(bundleId, environmentId);

		// clear the deleted entries from the cache
		if(assets!=null && assets.size()>0) {
			for (HistoricalPushedAsset asset : assets) {
				CacheLocator.getPushedAssetsCache().removePushedAssetById(asset.getAssetId(), asset.getEnvironmentId());
			}
		}

	}

	@Override
	public void deletePushedAssets(String assetId)
			throws DotDataException {

		List<HistoricalPushedAsset> assets = pushedAssetsFactory.getPushedAssets(assetId);

		pushedAssetsFactory.deletePushedAssets(assetId);

		// clear the deleted entries from the cache
		if(assets!=null && assets.size()>0) {
			for (HistoricalPushedAsset asset : assets) {
				CacheLocator.getPushedAssetsCache().removePushedAssetById(asset.getAssetId(), asset.getEnvironmentId());
			}
		}

	}

	@Override
	public void deletePushedAssetsByEnvironment(String environmentId)
			throws DotDataException {

		List<HistoricalPushedAsset> assets = pushedAssetsFactory.getPushedAssetsByEnvironment(environmentId);

		pushedAssetsFactory.deletePushedAssetsByEnvironment(environmentId);

		// clear the deleted entries from the cache
		if(assets!=null && assets.size()>0) {
			for (HistoricalPushedAsset asset : assets) {
				CacheLocator.getPushedAssetsCache().removePushedAssetById(asset.getAssetId(), asset.getEnvironmentId());
			}
		}

	}

	@Override
	public void deleteAllPushedAssets() throws DotDataException {
		pushedAssetsFactory.deleteAllPushedAssets();
		CacheLocator.getPushedAssetsCache().clearCache();

	}

	@Override
	public List<HistoricalPushedAsset> getPushedAssets(String assetId)
			throws DotDataException {
		return pushedAssetsFactory.getPushedAssets(assetId);
	}


}
