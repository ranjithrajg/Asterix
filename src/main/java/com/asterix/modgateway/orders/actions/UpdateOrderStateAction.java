package com.asterix.modgateway.orders.actions;

import com.asterix.modcore.orders.OrderState;
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

public class UpdateOrderStateAction {

    public static ActionHandle<Request, Response> UPDATE_ORDER_STATE = ActionHandle.build(
            "orders:/update-order/state",       //No I18N
            AppModuleId.ORDERS,
            Request::new,
            Response::new,
            UpdateOrderStateAction::executeLocally);

    public static Mono<Response> executeLocally(Object localImpl, Request request) throws Exception {
        return ((OrdersClusterActions)localImpl).updateOrderState(request);
    }

    public static class Request extends ActionRequest<Response> {

        public final Streamable<Long, Request> orderId = Streamable.longType("orderId", this);       //No I18N
        public final Streamable<OrderState, Request> state = Streamable.enumType("state", this, OrderState.class);       //No I18N

        Request() {
            super(UPDATE_ORDER_STATE, OrdersGateway.router());
        }

        @Override
        public void doReadFrom(MessageInStream messageInStream) throws IOException {
            orderId.readFrom(messageInStream);
            state.readFrom(messageInStream);
        }

        @Override
        public void doWriteTo(MessageOutStream messageOutStream) throws IOException {
            orderId.writeTo(messageOutStream);
            state.writeTo(messageOutStream);
        }
    }

    public static class Response extends ActionResponse {

        public final Streamable<Boolean, Response> isUpdated = Streamable.boolType("isUpdated", this);       //No I18N

        Response() {
            super(UPDATE_ORDER_STATE, OrdersGateway.router());
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
