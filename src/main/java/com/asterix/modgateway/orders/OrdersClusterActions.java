package com.asterix.modgateway.orders;

import com.asterix.modgateway.orders.actions.*;
import com.atom.commons.action.ClusterActions;
import reactor.core.publisher.Mono;

public interface OrdersClusterActions extends ClusterActions {

    public Mono<ViewOrdersAction.Response> getOrderList(ViewOrdersAction.Request request);
    public Mono<ViewOrderAction.Response> getOrderInfo(ViewOrderAction.Request request);
    public Mono<AddOrderAction.Response> addOrder(AddOrderAction.Request request);
    public Mono<ShareBillAction.Response> shareBill(ShareBillAction.Request request);
    public Mono<UpdateOrderStateAction.Response> updateOrderState(UpdateOrderStateAction.Request request);
    public Mono<UpdateOrderOnProcessStatusAction.Response> updateOrderOnProcessStatus(UpdateOrderOnProcessStatusAction.Request request);
    public Mono<UpdateOrderBillViewAction.Response> updateOrderBillView(UpdateOrderBillViewAction.Request request);
    public Mono<UpdateOrderBillAction.Response> updateOrderBill(UpdateOrderBillAction.Request request);
    public Mono<UpdateOrderMyNotesAction.Response> updateOrderMyNotes(UpdateOrderMyNotesAction.Request request);
    public Mono<UpdateOrderPaymentStatusAction.Response> updateOrderPaymentStatus(UpdateOrderPaymentStatusAction.Request request);
}
