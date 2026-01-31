package com.infragest.infra_devices_service.util;

public abstract class MessageException {

    public static final String DEVICE_NOT_FOUND_BY_ID = "Device with ID %s not found.";
    public static final String DEVICE_NOT_FOUND_BY_BARCODE = "Device with barcode %s not found.";
    public static final String DEVICE_BARCODE_ALREADY_EXISTS = "A device with barcode %s already exists.";
    public static final String DEVICE_ERROR_SAVING = "Error saving device.";
    public static final String DEVICE_ALREADY_EXISTS = "A device %s already exists.";
    public static final String DEVICE_IDS_CANNOT_BE_EMPTY = "The list of IDs cannot be empty.";
    public static final String DEVICE_NOT_FOUND_BY_IDS = "The following devices were not found: %s.";
    public static final String DEVICE_ERROR_UPDATING_STATES = "An error occurred while updating the states of devices.";

    // Mensajes específicos para DeviceAssignment
    public static final String DEVICE_ALREADY_ASSIGNED = "The device %s is already assigned to another order.";
    public static final String DEVICE_NOT_AVAILABLE_FOR_ASSIGNMENT = "The device %s is not in a state that allows assignment.";
    public static final String DEVICE_ASSIGNMENT_TO_ORDER_NOT_FOUND = "No active assignment found for the device %s in order %s.";
    public static final String DEVICE_ASSIGNMENT_NOT_FOUND = "No active assignment found for the device %s";
    public static final String DEVICE_ASSIGNMENT_ORDER_MISMATCH = "The active assignment does not belong to the order %s.";

    // Mensajes específicos para IDs de órdenes
    public static final String ORDER_ID_CANNOT_BE_NULL_OR_EMPTY = "The order ID cannot be null or empty.";

    private MessageException() {
    }
}
