package com.asterix.modgateway.cerberus;

import com.atom.commons.action.ActionRouter;
import com.atom.commons.action.ClusterActions;
import com.atom.commons.concurrent.ThreadPool;
import com.atom.commons.modz.*;
import com.atom.commons.utils.ReflectionUtil;
import com.atom.commons.modz.AppContext;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CerberusGateway extends AppModuleGateway {

    public static final boolean IS_DISTRIBUTABLE = true;
    public static final boolean IS_PLUGGABLE = true;

    private static final String localHandlerForLAClassName = "com.asterix.modcore.cerberus.CerberusLocalActionsImpl";        //No I18N
    private static final String localHandlerForCAClassName = "com.asterix.modcore.cerberus.CerberusClusterActionsImpl";        //No I18N
    private static final Logger LOGGER = LoggerFactory.getLogger("Asterix");        //No I18N

    private static volatile CerberusGateway instance;

    private CerberusLocalActions localHandlerForLocalActions;
    private CerberusClusterActions localHandlerForClusterActions;
    private ActionRouter actionRouter;
    private AppContext appContext;
    private AppModuleGatewayContext moduleContext;

    @Override
    public AppModuleId moduleId() {
        return AppModuleId.CERBERUS;
    }

    @Override
    public synchronized CerberusGateway load(AppContext appContext) throws Exception {
        if(instance == null) {
            instance = new CerberusGateway();
            instance.appContext = appContext;
            instance.moduleContext = new AppModuleGatewayContext(appContext, instance.moduleId(), IS_DISTRIBUTABLE, IS_PLUGGABLE);
            instance.loadLocalImplHandles();
            instance.initRouter();
        } else {
            throw new RuntimeException("CerberusGateway already initialized!!");        //No I18N
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
        CerberusWebActions.registerWebRoutes();
    }

    private void loadLocalImplHandles() throws Exception {
        this.localHandlerForLocalActions = ReflectionUtil.newSubClassInstance(localHandlerForLAClassName, CerberusLocalActions.class);
        this.localHandlerForClusterActions = ReflectionUtil.newSubClassInstance(localHandlerForCAClassName, CerberusClusterActions.class);
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
            throw new NullPointerException("Cerberus Gateway not initialized!!");        //No I18N
        }
    }

    public static ActionRouter router() {
        validateInstance();
        return instance.actionRouter;
    }

    public static CerberusLocalActions local() {
        validateInstance();
        return instance.localHandlerForLocalActions;
    }

    public static void collectLog1(RoutingContext context) {
        //String username = context.request().getParam("username");   // <1>
        context.response().putHeader("content-type", "application/json");
        context.response().end(new JsonObject().put("user", "testuser").put("token", "1234567890222222").toString());
    }
}
