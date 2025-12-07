package com.domus.enrollments.valueObjects;

/**
 * Enumeration representing the state of an enrollment.
 */
public enum EnrollmentState {
    CREATED,
    PENDING_DOCUMENTS,
    PROCESSING,
    ENROLLED,
    NULLIFIED,
    ERROR
}
