package com.asterix.modcore.orders;

import java.util.EnumSet;

public enum OrderState {
    NEW,
    ON_PROCESS,
    CANCELLED,
    DELIVERED;

    static {
        NEW.nextStates = EnumSet.of(ON_PROCESS, CANCELLED);
        ON_PROCESS.nextStates = EnumSet.of(DELIVERED, CANCELLED);
        CANCELLED.nextStates = EnumSet.of(ON_PROCESS);
        DELIVERED.nextStates = EnumSet.noneOf(OrderState.class);
    }

    EnumSet<OrderState> nextStates = null;

    public boolean canMoveToState(OrderState nextState, boolean throwException) {
        if(nextStates.contains(nextState)) {
            return true;
        } else if(throwException) {
            throw new IllegalStateException("Can't move from " + this + " to " + nextState + "!");//NO I18N
        } else {
            return false;
        }
    }

    public OrderState moveToState(OrderState nextState) {
        if(!nextStates.contains(nextState)) {
            throw new IllegalStateException("Can't move from " + this + " to " + nextState + "!");//NO I18N
        }
        return nextState;
    }

    public static OrderState getValueOf(String stateStr) {
        if(stateStr != null) {
            for(OrderState state : OrderState.values()) {
                if(stateStr.equalsIgnoreCase(state.name())) {
                    return state;
                }
            }
        }
        return null;
    }
}
