package com.example.poskedai;

import java.text.NumberFormat;
import java.util.Locale;

public class ModelDatabase {
    private String id_menu;
    private String menu_type;
    private String menu_name;
    private int menu_price;
    private String menu_remarks;
    private String imageUrl;
    private String key;
    private int qty;

    public ModelDatabase() {
        // Default constructor required for calls to DataSnapshot.getValue(ModelDatabase.class)
    }

    // Constructor
    public ModelDatabase(String id_menu, String menu_type, String menu_name, String price, String menu_remarks, String imageUrl) {
        this.id_menu = id_menu;
        this.menu_type = menu_type;
        this.menu_name = menu_name;
        this.menu_remarks = menu_remarks;
        this.imageUrl = imageUrl;
        try {
            this.menu_price = Integer.parseInt(price);
        } catch (NumberFormatException e) {
            this.menu_price = 0;
        }
        this.qty = 0;
    }

    // Getter and Setter for id_menu
    public String getId_menu() {
        return id_menu;
    }
    public void setId_menu(String id_menu) {
        this.id_menu = id_menu;
    }

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
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

    public int getQty() {
        return qty;
    }
    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getFormattedMenuPrice() {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.GERMAN);
        return numberFormat.format(menu_price);
    }


}
