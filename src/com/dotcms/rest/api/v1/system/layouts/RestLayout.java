package com.dotcms.rest.api.v1.system.layouts;

import com.dotcms.repackage.com.fasterxml.jackson.annotation.JsonProperty;
import com.dotcms.repackage.com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.dotcms.repackage.com.google.common.collect.ImmutableList;
import com.dotcms.repackage.com.google.common.collect.Lists;
import com.dotcms.rest.api.Validated;
import java.util.List;

@JsonDeserialize(builder = RestLayout.Builder.class)
public final class RestLayout extends Validated {

    public final String id;
    public final String name;
    public final String description;
    public final Integer tabOrder;
    public final String tabUrl;
    public final List<String> portletIds;

    private RestLayout(Builder builder) {
        id = builder.id;
        name = builder.name;
        description = builder.description;
        tabOrder = builder.tabOrder;
        tabUrl = builder.tabUrl;
        portletIds = ImmutableList.copyOf(builder.portletIds);
        checkValid();
    }

    public static final class Builder {
        @JsonProperty private String id;
        @JsonProperty private String name;
        @JsonProperty private String description;
        @JsonProperty private Integer tabOrder;
        @JsonProperty private List<String> portletIds = Lists.newArrayList();
        @JsonProperty private String tabUrl;

        public Builder() {}

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder tabOrder(Integer tabOrder) {
            this.tabOrder = tabOrder;
            return this;
        }

        public Builder portletIds(List<String> portletIds) {
            if(portletIds == null){
                this.portletIds.clear();
            } else {
                this.portletIds = portletIds;
            }
            return this;
        }

        public RestLayout build() {
            return new RestLayout(this);
        }

        public Builder tabUrl(String tabUrl) {
            this.tabUrl = tabUrl;
            return this;
        }
    }
}

