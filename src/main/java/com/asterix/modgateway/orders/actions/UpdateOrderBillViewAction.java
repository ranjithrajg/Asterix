package com.asterix.modgateway.orders.actions;

import com.asterix.modgateway.orders.OrdersClusterActions;
import com.asterix.modgateway.orders.OrdersGateway;
import com.atom.commons.action.ActionHandle;
import com.atom.commons.action.ActionRequest;
import com.atom.commons.action.ActionResponse;
import com.atom.commons.io.MessageInStream;
import com.atom.commons.io.MessageOutStream;
import com.atom.commons.modz.AppModuleId;
import reactor.core.publisher.Mono;

import java.io.IOException;

public class UpdateOrderBillViewAction {

    public static ActionHandle<Request, Response> UPDATE_ORDER_BILL_VIEW = ActionHandle.build(
            "orders:/update-order/bill-view",       //No I18N
            AppModuleId.ORDERS,
            Request::new,
            Response::new,
            UpdateOrderBillViewAction::executeLocally);

    public static Mono<Response> executeLocally(Object localImpl, Request request) throws Exception {
        return ((OrdersClusterActions)localImpl).updateOrderBillView(request);
    }

    public static class Request extends ActionRequest<Response> {

        Request() {
            super(UPDATE_ORDER_BILL_VIEW, OrdersGateway.router());
        }

        @Override
        public void doReadFrom(MessageInStream messageInStream) throws IOException {

        }

        @Override
        public void doWriteTo(MessageOutStream messageOutStream) throws IOException {

        }
    }

    public static class Response extends ActionResponse {

        Response() {
            super(UPDATE_ORDER_BILL_VIEW, OrdersGateway.router());
        }

        @Override
        public void doReadFrom(MessageInStream messageInStream) throws IOException {

        }

        @Override
        public void doWriteTo(MessageOutStream messageOutStream) throws IOException {

        }
    }
}
