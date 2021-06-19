package com.asterix.modcore.orders;

import com.atom.commons.io.MessageInStream;
import com.atom.commons.io.MessageOutStream;
import com.atom.commons.io.StreamableObject;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;

public class Order implements StreamableObject {

    private static final Logger LOGGER = LoggerFactory.getLogger("Asterix");        //No I18N

    public static final String ORDER_ID_KEY = "orderId";
    public static final String CREATION_TIME_KEY = "creationTime";
    public static final String DELIVERED_TIME_KEY = "deliveredTime";
    public static final String ELAPSED_TIME_KEY = "elapsedTime";
    public static final String TOTAL_AMOUNT_KEY = "totalAmount";
    public static final String TOTAL_ITEMS_KEY = "totalItems";
    public static final String STATE_KEY = "state";
    public static final String PAYMENT_TYPE_KEY = "paymentType";
    public static final String DELIVERY_TYPE_KEY = "deliveryType";
    public static final String IS_MODIFIED_KEY = "isModified";
    public static final String LAST_MODIFIED_TIME_KEY = "lastModifiedTime";
    public static final String ON_PROCESS_STATUS_KEY = "onProcessStatus";
    public static final String PAYMENT_STATUS_KEY = "paymentStatus";
    public static final String CANCELLED_REASON_KEY = "cancelledReason";
    public static final String INSTRUCTIONS_KEY = "instructions";
    public static final String MY_NOTES_KEY = "myNotes";

    private long orderId = -1L;
    private long customerId = -1L;
    private Date creationTime = new Date(0);
    private Date deliveredTime = new Date(0);
    private long elapsedTime = -1L;
    private float totalAmount = 0;
    private int totalItems = 0;
    private OrderState state = OrderState.NEW;
    private PaymentType paymentType = PaymentType.COD;
    private DeliveryType deliveryType = DeliveryType.PICKUP;
    private boolean isModified = false;
    private Date lastModifiedTime = new Date(0);
    private OnProcessStatus onProcessStatus = OnProcessStatus.PROCESSING_ORDER;
    private PaymentStatus paymentStatus = PaymentStatus.UNKNOWN;
    private String cancelledReason = "";
    private String instructions = "";
    private String myNotes = "";

    public Order() {
    }

    public Order(long orderId, OrderState state) {
        this.orderId = orderId;
        this.state = state;
    }

    public long getOrderId() {
        return orderId;
    }

    public Order setOrderId(long orderId) {
        this.orderId = orderId;
        return this;
    }

    public long getCustomerId() {
        return customerId;
    }

    public Order setCustomerId(long customerId) {
        this.customerId = customerId;
        return this;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public Order setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
        return this;
    }

    public Date getDeliveredTime() {
        return deliveredTime;
    }

