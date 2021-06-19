package com.asterix.modcore.database;

import com.asterix.modcore.cerberus.db.CerberusQueryMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class MyBatisManager {

    public static final String CONF_FILE_NAME = "mybatis-config.xml";       
    public static final String CONF_FILE_PATH = System.getProperty("atom.home")
            + File.separator + "conf"       
            + File.separator + "db"       
            + File.separator + CONF_FILE_NAME;       

    private static final Logger LOGGER = LoggerFactory.getLogger("Asterix");

    private static volatile MyBatisManager instance;

    private final DatabaseModuleContext moduleContext;

    private final SqlSessionFactory sqlSessionFactory;

    private MyBatisManager(DatabaseModuleContext moduleContext) throws IOException {
        this.moduleContext = moduleContext;
        this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream(CONF_FILE_NAME));
        sqlSessionFactory.getConfiguration().addMapper(CerberusQueryMapper.class);
    }

    protected static synchronized MyBatisManager initInstance(DatabaseModuleContext moduleContext) throws Exception {
        if(instance == null) {
            instance = new MyBatisManager(moduleContext);
        } else {
            throw new RuntimeException("MyBatisManager already initialized!!");      
        }
        return instance;
    }

    protected static MyBatisManager getInstance() {
        return instance;
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }
}
