package com.asterix.modcore.orders;

public enum DeliveryType {
    PICKUP,
    DELIVERY;

    public static DeliveryType getValueOf(String typeStr) {
        if(typeStr != null) {
            for(DeliveryType type : DeliveryType.values()) {
                if(typeStr.equalsIgnoreCase(type.name())) {
                    return type;
                }
            }
        }
        return null;
    }
}
