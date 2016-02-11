package com.dotmarketing.startup.runonce;

import com.dotmarketing.startup.AbstractJDBCStartupTask;

import java.util.List;

public class Task03535CreatePublishingPushedItemsTable extends AbstractJDBCStartupTask {

    @Override
    public boolean forceRun() {
        return true;
    }

    @Override
    public String getPostgresScript() {
        return "create table publishing_pushed_items(" +
                "asset_id varchar(36) NOT NULL," +
                "environment_id varchar(36) NOT NULL," +
                "push_date TIMESTAMP," +
                "PRIMARY KEY (asset_id, environment_id)" +
                ");";
    }

    @Override
    public String getMySQLScript() {
        return "create table publishing_pushed_items(" +
                "asset_id varchar(36) NOT NULL," +
                "environment_id varchar(36) NOT NULL," +
                "push_date DATETIME," +
                "PRIMARY KEY (asset_id, environment_id)" +
                ");";
    }

    @Override
    public String getOracleScript() {
        return "create table publishing_pushed_items(" +
                "asset_id varchar2(36) NOT NULL," +
                "environment_id varchar2(36) NOT NULL," +
                "push_date TIMESTAMP," +
                "PRIMARY KEY (asset_id, environment_id)" +
                ");";
    }

    @Override
    public String getMSSQLScript() {
        return "create table publishing_pushed_items(" +
                "asset_id varchar(36) NOT NULL," +
                "environment_id varchar(36) NOT NULL," +
                "push_date DATETIME," +
                "PRIMARY KEY (asset_id, environment_id)" +
                ");";
    }

    @Override
    public String getH2Script() {
        return "create table publishing_pushed_items(" +
                "asset_id varchar(36) NOT NULL," +
                "environment_id varchar(36) NOT NULL," +
                "push_date TIMESTAMP," +
                "PRIMARY KEY (asset_id, environment_id)" +
                ");";
    }

    @Override
    protected List<String> getTablesToDropConstraints() {
        return null;
    }

}
