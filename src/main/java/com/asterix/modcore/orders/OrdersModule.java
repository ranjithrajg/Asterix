package com.asterix.modcore.orders;


import com.asterix.modcore.cerberus.CerberusService;
import com.atom.commons.modz.*;
import com.atom.modgateway.cluster.ClusterGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrdersModule implements AppModule {

    private static final Logger LOGGER = LoggerFactory.getLogger("Asterix");        //No I18N

    public static final boolean IS_DISTRIBUTABLE = true;
    public static final boolean IS_PLUGGABLE = true;

    private OrdersModuleContext moduleContext;
    private OrdersService ordersService;

    @Override
    public AppModuleId moduleId() {
        return AppModuleId.ORDERS;
    }

    @Override
    public void load(AppContext appContext) throws Exception {
        LOGGER.info("LOADING Cerberus Module...");   // NO I18N
        this.moduleContext = new OrdersModuleContext(appContext, IS_DISTRIBUTABLE, IS_PLUGGABLE);
        this.ordersService = OrdersService.initInstance(appContext, moduleContext);
        registerModuleWithCluster();
        LOGGER.info("LOADED Cerberus Module!");   // NO I18N
    }

    @Override
    public void start() throws Exception {
        LOGGER.info("STARTING Cerberus Module...");   // NO I18N
        ordersService.start();
        LOGGER.info("STARTED Cerberus Module!");   // NO I18N
    }

    @Override
    public void stop() throws Exception {
        LOGGER.info("STOPPING Cerberus Module...");   // NO I18N
        ordersService.stop();
        LOGGER.info("STOPPED Cerberus Module!");   // NO I18N
    }

    @Override
    public void unload() throws Exception {
        LOGGER.info("UNLOADING Cerberus Module...");   // NO I18N
        ordersService.close();
        LOGGER.info("UNLOADED Cerberus Module!");   // NO I18N
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
        if(ordersService.started()) {
            return Health.GREEN;
        } else if(ordersService.initialized()) {
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

