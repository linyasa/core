package com.dotcms.rest;

import java.util.List;

/**
 * Created by freddyrodriguez on 18/5/16.
 */
public class Menu {

    private String tabName;
    private String tabDescription;
    private List<MenuItem> menuItems;
    private String url;

    public Menu(String tabName, String tabDescription, String url) {
        this.tabName = tabName;
        this.tabDescription = tabDescription;
        this.url = url;
    }

    public String getTabName() {
        return tabName;
    }

    public String getTabDescription() {
        return tabDescription;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public String getUrl() {
        return url;
    }
}
