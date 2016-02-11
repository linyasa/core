package com.dotcms.publisher.assets.bean;

import java.io.Serializable;
import java.util.Date;

public class LastPush implements Serializable {
    private final String assetId;
    private final String environmentId;
    private Date pushDate;

    public LastPush(String assetId, String environmentId, Date pushDate) {
        this.assetId = assetId;
        this.environmentId = environmentId;
        this.pushDate = pushDate;
    }

    public String getAssetId() {
        return assetId;
    }

    public String getEnvironmentId() {
        return environmentId;
    }

    public Date getPushDate() {
        return pushDate;
    }

    public void setPushDate(Date pushDate) {
        this.pushDate = pushDate;
    }

}
