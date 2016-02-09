package com.dotcms.publisher.assets.business;

import com.dotcms.publisher.assets.bean.HistoricalPushedAsset;
import com.dotcms.publisher.assets.bean.PushHistory;
import com.dotcms.publisher.assets.bean.PushedAsset;

public interface PushHistoryAPI {

    PushHistory getPushHistoryForAsset(String assetId);

    void savePushedAsset(HistoricalPushedAsset asset);
}
