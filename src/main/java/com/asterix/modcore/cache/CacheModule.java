package com.asterix.modcore.cache;

import com.asterix.modgateway.cache.CacheGateway;
import com.atom.commons.modz.*;
import com.atom.modgateway.cluster.ClusterGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheModule implements AppModule {

    private static final Logger LOGGER = LoggerFactory.getLogger("Asterix");        //No I18N

    public static final boolean IS_DISTRIBUTABLE = CacheGateway.IS_DISTRIBUTABLE;
    public static final boolean IS_PLUGGABLE = CacheGateway.IS_PLUGGABLE;

    private CacheModuleContext moduleContext;
    private CacheService cacheService;

    @Override
    public AppModuleId moduleId() {
        return AppModuleId.CACHE;
    }

    @Override
    public void load(AppContext appContext) throws Exception {
        LOGGER.info("LOADING Cache Module...");   // NO I18N
        this.moduleContext = new CacheModuleContext(appContext, IS_DISTRIBUTABLE, IS_PLUGGABLE);
        this.cacheService = CacheService.initInstance(appContext, moduleContext);
        registerModuleWithCluster();
        LOGGER.info("LOADED Cache Module!");   // NO I18N
    }

    @Override
    public void start() throws Exception {
        LOGGER.info("STARTING Cache Module...");   // NO I18N
        cacheService.start();
        LOGGER.info("STARTED Cache Module!");   // NO I18N
    }

    @Override
    public void stop() throws Exception {
        LOGGER.info("STOPPING Cache Module...");   // NO I18N
        cacheService.stop();
        LOGGER.info("STOPPED Cache Module!");   // NO I18N
    }

    @Override
    public void unload() throws Exception {
        LOGGER.info("UNLOADING Cache Module...");   // NO I18N
        cacheService.close();
        LOGGER.info("UNLOADED Cache Module!");   // NO I18N
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
        if(cacheService.started()) {
            return Health.GREEN;
        } else if(cacheService.initialized()) {
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