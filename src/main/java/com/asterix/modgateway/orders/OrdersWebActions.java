package com.asterix.modgateway.orders;

import com.asterix.modcore.orders.*;
import com.asterix.modgateway.orders.actions.*;
import com.atom.modgateway.federal.FederalGateway;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class OrdersWebActions {

    private static final Logger LOGGER = LoggerFactory.getLogger("Asterix");        //No I18N

    public static void registerWebRoutes() {
        FederalGateway.local().addGetRoute("/orders/view-orders", OrdersWebActions::getOrderList);
        FederalGateway.local().addGetRoute("/orders/view-orders/:order-id", OrdersWebActions::getOrderInfo);
        FederalGateway.local().addPostRoute("/orders/add-order", OrdersWebActions::addOrder);
        FederalGateway.local().addPostRoute("/orders/share-bill/:order-id", OrdersWebActions::shareBill);
        FederalGateway.local().addPostRoute("/orders/update-order/state/:order-id", OrdersWebActions::updateOrderState);
        FederalGateway.local().addPostRoute("/orders/update-order/on-process-status/:order-id", OrdersWebActions::updateOrderOnProcessStatus);
        FederalGateway.local().addPostRoute("/orders/update-order/bill-view/:order-id", OrdersWebActions::updateOrderBillView);
        FederalGateway.local().addPostRoute("/orders/update-order/bill/:order-id", OrdersWebActions::updateOrderBill);
        FederalGateway.local().addPostRoute("/orders/update-order/my-notes/:order-id", OrdersWebActions::updateOrderMyNotes);
        FederalGateway.local().addPostRoute("/orders/update-order/payment-status/:order-id", OrdersWebActions::updateOrderPaymentStatus);
    }

    public static void getOrderList(RoutingContext context) {
        JsonObject responseObj = new JsonObject();
        try {
            long fromTime = Long.parseLong(context.request().getParam("fromTime"));
            long toTime = Long.parseLong(context.request().getParam("toTime"));

            ViewOrdersAction.VIEW_ORDERS.buildRequest()
                    .fromTime.set(new Date(fromTime))
                    .toTime.set(new Date(toTime))
                    .executeOnCluster()
                    .subscribe(
                            response -> {
                                List<Order> orderList = response.orderList.value();
                                JsonArray orderJList = new JsonArray();
                                for(Order order : orderList) {
                                    orderJList.add(order.getAsJson());
                                }
                                responseObj.put("orderList", orderJList);
                                HttpServerResponse webResponse = context.response();
                                webResponse.end(responseObj.toString());
                            },
                            throwable -> {
                                throwable.printStackTrace();
                                sendErrorResponse(context, "Error occurred while processing request : " + throwable.getLocalizedMessage());
                            }
                    );
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(context, "Error occurred while processing request : " + e.getLocalizedMessage());
        }
    }

    public static void getOrderInfo(RoutingContext context) {
        JsonObject responseObj = new JsonObject();
        try {
            long orderId = Long.parseLong(context.pathParam("order-id"));
            ViewOrderAction.VIEW_ORDER.buildRequest()
                    .orderId.set(orderId)
                    .executeOnCluster()
                    .subscribe(
                            response -> {
                                Order order = response.orderInfo.get();
                                responseObj.put("orderInfo", order.getAsJson());
                                HttpServerResponse webResponse = context.response();
                                webResponse.end(responseObj.toString());
                            },
                            throwable -> {
                                throwable.printStackTrace();
                                sendErrorResponse(context, "Error occurred while processing request : " + throwable.getLocalizedMessage());
                            }
                    );
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(context, "Error occurred while processing request : " + e.getLocalizedMessage());
        }
    }

    public static void addOrder(RoutingContext context) {
        JsonObject responseObj = new JsonObject();
        try {
            JsonObject orderObj = new JsonObject(context.request().getParam("order"));
            Order order = new Order()
                    .setPaymentType(PaymentType.getValueOf(orderObj.getString(Order.PAYMENT_TYPE_KEY)))
                    .setDeliveryType(DeliveryType.getValueOf(orderObj.getString(Order.DELIVERY_TYPE_KEY)))
                    .setOnProcessStatus(OnProcessStatus.getValueOf(orderObj.getString(Order.ON_PROCESS_STATUS_KEY)))
                    .setInstructions(orderObj.getString(Order.INSTRUCTIONS_KEY));

            AddOrderAction.ADD_ORDER.buildRequest()
                    .order.set(order)
                    .executeOnCluster()
                    .subscribe(
                            response -> {
                                boolean isUpdated = response.isUpdated.get();
                                if(!isUpdated) {
                                    responseObj.put("failureReason", response.responseStatus());
                                }
                                responseObj.put("isUpdated", isUpdated);
                                HttpServerResponse webResponse = context.response();
                                webResponse.end(responseObj.toString());
                            },
                            throwable -> {
                                throwable.printStackTrace();
                                sendErrorResponse(context, "Error occurred while processing request : " + throwable.getLocalizedMessage());
                            }
                    );
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(context, "Error occurred while processing request : " + e.getLocalizedMessage());
        }
    }

    public static void shareBill(RoutingContext context) {
    }

    public static void updateOrderState(RoutingContext context) {
        JsonObject responseObj = new JsonObject();
        try {
            long orderId = Long.parseLong(context.pathParam("order-id"));
            OrderState state = OrderState.getValueOf(context.request().getParam("orderState"));

            UpdateOrderStateAction.UPDATE_ORDER_STATE.buildRequest()
                    .orderId.set(orderId)
                    .state.set(state)
                    .executeOnCluster()
                    .subscribe(
                            response -> {
                                boolean isUpdated = response.isUpdated.get();
                                if(!isUpdated) {
                                    responseObj.put("failureReason", response.responseStatus());
                                }
                                responseObj.put("isUpdated", isUpdated);
                                HttpServerResponse webResponse = context.response();
                                webResponse.end(responseObj.toString());
                            },
                            throwable -> {
                                throwable.printStackTrace();
                                sendErrorResponse(context, "Error occurred while processing request : " + throwable.getLocalizedMessage());
                            }
                    );
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(context, "Error occurred while processing request : " + e.getLocalizedMessage());
        }
    }

    public static void updateOrderOnProcessStatus(RoutingContext context) {
        JsonObject responseObj = new JsonObject();
        try {
            long orderId = Long.parseLong(context.pathParam("order-id"));
            OnProcessStatus onProcessStatus = OnProcessStatus.getValueOf(context.request().getParam("onProcessStatus"));

            UpdateOrderOnProcessStatusAction.UPDATE_ORDER_ON_PROCESS_STATUS.buildRequest()
                    .orderId.set(orderId)
                    .onProcessStatus.set(onProcessStatus)
                    .executeOnCluster()
                    .subscribe(
                            response -> {
                                boolean isUpdated = response.isUpdated.get();
                                if(!isUpdated) {
                                    responseObj.put("failureReason", response.responseStatus());
                                }
                                responseObj.put("isUpdated", isUpdated);
                                HttpServerResponse webResponse = context.response();
                                webResponse.end(responseObj.toString());
                            },
                            throwable -> {
                                throwable.printStackTrace();
                                sendErrorResponse(context, "Error occurred while processing request : " + throwable.getLocalizedMessage());
                            }
                    );
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(context, "Error occurred while processing request : " + e.getLocalizedMessage());
        }
    }

    public static void updateOrderBillView(RoutingContext context) {
    }

    public static void updateOrderBill(RoutingContext context) {
    }

    public static void updateOrderMyNotes(RoutingContext context) {
        JsonObject responseObj = new JsonObject();
        try {
            long orderId = Long.parseLong(context.pathParam("order-id"));
            String myNotes = context.request().getParam("myNotes");

            UpdateOrderMyNotesAction.UPDATE_ORDER_MY_NOTES.buildRequest()
                    .orderId.set(orderId)
                    .myNotes.set(myNotes)
                    .executeOnCluster()
                    .subscribe(
                            response -> {
                                boolean isUpdated = response.isUpdated.get();
                                if(!isUpdated) {
                                    responseObj.put("failureReason", response.responseStatus());
                                }
                                responseObj.put("isUpdated", isUpdated);
                                HttpServerResponse webResponse = context.response();
                                webResponse.end(responseObj.toString());
                            },
                            throwable -> {
                                throwable.printStackTrace();
                                sendErrorResponse(context, "Error occurred while processing request : " + throwable.getLocalizedMessage());
                            }
                    );
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(context, "Error occurred while processing request : " + e.getLocalizedMessage());
        }
    }

    public static void updateOrderPaymentStatus(RoutingContext context) {
        JsonObject responseObj = new JsonObject();
        try {
            long orderId = Long.parseLong(context.pathParam("order-id"));
            PaymentStatus paymentStatus = PaymentStatus.getValueOf(context.request().getParam("paymentStatus"));

            UpdateOrderPaymentStatusAction.UPDATE_ORDER_PAYMENT_STATUS.buildRequest()
                    .orderId.set(orderId)
                    .paymentStatus.set(paymentStatus)
                    .executeOnCluster()
                    .subscribe(
                            response -> {
                                boolean isUpdated = response.isUpdated.get();
                                if(!isUpdated) {
                                    responseObj.put("failureReason", response.responseStatus());
                                }
                                responseObj.put("isUpdated", isUpdated);
                                HttpServerResponse webResponse = context.response();
                                webResponse.end(responseObj.toString());
                            },
                            throwable -> {
                                throwable.printStackTrace();
                                sendErrorResponse(context, "Error occurred while processing request : " + throwable.getLocalizedMessage());
                            }
                    );
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(context, "Error occurred while processing request : " + e.getLocalizedMessage());
        }
    }

    private static void sendErrorResponse(RoutingContext context, String errMsg) {
        HttpServerResponse webResponse = context.response();
        webResponse.setStatusCode(500);
        webResponse.setStatusMessage(errMsg);
        webResponse.end(errMsg);
    }
}
