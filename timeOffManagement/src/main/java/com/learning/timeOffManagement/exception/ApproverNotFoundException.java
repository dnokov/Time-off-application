package com.learning.timeOffManagement.exception;

public class ApproverNotFoundException extends RuntimeException {

    private static final String APPROVER_NOT_FOUND_TEMPLATE = "%s you are not an approver.";

    public ApproverNotFoundException(String name) {
        super(String.format(APPROVER_NOT_FOUND_TEMPLATE, name));
    }
}
