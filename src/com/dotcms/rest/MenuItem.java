package com.dotcms.rest;

/**
 * Created by freddyrodriguez on 18/5/16.
 */
public class MenuItem {

    private String url;
    private String id;
    private String name;
    private boolean angular = false;
    private boolean isAjax;

    public MenuItem(String id, String url, String name, boolean isAngular, boolean isAjax) {
        this.url = url;
        this.id = id;
        this.name = name;
        this.angular = isAngular;
        this.isAjax = isAjax;
    }

    public MenuItem(String id, String url, String name, boolean angular) {
        this(id, url, name, angular, false);
        this.angular = angular;
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isAngular() {
        return angular;
    }

    public boolean isAjax() {
        return isAjax;
    }
}
