package com.asterix.modgateway.store;

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

public class StoreGateway extends AppModuleGateway {

    public static final boolean IS_DISTRIBUTABLE = true;
    public static final boolean IS_PLUGGABLE = true;

    private static final String localHandlerForLAClassName = "com.asterix.modcore.store.StoreLocalActionsImpl";        //No I18N
    private static final String localHandlerForCAClassName = "com.asterix.modcore.store.StoreClusterActionsImpl";        //No I18N
    private static final Logger LOGGER = LoggerFactory.getLogger("Asterix");        //No I18N

    private static volatile StoreGateway instance;

    private StoreLocalActions localHandlerForLocalActions;
    private StoreClusterActions localHandlerForClusterActions;
    private ActionRouter actionRouter;
    private AppContext appContext;
    private AppModuleGatewayContext moduleContext;

    @Override
    public AppModuleId moduleId() {
        return AppModuleId.STORE;
    }

    @Override
    public synchronized StoreGateway load(AppContext appContext) throws Exception {
        if(instance == null) {
            instance = new StoreGateway();
            instance.appContext = appContext;
            instance.moduleContext = new AppModuleGatewayContext(appContext, instance.moduleId(), IS_DISTRIBUTABLE, IS_PLUGGABLE);
            instance.loadLocalImplHandles();
            instance.initRouter();
        } else {
            throw new RuntimeException("StoreGateway already initialized!!");        //No I18N
        }
        return instance;
    }

    @Override
    public synchronized void unload() {
        instance = null;
    }

    @Override
    public void registerActionHandles() {
//        ClusterGateway.local().registerActionHandle(CollectLogAction.COLLECT_LOG);
//        ClusterGateway.local().registerActionHandle(CollectLogAction2.COLLECT_LOG_2);
    }

    @Override
    public void registerWebRoutes() {
        StoreWebActions.registerWebRoutes();
    }

    private void loadLocalImplHandles() throws Exception {
        this.localHandlerForLocalActions = ReflectionUtil.newSubClassInstance(localHandlerForLAClassName, StoreLocalActions.class);
        this.localHandlerForClusterActions = ReflectionUtil.newSubClassInstance(localHandlerForCAClassName, StoreClusterActions.class);
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
            throw new NullPointerException("StoreGateway not initialized!!");        //No I18N
        }
    }

    public static ActionRouter router() {
        validateInstance();
        return instance.actionRouter;
    }

    public static StoreLocalActions local() {
        validateInstance();
        return instance.localHandlerForLocalActions;
    }
}

