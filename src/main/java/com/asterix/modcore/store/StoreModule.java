package com.asterix.modcore.store;

import com.atom.commons.modz.*;
import com.atom.modgateway.cluster.ClusterGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StoreModule implements AppModule {

    private static final Logger LOGGER = LoggerFactory.getLogger("Asterix");        //No I18N

    public static final boolean IS_DISTRIBUTABLE = true;
    public static final boolean IS_PLUGGABLE = true;

    private StoreModuleContext moduleContext;
    private StoreService storeService;

    @Override
    public AppModuleId moduleId() {
        return AppModuleId.STORE;
    }

    @Override
    public void load(AppContext appContext) throws Exception {
        LOGGER.info("LOADING Store Module...");   // NO I18N
        this.moduleContext = new StoreModuleContext(appContext, IS_DISTRIBUTABLE, IS_PLUGGABLE);
        this.storeService = StoreService.initInstance(appContext, moduleContext);
        registerModuleWithCluster();
        LOGGER.info("LOADED Store Module!");   // NO I18N
    }

    @Override
    public void start() throws Exception {
        LOGGER.info("STARTING Store Module...");   // NO I18N
        storeService.start();
        LOGGER.info("STARTED Store Module!");   // NO I18N
    }

    @Override
    public void stop() throws Exception {
        LOGGER.info("STOPPING Store Module...");   // NO I18N
        storeService.stop();
        LOGGER.info("STOPPED Store Module!");   // NO I18N
    }

    @Override
    public void unload() throws Exception {
        LOGGER.info("UNLOADING Store Module...");   // NO I18N
        storeService.close();
        LOGGER.info("UNLOADED Store Module!");   // NO I18N
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
        if(storeService.started()) {
            return Health.GREEN;
        } else if(storeService.initialized()) {
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