package com.example.poskedai.model;

public class ModelOrder {
    private String id_order;
    private String id_menu;
    private String order_date;
    private String order_time;
    private int total_qty;
    private int total_price;

    // Default constructor required for calls to DataSnapshot.getValue(ModelOrder.class)
    public ModelOrder() {}

    public ModelOrder(String id_order, String id_menu, String order_date, String order_time, int total_qty, int total_price) {
        this.id_order = id_order;
        this.id_menu = id_menu;
        this.order_date = order_date;
        this.order_time = order_time;
        this.total_qty = total_qty;
        this.total_price = total_price;
    }

    // Getters and setters
    public String getId_order() {
        return id_order;
    }

    public void setId_order(String id_order) {
        this.id_order = id_order;
    }

    public String getId_menu() {
        return id_menu;
    }

    public void setId_menu(String id_menu) {
        this.id_menu = id_menu;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public int getTotal_qty() {
        return total_qty;
    }

    public void setTotal_qty(int total_qty) {
        this.total_qty = total_qty;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }
}
