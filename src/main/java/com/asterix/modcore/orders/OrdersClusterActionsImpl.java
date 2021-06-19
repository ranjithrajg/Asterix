package com.asterix.modcore.orders;

import com.asterix.modgateway.orders.OrdersClusterActions;
import com.asterix.modgateway.orders.actions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.List;

public class OrdersClusterActionsImpl implements OrdersClusterActions {

    private static final Logger LOGGER = LoggerFactory.getLogger("Asterix");        //No I18N

    @Override
    public Mono<ViewOrdersAction.Response> getOrderList(ViewOrdersAction.Request request) {
        LOGGER.info("-------------------> getOrderList Received : " + request.toString());
        List<Order> orderList = OrdersService.getInstance().getOrderList(request.fromTime.get(), request.toTime.get());
        ViewOrdersAction.Response response = ViewOrdersAction.VIEW_ORDERS.buildResponse()
                                                    .orderList.set(orderList);
        LOGGER.info("-------------------> Response : " + response.toString());
        return Mono.just(response);
    }

    @Override
    public Mono<ViewOrderAction.Response> getOrderInfo(ViewOrderAction.Request request) {
        LOGGER.info("-------------------> getOrderInfo Received : " + request.toString());
        Order order = OrdersService.getInstance().getOrderInfo(request.orderId.get());
        ViewOrderAction.Response response = ViewOrderAction.VIEW_ORDER.buildResponse()
                                                .orderInfo.set(order);
        LOGGER.info("-------------------> Response : " + response.toString());
        return Mono.just(response);
    }

    @Override
    public Mono<AddOrderAction.Response> addOrder(AddOrderAction.Request request) {
        LOGGER.info("-------------------> addOrder Received : " + request.toString());
        OrdersService.getInstance().addOrder(request.order.get());
        AddOrderAction.Response response = AddOrderAction.ADD_ORDER.buildResponse().isUpdated.set(true);
        LOGGER.info("-------------------> Response : " + response.toString());
        return Mono.just(response);
    }

    @Override
    public Mono<ShareBillAction.Response> shareBill(ShareBillAction.Request request) {
        LOGGER.info("-------------------> shareBill Received : " + request.toString());
        //OrdersService.getInstance().shareBill(request.);
        //LOGGER.info("-------------------> Response : " + response.toString());
        return null;
    }

    @Override
    public Mono<UpdateOrderStateAction.Response> updateOrderState(UpdateOrderStateAction.Request request) {
        LOGGER.info("-------------------> updateOrderState Received : " + request.toString());
        OrdersService.getInstance().updateOrderState(request.orderId.get(), request.state.get());
        UpdateOrderStateAction.Response response = UpdateOrderStateAction.UPDATE_ORDER_STATE.buildResponse().isUpdated.set(true);
        LOGGER.info("-------------------> Response : " + response.toString());
       return  Mono.just(response);
    }

    @Override
    public Mono<UpdateOrderOnProcessStatusAction.Response> updateOrderOnProcessStatus(UpdateOrderOnProcessStatusAction.Request request) {
        LOGGER.info("-------------------> updateOrderOnProcessStatus Received : " + request.toString());
        OrdersService.getInstance().updateOrderOnProcessStatus(request.orderId.get(), request.onProcessStatus.get());
        UpdateOrderOnProcessStatusAction.Response response = UpdateOrderOnProcessStatusAction.UPDATE_ORDER_ON_PROCESS_STATUS
                                                                .buildResponse().isUpdated.set(true);
        LOGGER.info("-------------------> Response : " + response.toString());
        return Mono.just(response);
    }

    @Override
    public Mono<UpdateOrderBillViewAction.Response> updateOrderBillView(UpdateOrderBillViewAction.Request request) {
        LOGGER.info("-------------------> updateOrderBillView Received : " + request.toString());
        //LOGGER.info("-------------------> Response : " + response.toString());
        return null;
    }

    @Override
    public Mono<UpdateOrderBillAction.Response> updateOrderBill(UpdateOrderBillAction.Request request) {
        LOGGER.info("-------------------> updateOrderBill Received : " + request.toString());
        //LOGGER.info("-------------------> Response : " + response.toString());
        return null;
    }

    @Override
    public Mono<UpdateOrderMyNotesAction.Response> updateOrderMyNotes(UpdateOrderMyNotesAction.Request request) {
        LOGGER.info("-------------------> updateOrderMyNotes Received : " + request.toString());
        OrdersService.getInstance().updateOrderMyNotes(request.orderId.get(), request.myNotes.get());
        UpdateOrderMyNotesAction.Response response = UpdateOrderMyNotesAction.UPDATE_ORDER_MY_NOTES
                                                            .buildResponse().isUpdated.set(true);
        LOGGER.info("-------------------> Response : " + response.toString());
        return Mono.just(response);
    }

    @Override
    public Mono<UpdateOrderPaymentStatusAction.Response> updateOrderPaymentStatus(UpdateOrderPaymentStatusAction.Request request) {
        LOGGER.info("-------------------> updateOrderPaymentStatus Received : " + request.toString());
        OrdersService.getInstance().updateOrderPaymentStatus(request.orderId.get(), request.paymentStatus.get());
        UpdateOrderPaymentStatusAction.Response response = UpdateOrderPaymentStatusAction.UPDATE_ORDER_PAYMENT_STATUS
                .buildResponse().isUpdated.set(true);
        LOGGER.info("-------------------> Response : " + response.toString());
        return Mono.just(response);
    }
}
