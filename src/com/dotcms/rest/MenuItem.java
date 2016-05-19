package com.dotcms.rest;

/**
 * Created by freddyrodriguez on 18/5/16.
 */
public class MenuItem {

    private String url;
    private String id;
    private String name;
    private boolean angular = false;

    public MenuItem(String id, String url, String name) {
        this.url = url;
        this.id = id;
        this.name = name;
    }

    public MenuItem(String id, String url, String name, boolean angular) {
        this(id, url, name);
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
}
