package com.asterix.modcore.store;

import com.atom.commons.concurrent.ThreadPool;
import com.atom.commons.modz.AppContext;
import com.atom.commons.modz.AppModuleClusterMetaInfo;
import com.atom.commons.modz.AppModuleContext;
import com.atom.commons.modz.DistributablePluginAppService;
import com.atom.modgateway.federal.FederalGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class StoreService extends DistributablePluginAppService<StoreService.Leader, StoreService.LeadWorker, StoreService> {

    public static final String CONF_FILE_PATH = System.getProperty("atom.home")       //No I18N
            + File.separator + "conf"       //No I18N
            + File.separator + "Atom"       //No I18N
            + File.separator + "store-module.yml";       //No I18N
    public static final File CONF_FILE = new File(CONF_FILE_PATH);

    private static final Logger LOGGER = LoggerFactory.getLogger("Asterix");        //No I18N

    private static volatile StoreService instance;

    private final StoreService.Leader leader = new StoreService.Leader();
    private final StoreService.LeadWorker leadWorker = new StoreService.LeadWorker();
    private ThreadPool threadPool;
    private final StoreService.Proxy serviceProxy;

    private StoreService(AppContext appContext, AppModuleContext moduleContext) {
        super(appContext, moduleContext);
        this.serviceProxy = new StoreService.Proxy();
        super.initRoles(leader, leadWorker, this);
    }

    protected static synchronized StoreService initInstance(AppContext appContext, AppModuleContext moduleContext) throws Exception {
        if(instance == null) {
            instance = new StoreService(appContext, moduleContext);
            instance.init();
        } else {
            throw new RuntimeException("StoreService already initialized!!");       //No I18N
        }
        return instance;
    }

    protected static StoreService.Proxy getInstance() {
        return instance.serviceProxy;
    }

    @Override
    protected void doInit() throws Exception {
        LOGGER.info("||Store Service|| INITIALIZING...");   // NO I18N
        this.threadPool = FederalGateway.local().threadPool();
        startPreUtilServices();
        LOGGER.info("||Store Service|| INITIALIZED!");   // NO I18N
    }

    @Override
    protected void doStart() throws Exception {
        LOGGER.info("||Store Service|| STARTING...");   // NO I18N
        startPostUtilServices();
        LOGGER.info("||Store Service|| STARTED!");   // NO I18N
    }

    @Override
    protected void doStop() throws Exception {
        LOGGER.info("||Store Service|| STOPPING...");   // NO I18N
        stopPostUtilServices();
        LOGGER.info("||Store Service|| STOPPED!");   // NO I18N
    }

    @Override
    protected void doClose() throws Exception {
        LOGGER.info("||Store Service|| CLOSING...");   // NO I18N
        stopPreUtilServices();
        LOGGER.info("||Store Service|| CLOSED!");   // NO I18N
    }

    protected ThreadPool threadPool() {
        return threadPool;
    }

    protected StoreService.Proxy serviceProxy() {
        return serviceProxy;
    }

    private void startPreUtilServices() throws Exception {
    }

    private void stopPreUtilServices() throws Exception {
    }

    private void startPostUtilServices() throws Exception {
    }

    private void stopPostUtilServices() throws Exception {
    }

    class Leader {

    }

    class LeadWorker {

    }

    public class Proxy {

        public final ThreadPool threadPool() {
            return StoreService.this.threadPool();
        }

        public final String actionExecutorType() {
            return ThreadPool.Names.GENERIC;
        }

        public final String nativeNodeId() {
            return FederalGateway.local().nativeNodeId();
        }

        public final String nativeNodeClusterRole() {
            return FederalGateway.local().nativeNodeClusterRole();
        }

        public boolean isDistributableService() {
            return StoreService.this.isDistributable();
        }

        public boolean isPluggableService() {
            return StoreService.this.isPluggable();
        }

        public AppModuleClusterMetaInfo getMetaInfo() {
            return StoreService.this.moduleContext().moduleClusterMetaInfo();
        }
    }
}

