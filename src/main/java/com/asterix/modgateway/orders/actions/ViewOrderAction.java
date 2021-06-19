package com.asterix.modgateway.orders.actions;

import com.asterix.modcore.orders.Order;
import com.asterix.modgateway.orders.OrdersClusterActions;
import com.asterix.modgateway.orders.OrdersGateway;
import com.atom.commons.action.ActionHandle;
import com.atom.commons.action.ActionRequest;
import com.atom.commons.action.ActionResponse;
import com.atom.commons.io.MessageInStream;
import com.atom.commons.io.MessageOutStream;
import com.atom.commons.io.Streamable;
import com.atom.commons.modz.AppModuleId;
import reactor.core.publisher.Mono;
import java.io.IOException;


public class ViewOrderAction {

    public static ActionHandle<Request, Response> VIEW_ORDER = ActionHandle.build(
            "orders:/view-order",       //No I18N
            AppModuleId.ORDERS,
            Request::new,
            Response::new,
            ViewOrderAction::executeLocally);

    public static Mono<Response> executeLocally(Object localImpl, Request request) throws Exception {
        return ((OrdersClusterActions)localImpl).getOrderInfo(request);
    }

    public static class Request extends ActionRequest<Response> {

        public final Streamable<Long, Request> orderId = Streamable.longType("orderId", this);       //No I18N

        Request() {
            super(VIEW_ORDER, OrdersGateway.router());
        }

        @Override
        public void doReadFrom(MessageInStream messageInStream) throws IOException {
            orderId.readFrom(messageInStream);
        }

        @Override
        public void doWriteTo(MessageOutStream messageOutStream) throws IOException {
            orderId.writeTo(messageOutStream);
        }
    }

    public static class Response extends ActionResponse {

        public final Streamable<Order, Response> orderInfo = Streamable.objectType("orderInfo", this, Order.class);       //No I18N

        Response() {
            super(VIEW_ORDER, OrdersGateway.router());
        }

        @Override
        public void doReadFrom(MessageInStream messageInStream) throws IOException {
            orderInfo.readFrom(messageInStream);
        }

        @Override
        public void doWriteTo(MessageOutStream messageOutStream) throws IOException {
            orderInfo.writeTo(messageOutStream);
        }
    }
}
