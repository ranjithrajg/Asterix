package com.asterix.modcore.orders;


public enum OnProcessStatus {
    PROCESSING_ORDER("Processing Order"),
    READY_FOR_PICKUP("Ready for Pickup"),
    READY_FOR_DELIVERY("Ready for Delivery"),
    OUT_FOR_DELIVERY("Out for Delivery"),
    NOT_ABLE_TO_DELIVER("Not able to Deliver");

    private final String message;

    OnProcessStatus(String message) {
        this.message = message;
    }

    public static OnProcessStatus getValueOf(String statusStr) {
        if(statusStr != null) {
            for(OnProcessStatus status : OnProcessStatus.values()) {
                if(statusStr.equalsIgnoreCase(status.message())) {
                    return status;
                }
            }
            for(OnProcessStatus status : OnProcessStatus.values()) {
                if(statusStr.equalsIgnoreCase(status.name())) {
                    return status;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return message;
    }

    public String message() {
        return message;
    }
}
