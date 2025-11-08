package com.infragest.infra_devices_service.util;

public abstract class MessageException {
    public static final String DEVICE_NOT_FOUND_BY_ID = "Device with ID %s not found.";
    public static final String DEVICE_NOT_FOUND_BY_BARCODE = "Device with barcode %s not found.";
    public static final String DEVICE_BARCODE_ALREADY_EXISTS = "A device with barcode %s already exists.";
    public static final String DEVICE_ERROR_SAVING = "Error saving device.";
    public static final String DEVICE_ALREADY_EXISTS = "A device %s already exists.";

    private MessageException() {
    }
}
