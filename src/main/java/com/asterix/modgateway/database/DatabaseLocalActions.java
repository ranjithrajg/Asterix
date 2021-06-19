package com.asterix.modgateway.database;

import com.atom.commons.modz.AppModuleClusterMetaInfo;
import org.apache.ibatis.session.SqlSessionFactory;

public interface DatabaseLocalActions {

    public AppModuleClusterMetaInfo getModuleMetaInfo();

    public SqlSessionFactory getMyBatisSqlSessionFactory();
}
