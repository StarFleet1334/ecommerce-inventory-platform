package com.example.orderprocessingservice.utils.constants;

public final class WareHouseConstants {

    private WareHouseConstants() {}

    public static final int WAREHOUSE_NAME_LENGTH = 20;

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
