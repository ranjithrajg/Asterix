package com.asterix.modcore.database;

import com.atom.commons.concurrent.ThreadPool;
import com.atom.commons.modz.*;
import com.atom.modgateway.federal.FederalGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class DatabaseService  extends DistributablePluginAppService<DatabaseService.Leader, DatabaseService.LeadWorker, DatabaseService> {

    public static final String CONF_FILE_PATH = System.getProperty("atom.home")       //No I18N
            + File.separator + "conf"       //No I18N
            + File.separator + "Atom"       //No I18N
            + File.separator + "database-module.yml";       //No I18N
    public static final File CONF_FILE = new File(CONF_FILE_PATH);

    private static final Logger LOGGER = LoggerFactory.getLogger("Asterix");        //No I18N

    private static volatile DatabaseService instance;

    private final Leader leader = new Leader();
    private final LeadWorker leadWorker = new LeadWorker();
    private ThreadPool threadPool;
    private DatabaseModuleContext moduleContext;
    private final DatabaseService.Proxy serviceProxy;

    private DatabaseService(AppContext appContext, DatabaseModuleContext moduleContext) {
        super(appContext, moduleContext);
        this.moduleContext = moduleContext;
        this.serviceProxy = new Proxy();
        super.initRoles(leader, leadWorker, this);
    }

    protected static synchronized DatabaseService initInstance(AppContext appContext, DatabaseModuleContext moduleContext) throws Exception {
        if(instance == null) {
            instance = new DatabaseService(appContext, moduleContext);
            instance.init();
        } else {
            throw new RuntimeException("DatabaseService already initialized!!");       //No I18N
        }
        return instance;
    }

    protected static DatabaseService.Proxy getInstance() {
        return instance.serviceProxy;
    }

    @Override
    protected void doInit() throws Exception {
        LOGGER.info("||Database Service|| INITIALIZING...");   // NO I18N
        this.threadPool = FederalGateway.local().threadPool();
        startPreUtilServices();
        MyBatisManager.initInstance(moduleContext);
        LOGGER.info("||Database Service|| INITIALIZED!");   // NO I18N
    }

    @Override
    protected void doStart() throws Exception {
        LOGGER.info("||Database Service|| STARTING...");   // NO I18N
        startPostUtilServices();
        LOGGER.info("||Database Service|| STARTED!");   // NO I18N
    }

    @Override
    protected void doStop() throws Exception {
        LOGGER.info("||Database Service|| STOPPING...");   // NO I18N
        stopPostUtilServices();
        LOGGER.info("||Database Service|| STOPPED!");   // NO I18N
    }

    @Override
    protected void doClose() throws Exception {
        LOGGER.info("||Database Service|| CLOSING...");   // NO I18N
        stopPreUtilServices();
        LOGGER.info("||Database Service|| CLOSED!");   // NO I18N
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
            return DatabaseService.this.threadPool();
        }

        public final String actionExecutorType() {
            return ThreadPool.Names.GENERIC;
        }

        public AppModuleClusterMetaInfo getMetaInfo() {
            return DatabaseService.this.moduleContext().moduleClusterMetaInfo();
        }
    }
}