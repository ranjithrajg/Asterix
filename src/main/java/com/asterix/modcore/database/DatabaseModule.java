package com.asterix.modcore.database;

import com.asterix.modgateway.database.DatabaseGateway;
import com.atom.commons.modz.*;
import com.atom.modgateway.cluster.ClusterGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseModule implements AppModule {

    private static final Logger LOGGER = LoggerFactory.getLogger("Asterix");        //No I18N

    public static final boolean IS_DISTRIBUTABLE = DatabaseGateway.IS_DISTRIBUTABLE;
    public static final boolean IS_PLUGGABLE = DatabaseGateway.IS_PLUGGABLE;

    private DatabaseModuleContext moduleContext;
    private DatabaseService databaseService;

    @Override
    public AppModuleId moduleId() {
        return AppModuleId.DATABASE;
    }

    @Override
    public void load(AppContext appContext) throws Exception {
        LOGGER.info("LOADING Database Module...");   // NO I18N
        this.moduleContext = new DatabaseModuleContext(appContext, IS_DISTRIBUTABLE, IS_PLUGGABLE);
        this.databaseService = DatabaseService.initInstance(appContext, moduleContext);
        registerModuleWithCluster();
        LOGGER.info("LOADED Database Module!");   // NO I18N
    }

    @Override
    public void start() throws Exception {
        LOGGER.info("STARTING Database Module...");   // NO I18N
        databaseService.start();
        LOGGER.info("STARTED Database Module!");   // NO I18N
    }

    @Override
    public void stop() throws Exception {
        LOGGER.info("STOPPING Database Module...");   // NO I18N
        databaseService.stop();
        LOGGER.info("STOPPED Database Module!");   // NO I18N
    }

    @Override
    public void unload() throws Exception {
        LOGGER.info("UNLOADING Database Module...");   // NO I18N
        databaseService.close();
        LOGGER.info("UNLOADED Database Module!");   // NO I18N
    }

    @Override
    public boolean isDistributable() {
        return IS_DISTRIBUTABLE;
    }

    @Override
    public boolean isPluggable() {
        return IS_PLUGGABLE;
    }

    @Override
    public Health health() {
        if(databaseService.started()) {
            return Health.GREEN;
        } else if(databaseService.initialized()) {
            return Health.YELLOW;
        } else {
            return Health.RED;
        }
    }

    @Override
    public AppModuleClusterMetaInfo getClusterMetaInfo() {
        return moduleContext.moduleClusterMetaInfo();
    }

    private void registerModuleWithCluster() throws Exception {
        ClusterGateway.local().registerLocalModule(moduleContext.moduleClusterMetaInfo());
    }
}
