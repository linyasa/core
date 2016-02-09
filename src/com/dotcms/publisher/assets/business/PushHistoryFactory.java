package com.dotcms.publisher.assets.business;

import com.dotcms.publisher.assets.bean.HistoricalPushedAsset;
import com.dotcms.publisher.assets.bean.PushHistory;
import com.dotmarketing.exception.DotDataException;

public interface PushHistoryFactory {

    PushHistory getPushHistoryForAsset(String assetId);

    void savePushedAsset(HistoricalPushedAsset asset) throws DotDataException;
}
