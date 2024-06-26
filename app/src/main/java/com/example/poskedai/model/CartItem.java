package com.example.poskedai.model;

public class CartItem {
    private String id_menu;
    private String menu_name;
    private String menu_remarks;
    private int menu_price, total_price;
    private int qty;

    private String imageUrl;


    public CartItem() {
    }

    public CartItem(String id_menu, String menu_name, int menu_price, int qty, int total_price, String imageUrl) {
        this.id_menu = id_menu;
        this.menu_name = menu_name;
        this.menu_price = menu_price;
        this.qty = qty;
        this.total_price = total_price;
        this.imageUrl = imageUrl;
    }

    public String getId_menu() {
        return id_menu;
    }

    public void setId_menu(String id_menu) {
        this.id_menu = id_menu;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getMenu_remarks() {
        return menu_remarks;
    }

    public void setMenu_remarks(String menu_remarks) {
        this.menu_remarks = menu_remarks;
    }

    public int getMenu_price() {
        return menu_price;
    }

    public void setMenu_price(int menu_price) {
        this.menu_price = menu_price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getTotal_price() {
        return menu_price;
    }

    public void setTotal_price(int menu_price) {
        this.menu_price = menu_price;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
