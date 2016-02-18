package com.dotmarketing.startup.runonce;

import com.dotmarketing.startup.AbstractJDBCStartupTask;

import java.util.List;

public class Task03545CreatePublishingPushedItemsTable extends AbstractJDBCStartupTask {

    @Override
    public boolean forceRun() {
        return true;
    }

    @Override
    public String getPostgresScript() {
        return "create table publishing_last_push(" +
                "asset_id varchar(36) NOT NULL," +
                "environment_id varchar(36) NOT NULL," +
                "push_date TIMESTAMP," +
                "PRIMARY KEY (asset_id, environment_id)" +
                ");" +
                "DROP INDEX idx_pushed_assets_1;" +
                "DROP INDEX idx_pushed_assets_2;" +
                "DROP INDEX idx_pushed_assets_3;" +
                "CREATE INDEX idx_pushed_assets_1 ON publishing_pushed_assets (bundle_id, environment_id);" +
                "CREATE INDEX idx_pushed_assets_2 ON publishing_pushed_assets (asset_id, push_date);";
    }

    @Override
    public String getMySQLScript() {
        return "create table publishing_last_push(" +
                "asset_id varchar(36) NOT NULL," +
                "environment_id varchar(36) NOT NULL," +
                "push_date DATETIME," +
                "PRIMARY KEY (asset_id, environment_id)" +
                ");" +
                "DROP INDEX idx_pushed_assets_1 on publishing_pushed_assets;" +
                "DROP INDEX idx_pushed_assets_2 on publishing_pushed_assets;" +
                "DROP INDEX idx_pushed_assets_3 on publishing_pushed_assets;" +
                "CREATE INDEX idx_pushed_assets_1 ON publishing_pushed_assets (bundle_id, environment_id);" +
                "CREATE INDEX idx_pushed_assets_2 ON publishing_pushed_assets (asset_id, push_date);";
    }

    @Override
    public String getOracleScript() {
        return "create table publishing_last_push(" +
                "asset_id varchar2(36) NOT NULL," +
                "environment_id varchar2(36) NOT NULL," +
                "push_date TIMESTAMP," +
                "PRIMARY KEY (asset_id, environment_id)" +
                ");" +
                "DROP INDEX idx_pushed_assets_1;" +
                "DROP INDEX idx_pushed_assets_2;" +
                "DROP INDEX idx_pushed_assets_3;" +
                "CREATE INDEX idx_pushed_assets_1 ON publishing_pushed_assets (bundle_id, environment_id);" +
                "CREATE INDEX idx_pushed_assets_2 ON publishing_pushed_assets (asset_id, push_date);";

    }

    @Override
    public String getMSSQLScript() {
        return "create table publishing_last_push(" +
                "asset_id varchar(36) NOT NULL," +
                "environment_id varchar(36) NOT NULL," +
                "push_date DATETIME," +
                "PRIMARY KEY (asset_id, environment_id)" +
                ");" +
                "DROP INDEX idx_pushed_assets_1 ON publishing_pushed_assets;" +
                "DROP INDEX idx_pushed_assets_2 ON publishing_pushed_assets;" +
                "DROP INDEX idx_pushed_assets_3 ON publishing_pushed_assets;" +
                "CREATE INDEX idx_pushed_assets_1 ON publishing_pushed_assets (bundle_id, environment_id);" +
                "CREATE INDEX idx_pushed_assets_2 ON publishing_pushed_assets (asset_id, push_date);";
    }

    @Override
    public String getH2Script() {
        return "create table publishing_last_push(" +
                "asset_id varchar(36) NOT NULL," +
                "environment_id varchar(36) NOT NULL," +
                "push_date TIMESTAMP," +
                "PRIMARY KEY (asset_id, environment_id)" +
                ");" +
                "DROP INDEX idx_pushed_assets_1;" +
                "DROP INDEX idx_pushed_assets_2;" +
                "DROP INDEX idx_pushed_assets_3;" +
                "CREATE INDEX idx_pushed_assets_1 ON publishing_pushed_assets (bundle_id, environment_id);" +
                "CREATE INDEX idx_pushed_assets_2 ON publishing_pushed_assets (asset_id, push_date);";
    }

    @Override
    protected List<String> getTablesToDropConstraints() {
        return null;
    }

}
