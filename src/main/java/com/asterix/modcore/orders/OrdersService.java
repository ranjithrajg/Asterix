package com.asterix.modcore.orders;

import com.asterix.modgateway.orders.actions.*;
import com.atom.commons.concurrent.ThreadPool;
import com.atom.commons.modz.AppContext;
import com.atom.commons.modz.AppModuleClusterMetaInfo;
import com.atom.commons.modz.AppModuleContext;
import com.atom.commons.modz.DistributablePluginAppService;
import com.atom.modgateway.federal.FederalGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrdersService extends DistributablePluginAppService<OrdersService.Leader, OrdersService.LeadWorker, OrdersService> {

    public static final String CONF_FILE_PATH = System.getProperty("atom.home")       //No I18N
            + File.separator + "conf"       //No I18N
            + File.separator + "Atom"       //No I18N
            + File.separator + "orders-module.yml";       //No I18N
    public static final File CONF_FILE = new File(CONF_FILE_PATH);

    private static final Logger LOGGER = LoggerFactory.getLogger("Asterix");        //No I18N

    private static volatile OrdersService instance;

    private final Leader leader = new Leader();
    private final LeadWorker leadWorker = new LeadWorker();
    private ThreadPool threadPool;
    private final OrdersService.Proxy serviceProxy;

    private OrdersService(AppContext appContext, AppModuleContext moduleContext) {
        super(appContext, moduleContext);
        this.serviceProxy = new OrdersService.Proxy();
        super.initRoles(leader, leadWorker, this);
    }

    protected static synchronized OrdersService initInstance(AppContext appContext, AppModuleContext moduleContext) throws Exception {
        if(instance == null) {
            instance = new OrdersService(appContext, moduleContext);
            instance.init();
        } else {
            throw new RuntimeException("OrdersService already initialized!!");       //No I18N
        }
        return instance;
    }

    protected static OrdersService.Proxy getInstance() {
        return instance.serviceProxy;
    }

    @Override
    protected void doInit() throws Exception {
        LOGGER.info("||Orders Service|| INITIALIZING...");   // NO I18N
        this.threadPool = FederalGateway.local().threadPool();
        LOGGER.info("||Orders Service|| INITIALIZED!");   // NO I18N
    }

    @Override
    protected void doStart() throws Exception {
        LOGGER.info("||Orders Service|| STARTING...");   // NO I18N
        LOGGER.info("||Orders Service|| STARTED!");   // NO I18N
    }

    @Override
    protected void doStop() throws Exception {
        LOGGER.info("||Orders Service|| STOPPING...");   // NO I18N
        LOGGER.info("||Orders Service|| STOPPED!");   // NO I18N
    }

    @Override
    protected void doClose() throws Exception {
        LOGGER.info("||Orders Service|| CLOSING...");   // NO I18N
        LOGGER.info("||Orders Service|| CLOSED!");   // NO I18N
    }

    protected ThreadPool threadPool() {
        return threadPool;
    }

    protected OrdersService.Proxy serviceProxy() {
        return serviceProxy;
    }



    class Leader {

    }

    class LeadWorker {

    }

    public class Proxy {

        List<Order> dummyList = new ArrayList<>();

        Proxy() {
            Order order = new Order(123456L, OrderState.NEW);
            order.setCustomerId(123L);
            order.setCreationTime(new Date(1623541342000L));
            order.setDeliveredTime(new Date());
            order.setElapsedTime(123666L);
            order.setTotalAmount(1000);
            order.setTotalItems(5);
            order.setPaymentType(PaymentType.COD);
            order.setDeliveryType(DeliveryType.PICKUP);
            order.setModified(false);
            order.setLastModifiedTime(new Date());
            order.setOnProcessStatus(OnProcessStatus.PROCESSING_ORDER);
            order.setCancelledReason("None");
            order.setInstructions("Please deliver by today");
            order.setMyNotes("ererer");
            dummyList.add(order);

            order = new Order(123457L, OrderState.NEW);
            order.setCustomerId(124L);
            order.setCreationTime(new Date(1623537742000L));
            order.setDeliveredTime(new Date());
            order.setElapsedTime(123666L);
            order.setTotalAmount(1000);
            order.setTotalItems(5);
            order.setPaymentType(PaymentType.PAYTM);
            order.setDeliveryType(DeliveryType.DELIVERY);
            order.setModified(false);
            order.setLastModifiedTime(new Date());
            order.setOnProcessStatus(OnProcessStatus.PROCESSING_ORDER);
            order.setCancelledReason("None");
            order.setInstructions("Please deliver by today");
            order.setMyNotes("ererer");
            dummyList.add(order);

            order = new Order(123458L, OrderState.ON_PROCESS);
            order.setCustomerId(125L);
            order.setCreationTime(new Date(1623451342000L));
            order.setDeliveredTime(new Date());
            order.setElapsedTime(123666L);
            order.setTotalAmount(1000);
            order.setTotalItems(5);
            order.setPaymentType(PaymentType.PAYTM);
            order.setDeliveryType(DeliveryType.DELIVERY);
            order.setModified(false);
            order.setLastModifiedTime(new Date());
            order.setOnProcessStatus(OnProcessStatus.PROCESSING_ORDER);
            order.setCancelledReason("None");
            order.setInstructions("Please deliver by today");
            order.setMyNotes("ererer");
            dummyList.add(order);

            order = new Order(123659L, OrderState.ON_PROCESS);
            order.setCustomerId(126L);
            order.setCreationTime(new Date(1623426142000L));
            order.setDeliveredTime(new Date());
            order.setElapsedTime(123666L);
            order.setTotalAmount(1000);
            order.setTotalItems(5);
            order.setPaymentType(PaymentType.PAYTM);
            order.setDeliveryType(DeliveryType.DELIVERY);
            order.setModified(true);
            order.setLastModifiedTime(new Date());
            order.setOnProcessStatus(OnProcessStatus.READY_FOR_DELIVERY);
            order.setCancelledReason("None");
            order.setInstructions("Please deliver by today");
            order.setMyNotes("ererer");
            dummyList.add(order);

            order = new Order(123660L, OrderState.CANCELLED);
            order.setCustomerId(127L);
            order.setCreationTime(new Date(1623426142000L));
            order.setDeliveredTime(new Date());
            order.setElapsedTime(123666L);
            order.setTotalAmount(1000);
            order.setTotalItems(5);
            order.setPaymentType(PaymentType.PAYTM);
            order.setDeliveryType(DeliveryType.DELIVERY);
            order.setModified(false);
            order.setLastModifiedTime(new Date());
            order.setOnProcessStatus(OnProcessStatus.PROCESSING_ORDER);
            order.setCancelledReason("Out of Stock");
            order.setInstructions("Please deliver by today");
            order.setMyNotes("ererer");
            dummyList.add(order);

            order = new Order(123461L, OrderState.DELIVERED);
            order.setCustomerId(128L);
            order.setCreationTime(new Date(1623422542000L));
            order.setDeliveredTime(new Date(1623426142000L));
            order.setElapsedTime(123666L);
            order.setTotalAmount(1000);
            order.setTotalItems(5);
            order.setPaymentType(PaymentType.PAYTM);
            order.setDeliveryType(DeliveryType.DELIVERY);
            order.setModified(false);
            order.setLastModifiedTime(new Date());
            order.setOnProcessStatus(OnProcessStatus.PROCESSING_ORDER);
            order.setCancelledReason("");
            order.setInstructions("Please deliver by today");
            order.setMyNotes("ererer");
            dummyList.add(order);
        }

        public final ThreadPool threadPool() {
            return OrdersService.this.threadPool();
        }

        public final String actionExecutorType() {
            return ThreadPool.Names.GENERIC;
        }

        public AppModuleClusterMetaInfo getMetaInfo() {
            return OrdersService.this.moduleContext().moduleClusterMetaInfo();
        }

        public List<Order> getOrderList(Date fromTime, Date toTime) {
            return dummyList;
        }

        public Order getOrderInfo(long orderId) {
            for(Order order : dummyList) {
                if(order.getOrderId() == orderId) {
                    return order;
                }
            }
            return null;
        }

        public void addOrder(Order order) {
            order.setOrderId(dummyList.size() + 123456L);
            order.setCreationTime(new Date());
            order.setDeliveredTime(new Date(0));
            order.setElapsedTime(0L);
            order.setTotalAmount(5000);
            order.setTotalItems(50);
            order.setModified(false);
            order.setLastModifiedTime(new Date(0));
            order.setOnProcessStatus(OnProcessStatus.PROCESSING_ORDER);
            order.setCancelledReason("");
            dummyList.add(order);
        }

        public void shareBill(long orderId) {

        }

        public void updateOrderState(long orderId, OrderState newState) {
            for(Order order : dummyList) {
                if(order.getOrderId() == orderId) {
                    order.changeState(newState);
                    break;
                }
            }
            throw new IllegalArgumentException("Order not found for order Id : " + orderId);
        }

        public void updateOrderOnProcessStatus(long orderId, OnProcessStatus newStatus) {
            for(Order order : dummyList) {
                if(order.getOrderId() == orderId) {
                    order.setOnProcessStatus(newStatus);
                    break;
                }
            }
            throw new IllegalArgumentException("Order not found for order Id : " + orderId);
        }

        public void updateOrderBillView(long orderId) {

        }

        public void updateOrderBill(long orderId, String myNotes) {

        }

        public void updateOrderMyNotes(long orderId, String myNotes) {
            for(Order order : dummyList) {
                if(order.getOrderId() == orderId) {
                    order.setMyNotes(myNotes);
                    break;
                }
            }
            throw new IllegalArgumentException("Order not found for order Id : " + orderId);
        }

        public void updateOrderPaymentStatus(long orderId, PaymentStatus paymentStatus) {
            for(Order order : dummyList) {
                if(order.getOrderId() == orderId) {
                    order.setPaymentStatus(paymentStatus);
                    break;
                }
            }
            throw new IllegalArgumentException("Order not found for order Id : " + orderId);
        }
    }
}
