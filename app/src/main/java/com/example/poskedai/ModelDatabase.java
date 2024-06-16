package com.example.poskedai;

public class ModelDatabase {
    private String menu_type;
    private String menu_name;
    private int menu_price;
    private String menu_remarks;
    private String key;

    public ModelDatabase() {
        // Default constructor required for calls to DataSnapshot.getValue(ModelDatabase.class)
    }

    public ModelDatabase(String menu_type, String menu_name, String price, String menu_remarks) {
        this.menu_type = menu_type;
        this.menu_name = menu_name;
        this.menu_remarks = menu_remarks;
        try {
            this.menu_price = Integer.parseInt(price);
        } catch (NumberFormatException e) {
            this.menu_price = 0;
        }
    }

    // Getter and Setter for key
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    // Getter and Setter for menu_type
    public String getMenu_type() {
        return menu_type;
    }
    public void setMenu_type(String menu_type) {
        this.menu_type = menu_type;
    }

    // Getter and Setter for menu_name
    public String getMenu_name() {
        return menu_name;
    }
    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    // Getter and Setter for menu_price
    public int getMenu_price() {
        return menu_price;
    }
    public void setMenu_price(int menu_price) {
        this.menu_price = menu_price;
    }

    // Getter and Setter for menu_remarks
    public String getMenu_remarks() {
        return menu_remarks;
    }
    public void setMenu_remarks(String menu_remarks) {
        this.menu_remarks = menu_remarks;
    }
}
