package com.asterix.modcore.database;

import com.atom.commons.modz.AppModuleClusterMetaInfo;
import com.asterix.modgateway.database.DatabaseLocalActions;
import org.apache.ibatis.session.SqlSessionFactory;

public class DatabaseLocalActionsImpl implements DatabaseLocalActions {

    @Override
    public AppModuleClusterMetaInfo getModuleMetaInfo() {
        return DatabaseService.getInstance().getMetaInfo();
    }

    @Override
    public SqlSessionFactory getMyBatisSqlSessionFactory() {
        return MyBatisManager.getInstance().getSqlSessionFactory();
    }
}
