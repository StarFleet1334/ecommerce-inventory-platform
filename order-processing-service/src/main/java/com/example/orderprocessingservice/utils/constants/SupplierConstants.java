package com.example.orderprocessingservice.utils.constants;

public final class SupplierConstants {

    private SupplierConstants() {}

    public static final int MAX_FIRST_NAME_LENGTH = 10;
    public static final int MAX_LAST_NAME_LENGTH = 10;
    public static final int MAX_EMAIL_LENGTH = 50;
    public static final int MAX_PHONE_NUMBER_LENGTH = 15;

    public static final int LATITUDE_PRECISION = 10;
    public static final int LATITUDE_SCALE = 8;
    public static final int LATITUDE_DIGITS_INTEGER = 2;
    public static final int LATITUDE_DIGITS_FRACTIONAL = 8;
    public static final String LATITUDE_MIN_DECIMAL = "-90.0";
    public static final String LATITUDE_MAX_DECIMAL = "90.0";

    public static final int LONGITUDE_PRECISION = 11;
    public static final int LONGITUDE_SCALE = 8;
    public static final int LONGITUDE_DIGITS_INTEGER = 3;
    public static final int LONGITUDE_DIGITS_FRACTIONAL = 8;
    public static final String LONGITUDE_MIN_DECIMAL = "-180.0";
    public static final String LONGITUDE_MAX_DECIMAL = "180.0";
}
