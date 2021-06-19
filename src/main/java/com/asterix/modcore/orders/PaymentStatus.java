package com.asterix.modcore.orders;

public enum PaymentStatus {
    PAID,
    PAYMENT_PENDING,
    REFUND_PENDING,
    UNKNOWN;

    public static PaymentStatus getValueOf(String statusStr) {
        if(statusStr != null) {
            for(PaymentStatus status : PaymentStatus.values()) {
                if(statusStr.equalsIgnoreCase(status.name())) {
                    return status;
                }
            }
        }
        return null;
    }
}
