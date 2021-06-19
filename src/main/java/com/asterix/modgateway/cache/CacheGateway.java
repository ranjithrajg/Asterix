package com.asterix.modgateway.cache;

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

public class CacheGateway extends AppModuleGateway {

    public static final boolean IS_DISTRIBUTABLE = true;
    public static final boolean IS_PLUGGABLE = true;

    private static final String localHandlerForLAClassName = "com.asterix.modcore.cache.CacheLocalActionsImpl";        //No I18N
    private static final String localHandlerForCAClassName = "com.asterix.modcore.cache.CacheClusterActionsImpl";        //No I18N
    private static final Logger LOGGER = LoggerFactory.getLogger("Asterix");        //No I18N

    private static volatile CacheGateway instance;

    private CacheLocalActions localHandlerForLocalActions;
    private CacheClusterActions localHandlerForClusterActions;
    private ActionRouter actionRouter;
    private AppContext appContext;
    private AppModuleGatewayContext moduleContext;

    @Override
    public AppModuleId moduleId() {
        return AppModuleId.CACHE;
    }

    @Override
    public synchronized CacheGateway load(AppContext appContext) throws Exception {
        if(instance == null) {
            instance = new CacheGateway();
            instance.appContext = appContext;
            instance.moduleContext = new AppModuleGatewayContext(appContext, instance.moduleId(), IS_DISTRIBUTABLE, IS_PLUGGABLE);
            instance.loadLocalImplHandles();
            instance.initRouter();
        } else {
            throw new RuntimeException("CacheGateway already initialized!!");        //No I18N
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

    public static CacheLocalActions local() {
        validateInstance();
        return instance.localHandlerForLocalActions;
    }

    private void loadLocalImplHandles() throws Exception {
        this.localHandlerForLocalActions = ReflectionUtil.newSubClassInstance(localHandlerForLAClassName, CacheLocalActions.class);
        this.localHandlerForClusterActions = ReflectionUtil.newSubClassInstance(localHandlerForCAClassName, CacheClusterActions.class);
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
            throw new NullPointerException("Cache Gateway not initialized!!");        //No I18N
        }
    }
}

