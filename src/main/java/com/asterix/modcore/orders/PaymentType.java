package com.asterix.modcore.orders;

public enum PaymentType {
    COD,
    DEBIT_CARD,
    CREDIT_CARD,
    GATEWAY,
    PAYTM,
    UPI;

    public static PaymentType getValueOf(String typeStr) {
        if(typeStr != null) {
            for(PaymentType type : PaymentType.values()) {
                if(typeStr.equalsIgnoreCase(type.toString())) {
                    return type;
                }
            }
        }
        return null;
    }
}
