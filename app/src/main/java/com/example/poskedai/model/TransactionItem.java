package com.example.poskedai.model;

public class TransactionItem {
    private String id_transaction;
    private String id_transaction_item;
    private String transaction_date;
    private String transaction_time;
    private String id_menu;
    private String menu_type;
    private String menu_name;
    private int menu_price;
    private String menu_remarks;
    private String imageUrl;
    private int order_qty;
    private int total_price;

    // Constructor kosong diperlukan untuk Firebase
    public TransactionItem() {
    }


    public TransactionItem(String id_transaction,String id_transaction_item, String transaction_date, String transaction_time, String id_menu, String menu_type, String menu_name, int menu_price, String menu_remarks, String imageUrl, int order_qty, int total_price) {
        this.id_transaction = id_transaction;
        this.id_transaction_item = id_transaction_item;
        this.transaction_date = transaction_date;
        this.transaction_time = transaction_time;
        this.id_menu = id_menu;
        this.menu_type = menu_type;
        this.menu_name = menu_name;
        this.menu_price = menu_price;
        this.menu_remarks = menu_remarks;
        this.imageUrl = imageUrl;
        this.order_qty = order_qty;
        this.total_price = total_price;
    }


    public String getId_transaction() {
        return id_transaction;
    }

    public void setId_transaction(String id_transaction) {
        this.id_transaction = id_transaction;
    }

    public String getId_transaction_item() {
        return id_transaction_item;
    }
    public void setId_transaction_item(String id_transaction_item) {
        this.id_transaction_item = id_transaction_item;
    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }

    public String getTransaction_time() {
        return transaction_time;
    }

    public void setTransaction_time(String transaction_time) {
        this.transaction_time = transaction_time;
    }

    public String getId_menu() {
        return id_menu;
    }

    public void setId_menu(String id_menu) {
        this.id_menu = id_menu;
    }

    public String getMenu_type() {
        return menu_type;
    }

    public void setMenu_type(String menu_type) {
        this.menu_type = menu_type;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public int getMenu_price() {
        return menu_price;
    }

    public void setMenu_price(int menu_price) {
        this.menu_price = menu_price;
    }

    public String getMenu_remarks() {
        return menu_remarks;
    }

    public void setMenu_remarks(String menu_remarks) {
        this.menu_remarks = menu_remarks;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getOrder_qty() {
        return order_qty;
    }

    public void setOrder_qty(int order_qty) {
        this.order_qty = order_qty;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

}