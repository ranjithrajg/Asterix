package com.asterix.commons.modz.store;

public enum UnitType {
    NONE("NAN"),
    KILOGRAM("kg"),
    GRAM("g"),
    MILLIGRAM("mg"),
    METER("m"),
    CENTIMETER("cm"),
    MILLIMETER("mm"),
    LITER("L");

    private String symbol;

    UnitType(String symbol) {
        this.symbol = symbol;
    }

    public static UnitType getTypeFor(String typeStr) {
        if(typeStr != null) {
            for(UnitType type : UnitType.values()) {
                if(typeStr.equalsIgnoreCase(type.toString())) {
                    return type;
                }
            }
        }
        return null;
    }
}
