package com.dotcms.publisher.assets.bean;

import java.util.List;

public class PushHistory {

    private List<HistoricalPushedAsset> assets;

    public PushHistory(List<HistoricalPushedAsset> assets) {
        this.assets = assets;
    }

    public List<HistoricalPushedAsset> getAssets() {
        return assets;
    }
}
