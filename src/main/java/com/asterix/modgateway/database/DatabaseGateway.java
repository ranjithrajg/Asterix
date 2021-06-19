package com.asterix.modgateway.database;

import com.atom.commons.action.ActionRouter;
import com.atom.commons.action.ClusterActions;
import com.atom.commons.concurrent.ThreadPool;
import com.atom.commons.modz.AppContext;
import com.atom.commons.modz.AppModuleGateway;
import com.atom.commons.modz.AppModuleGatewayContext;
import com.atom.commons.modz.AppModuleId;
import com.atom.commons.utils.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseGateway extends AppModuleGateway {

    public static final boolean IS_DISTRIBUTABLE = true;
    public static final boolean IS_PLUGGABLE = true;

    private static final String localHandlerForLAClassName = "com.asterix.modcore.database.DatabaseLocalActionsImpl";        //No I18N
    private static final String localHandlerForCAClassName = "com.asterix.modcore.database.DatabaseClusterActionsImpl";        //No I18N
    private static final Logger LOGGER = LoggerFactory.getLogger("Asterix");        //No I18N

    private static volatile DatabaseGateway instance;

    private DatabaseLocalActions localHandlerForLocalActions;
    private DatabaseClusterActions localHandlerForClusterActions;
    private ActionRouter actionRouter;
    private AppContext appContext;
    private AppModuleGatewayContext moduleContext;

    @Override
    public AppModuleId moduleId() {
        return AppModuleId.DATABASE;
    }

    @Override
    public synchronized DatabaseGateway load(AppContext appContext) throws Exception {
        if(instance == null) {
            instance = new DatabaseGateway();
            instance.appContext = appContext;
            instance.moduleContext = new AppModuleGatewayContext(appContext, instance.moduleId(), IS_DISTRIBUTABLE, IS_PLUGGABLE);
            instance.loadLocalImplHandles();
            instance.initRouter();
        } else {
            throw new RuntimeException("DatabaseGateway already initialized!!");        //No I18N
        }
        return instance;
    }

    @Override
    public synchronized void unload() {
        instance = null;
    }

    @Override
    public void registerActionHandles() {
    }

    @Override
    public void registerWebRoutes() {
    }

    public static ActionRouter router() {
        validateInstance();
        return instance.actionRouter;
    }

    public static DatabaseLocalActions local() {
        validateInstance();
        return instance.localHandlerForLocalActions;
    }

    private void loadLocalImplHandles() throws Exception {
        this.localHandlerForLocalActions = ReflectionUtil.newSubClassInstance(localHandlerForLAClassName, DatabaseLocalActions.class);
        this.localHandlerForClusterActions = ReflectionUtil.newSubClassInstance(localHandlerForCAClassName, DatabaseClusterActions.class);
    }

    private void initRouter() {
        this.actionRouter = new ActionRouter(moduleContext) {

            @Override
            public ClusterActions localImplForClusterActions() {
                return localHandlerForClusterActions;
            }

            @Override
            public String actionExecutorThreadPoolType() {
                return ThreadPool.Names.GENERIC;
            }
        };
    }

    private static void validateInstance() {
        if(instance == null) {
            throw new NullPointerException("Database Gateway not initialized!!");        //No I18N
        }
    }
}
