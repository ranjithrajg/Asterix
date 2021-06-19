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
import java.util.Date;
import java.util.List;

public class ViewOrdersAction {

    public static ActionHandle<Request, Response> VIEW_ORDERS = ActionHandle.build(
            "orders:/view-orders",       //No I18N
            AppModuleId.ORDERS,
            Request::new,
            Response::new,
            ViewOrdersAction::executeLocally);

    public static Mono<Response> executeLocally(Object localImpl, Request request) throws Exception {
        return ((OrdersClusterActions)localImpl).getOrderList(request);
    }

    public static class Request extends ActionRequest<Response> {

        public final Streamable<Date, Request> fromTime = Streamable.dateType("fromTime", this);       //No I18N
        public final Streamable<Date, Request> toTime = Streamable.dateType("toTime", this);       //No I18N

        Request() {
            super(VIEW_ORDERS, OrdersGateway.router());
        }

        @Override
        public void doReadFrom(MessageInStream inStream) throws IOException {
            fromTime.readFrom(inStream);
            toTime.readFrom(inStream);
        }

        @Override
        public void doWriteTo(MessageOutStream outStream) throws IOException {
            fromTime.writeTo(outStream);
            toTime.writeTo(outStream);
        }

        @Override
        public String toString() {
            return super.toString() +
                    ", fromTime:\"" + fromTime.value() + "\"" +
                    ", toTime:\"" + toTime.value() + "\"";       //No I18N
        }
    }

    public static class Response extends ActionResponse {

        public final Streamable<List<Order>, Response> orderList = Streamable.objectArrType("orderList", this, Order.class);       //No I18N

        Response() {
            super(VIEW_ORDERS, OrdersGateway.router());
        }

        @Override
        public void doReadFrom(MessageInStream inStream) throws IOException {
            orderList.readFrom(inStream);
        }

        @Override
        public void doWriteTo(MessageOutStream outStream) throws IOException {
            orderList.writeTo(outStream);
        }

        @Override
        public String toString() {
            return super.toString() +
                    ", orderList:\"" + orderList.value() + "\"";       //No I18N
        }
    }
}