    public Order setDeliveredTime(Date deliveredTime) {
        this.deliveredTime = deliveredTime;
        return this;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public Order setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
        return this;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public Order setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public Order setTotalItems(int totalItems) {
        this.totalItems = totalItems;
        return this;
    }

    public OrderState getState() {
        return state;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public Order setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
        return this;
    }

    public DeliveryType getDeliveryType() {
        return deliveryType;
    }

    public Order setDeliveryType(DeliveryType deliveryType) {
        this.deliveryType = deliveryType;
        return this;
    }

    public boolean isModified() {
        return isModified;
    }

    public Order setModified(boolean modified) {
        isModified = modified;
        return this;
    }

    public Date getLastModifiedTime() {
        return lastModifiedTime;
    }

    public Order setLastModifiedTime(Date lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
        return this;
    }

    public OnProcessStatus getOnProcessStatus() {
        return onProcessStatus;
    }

    public Order setOnProcessStatus(OnProcessStatus onProcessStatus) {
        this.onProcessStatus = onProcessStatus;
        return this;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public Order setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
        return this;
    }

    public String getCancelledReason() {
        return cancelledReason;
    }

    public Order setCancelledReason(String cancelledReason) {
        this.cancelledReason = cancelledReason;
        return this;
    }

    public String getInstructions() {
        return instructions;
    }

    public Order setInstructions(String instructions) {
        this.instructions = instructions;
        return this;
    }

    public String getMyNotes() {
        return myNotes;
    }

    public Order setMyNotes(String myNotes) {
        this.myNotes = myNotes;
        return this;
    }

    public void changeState(OrderState newState) {
        state = state.moveToState(newState);
    }

    public void editMyNotes(String newNotes) {
        myNotes = newNotes;
    }

    public JsonObject getAsJson() {
        return new JsonObject()
                .put(ORDER_ID_KEY, orderId)
                .put(CREATION_TIME_KEY, creationTime)
                .put(DELIVERED_TIME_KEY, deliveredTime)
                .put(ELAPSED_TIME_KEY, elapsedTime)
                .put(TOTAL_AMOUNT_KEY, totalAmount)
                .put(TOTAL_ITEMS_KEY, totalItems)
                .put(STATE_KEY, state)
                .put(PAYMENT_TYPE_KEY, paymentType)
                .put(DELIVERY_TYPE_KEY, deliveryType)
                .put(IS_MODIFIED_KEY, isModified)
                .put(LAST_MODIFIED_TIME_KEY, lastModifiedTime)
                .put(ON_PROCESS_STATUS_KEY, onProcessStatus)
                .put(PAYMENT_STATUS_KEY, paymentStatus)
                .put(CANCELLED_REASON_KEY, cancelledReason)
                .put(INSTRUCTIONS_KEY, instructions)
                .put(MY_NOTES_KEY, myNotes);
    }

    public void log() {
        LOGGER.info(
                "[" + ORDER_ID_KEY + " : " + orderId + "] "
                + "[" + CREATION_TIME_KEY + " : " + creationTime + "] "
                + "[" + DELIVERED_TIME_KEY + " : " + deliveredTime + "] "
                + "[" + ELAPSED_TIME_KEY + " : " + elapsedTime + "] "
                + "[" + TOTAL_AMOUNT_KEY + " : " + totalAmount + "] "
                + "[" + TOTAL_ITEMS_KEY + " : " + totalItems + "] "
                + "[" + STATE_KEY + " : " + state + "] "
                + "[" + PAYMENT_TYPE_KEY + " : " + paymentType + "] "
                + "[" + DELIVERY_TYPE_KEY + " : " + deliveryType + "] "
                + "[" + IS_MODIFIED_KEY + " : " + isModified + "] "
                + "[" + LAST_MODIFIED_TIME_KEY + " : " + lastModifiedTime + "] "
                + "[" + ON_PROCESS_STATUS_KEY + " : " + onProcessStatus + "] "
                + "[" + PAYMENT_STATUS_KEY + " : " + paymentStatus + "] "
                + "[" + CANCELLED_REASON_KEY + " : " + cancelledReason + "] "
                + "[" + INSTRUCTIONS_KEY + " : " + instructions + "] "
                + "[" + MY_NOTES_KEY + " : " + myNotes + "] ");
    }

    @Override
    public void readFrom(MessageInStream messageInStream) throws IOException {
        orderId = messageInStream.readLong(ORDER_ID_KEY);
        creationTime = messageInStream.readDate(CREATION_TIME_KEY);
        deliveredTime = messageInStream.readDate(DELIVERED_TIME_KEY);
        elapsedTime = messageInStream.readLong(ELAPSED_TIME_KEY);
        totalAmount = messageInStream.readFloat(TOTAL_AMOUNT_KEY);
        totalItems = messageInStream.readInt(TOTAL_ITEMS_KEY);
        state = messageInStream.readEnum(STATE_KEY, OrderState.class);
        paymentType = messageInStream.readEnum(PAYMENT_TYPE_KEY, PaymentType.class);
        deliveryType = messageInStream.readEnum(DELIVERY_TYPE_KEY, DeliveryType.class);
        isModified = messageInStream.readBoolean(IS_MODIFIED_KEY);
        lastModifiedTime = messageInStream.readDate(LAST_MODIFIED_TIME_KEY);
        onProcessStatus = messageInStream.readEnum(ON_PROCESS_STATUS_KEY, OnProcessStatus.class);
        paymentStatus = messageInStream.readEnum(PAYMENT_STATUS_KEY, PaymentStatus.class);
        cancelledReason = messageInStream.readString(CANCELLED_REASON_KEY);
        instructions = messageInStream.readString(INSTRUCTIONS_KEY);
        myNotes = messageInStream.readString(MY_NOTES_KEY);
    }

    @Override
    public void writeTo(MessageOutStream messageOutStream) throws IOException {
        messageOutStream.writeLong(ORDER_ID_KEY, orderId);
        messageOutStream.writeDate(CREATION_TIME_KEY, creationTime);
        messageOutStream.writeDate(DELIVERED_TIME_KEY, deliveredTime);
        messageOutStream.writeLong(ELAPSED_TIME_KEY, elapsedTime);
        messageOutStream.writeFloat(TOTAL_AMOUNT_KEY, totalAmount);
        messageOutStream.writeInt(TOTAL_ITEMS_KEY, totalItems);
        messageOutStream.writeEnum(STATE_KEY, state);
        messageOutStream.writeEnum(PAYMENT_TYPE_KEY, paymentType);
        messageOutStream.writeEnum(DELIVERY_TYPE_KEY, deliveryType);
        messageOutStream.writeBoolean(IS_MODIFIED_KEY, isModified);
        messageOutStream.writeDate(LAST_MODIFIED_TIME_KEY, lastModifiedTime);
        messageOutStream.writeEnum(ON_PROCESS_STATUS_KEY, onProcessStatus);
        messageOutStream.writeEnum(PAYMENT_STATUS_KEY, paymentStatus);
        messageOutStream.writeString(CANCELLED_REASON_KEY, cancelledReason);
        messageOutStream.writeString(INSTRUCTIONS_KEY, instructions);
        messageOutStream.writeString(MY_NOTES_KEY, myNotes);
    }
}
