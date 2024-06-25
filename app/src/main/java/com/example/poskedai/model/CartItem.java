package com.example.poskedai.model;

public class CartItem {
    private String id_menu;
    private int menu_price;
    private int quantity;
    private int total_price;

    public CartItem() {
        // Default constructor required for calls to DataSnapshot.getValue(CartItem.class)
    }

    public CartItem(String id_menu, int menu_price, int quantity, int total_price) {
        this.id_menu = id_menu;
        this.menu_price = menu_price;
        this.quantity = quantity;
        this.total_price = total_price;
    }

    public String getId_menu() {
        return id_menu;
    }

    public void setId_menu(String id_menu) {
        this.id_menu = id_menu;
    }

    public int getMenu_price() {
        return menu_price;
    }

    public void setMenu_price(int menu_price) {
        this.menu_price = menu_price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }
}
