package com.asterix.modgateway.orders;

import com.asterix.modgateway.orders.actions.ViewOrdersAction;
import com.atom.commons.action.ActionRouter;
import com.atom.commons.action.ClusterActions;
import com.atom.commons.concurrent.ThreadPool;
import com.atom.commons.modz.AppContext;
import com.atom.commons.modz.AppModuleGateway;
import com.atom.commons.modz.AppModuleGatewayContext;
import com.atom.commons.modz.AppModuleId;
import com.atom.commons.utils.ReflectionUtil;
import com.atom.modgateway.cluster.ClusterGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrdersGateway extends AppModuleGateway {

    public static final boolean IS_DISTRIBUTABLE = true;
    public static final boolean IS_PLUGGABLE = true;

    private static final String localHandlerForLAClassName = "com.asterix.modcore.orders.OrdersLocalActionsImpl";        //No I18N
    private static final String localHandlerForCAClassName = "com.asterix.modcore.orders.OrdersClusterActionsImpl";        //No I18N
    private static final Logger LOGGER = LoggerFactory.getLogger("Asterix");        //No I18N

    private static volatile OrdersGateway instance;

    private OrdersLocalActions localHandlerForLocalActions;
    private OrdersClusterActions localHandlerForClusterActions;
    private ActionRouter actionRouter;
    private AppContext appContext;
    private AppModuleGatewayContext moduleContext;

    @Override
    public AppModuleId moduleId() {
        return AppModuleId.ORDERS;
    }

    @Override
    public synchronized OrdersGateway load(AppContext appContext) throws Exception {
        if(instance == null) {
            instance = new OrdersGateway();
            instance.appContext = appContext;
            instance.moduleContext = new AppModuleGatewayContext(appContext, instance.moduleId(), IS_DISTRIBUTABLE, IS_PLUGGABLE);
            instance.loadLocalImplHandles();
            instance.initRouter();
        } else {
            throw new RuntimeException("OrdersGateway already initialized!!");        //No I18N
        }
        return instance;
    }

    @Override
    public synchronized void unload() {
        instance = null;
    }

    @Override
    public void registerActionHandles() {
        ClusterGateway.local().registerActionHandle(ViewOrdersAction.VIEW_ORDERS);
//        ClusterGateway.local().registerActionHandle(CollectLogAction2.COLLECT_LOG_2);
    }

    @Override
    public void registerWebRoutes() {
        OrdersWebActions.registerWebRoutes();
    }

    private void loadLocalImplHandles() throws Exception {
        this.localHandlerForLocalActions = ReflectionUtil.newSubClassInstance(localHandlerForLAClassName, OrdersLocalActions.class);
        this.localHandlerForClusterActions = ReflectionUtil.newSubClassInstance(localHandlerForCAClassName, OrdersClusterActions.class);
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
            throw new NullPointerException("OrdersGateway not initialized!!");        //No I18N
        }
    }

    public static ActionRouter router() {
        validateInstance();
        return instance.actionRouter;
    }

    public static OrdersLocalActions local() {
        validateInstance();
        return instance.localHandlerForLocalActions;
    }
}
