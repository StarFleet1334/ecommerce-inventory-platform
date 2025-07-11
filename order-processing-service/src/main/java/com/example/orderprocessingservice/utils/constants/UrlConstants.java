package com.example.orderprocessingservice.utils.constants;

public class UrlConstants {
    private static final String BASE_URL = "http://inventory-service:8081/api/v1/";

    public static final String WAREHOUSE_POST_ENDPOINT = BASE_URL + "warehouse";
    public static final String PRODUCT_POST_ENDPOINT = BASE_URL + "product";
    public static final String CUSTOMER_POST_ENDPOINT = BASE_URL + "customer";
    public static final String SUPPLIER_POST_ENDPOINT = BASE_URL + "supplier";
    public static final String EMPLOYEE_POST_ENDPOINT = BASE_URL + "employee";
    public static final String STOCK_POST_ENDPOINT = BASE_URL + "stock";
}
