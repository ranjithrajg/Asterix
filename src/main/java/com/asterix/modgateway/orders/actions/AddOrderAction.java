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

public class AddOrderAction {

    public static ActionHandle<Request, Response> ADD_ORDER = ActionHandle.build(
            "orders:/add-order",       //No I18N
            AppModuleId.ORDERS,
            Request::new,
            Response::new,
            AddOrderAction::executeLocally);

    public static Mono<Response> executeLocally(Object localImpl, Request request) throws Exception {
        return ((OrdersClusterActions)localImpl).addOrder(request);
    }

    public static class Request extends ActionRequest<Response> {

        public final Streamable<Order, Request> order = Streamable.objectType("order", this, Order.class);       //No I18N

        Request() {
            super(ADD_ORDER, OrdersGateway.router());
        }

        @Override
        public void doReadFrom(MessageInStream messageInStream) throws IOException {
            order.readFrom(messageInStream);
        }

        @Override
        public void doWriteTo(MessageOutStream messageOutStream) throws IOException {
            order.writeTo(messageOutStream);
        }
    }

    public static class Response extends ActionResponse {

        public final Streamable<Boolean, Response> isUpdated = Streamable.boolType("isUpdated", this);       //No I18N

        Response() {
            super(ADD_ORDER, OrdersGateway.router());
        }

        @Override
        public void doReadFrom(MessageInStream messageInStream) throws IOException {
            isUpdated.readFrom(messageInStream);
        }

        @Override
        public void doWriteTo(MessageOutStream messageOutStream) throws IOException {
            isUpdated.writeTo(messageOutStream);
        }
    }
}
