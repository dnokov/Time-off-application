package com.learning.timeOffManagement.model;

public enum TimeOffType {
    PAID("paid leave"),
    UNPAID("unpaid leave"),
    SICK_LEAVE("sick leave");

    public final String label;

    private TimeOffType(String label) {
        this.label = label;
    }

    @Override
    public String toString() {

        return label;
    }
}
