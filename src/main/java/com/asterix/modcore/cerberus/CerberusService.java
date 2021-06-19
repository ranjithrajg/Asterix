package com.asterix.modcore.cerberus;

import com.atom.commons.modz.AppContext;
import com.atom.commons.modz.AppModuleContext;
import com.atom.commons.modz.AppModuleClusterMetaInfo;
import com.atom.modgateway.federal.FederalGateway;
import com.atom.commons.concurrent.ThreadPool;
import com.atom.commons.modz.DistributablePluginAppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class CerberusService extends DistributablePluginAppService<CerberusService.Leader, CerberusService.LeadWorker, CerberusService> {

    public static final String CONF_FILE_PATH = System.getProperty("atom.home")       //No I18N
            + File.separator + "conf"       //No I18N
            + File.separator + "Atom"       //No I18N
            + File.separator + "cerberus-module.yml";       //No I18N
    public static final File CONF_FILE = new File(CONF_FILE_PATH);

    private static final Logger LOGGER = LoggerFactory.getLogger("Asterix");        //No I18N

    private static volatile CerberusService instance;

    private final Leader leader = new Leader();
    private final LeadWorker leadWorker = new LeadWorker();
    private ThreadPool threadPool;
    private final Proxy serviceProxy;

    private CerberusService(AppContext appContext, AppModuleContext moduleContext) {
        super(appContext, moduleContext);
        this.serviceProxy = new Proxy();
        super.initRoles(leader, leadWorker, this);
    }

    protected static synchronized CerberusService initInstance(AppContext appContext, AppModuleContext moduleContext) throws Exception {
        if(instance == null) {
            instance = new CerberusService(appContext, moduleContext);
            instance.init();
        } else {
            throw new RuntimeException("CerberusService already initialized!!");       //No I18N
        }
        return instance;
    }

    protected static CerberusService.Proxy getInstance() {
        return instance.serviceProxy;
    }

    @Override
    protected void doInit() throws Exception {
        LOGGER.info("||Cerberus Service|| INITIALIZING...");   // NO I18N
        this.threadPool = FederalGateway.local().threadPool();
        startPreUtilServices();
        LOGGER.info("||Cerberus Service|| INITIALIZED!");   // NO I18N
    }

    @Override
    protected void doStart() throws Exception {
        LOGGER.info("||Cerberus Service|| STARTING...");   // NO I18N
        startPostUtilServices();
        LOGGER.info("||Cerberus Service|| STARTED!");   // NO I18N
    }

    @Override
    protected void doStop() throws Exception {
        LOGGER.info("||Cerberus Service|| STOPPING...");   // NO I18N
        stopPostUtilServices();
        LOGGER.info("||Cerberus Service|| STOPPED!");   // NO I18N
    }

    @Override
    protected void doClose() throws Exception {
        LOGGER.info("||Cerberus Service|| CLOSING...");   // NO I18N
        stopPreUtilServices();
        LOGGER.info("||Cerberus Service|| CLOSED!");   // NO I18N
    }

    protected ThreadPool threadPool() {
        return threadPool;
    }

    protected Proxy serviceProxy() {
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
            return CerberusService.this.threadPool();
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
            return CerberusService.this.isDistributable();
        }

        public boolean isPluggableService() {
            return CerberusService.this.isPluggable();
        }

        public AppModuleClusterMetaInfo getMetaInfo() {
            return CerberusService.this.moduleContext().moduleClusterMetaInfo();
        }
    }
}
