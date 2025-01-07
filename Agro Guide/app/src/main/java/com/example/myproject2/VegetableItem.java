package com.example.myproject2;
public class VegetableItem {
    private String itemName;
    private String district;
    private String date;
    private String price;

    // Default constructor required for Firestore or deserialization
    public VegetableItem() {}

    // Parameterized constructor
    public VegetableItem(String itemName, String district, String date, String price) {
        this.itemName = itemName;
        this.district = district;
        this.date = date;
        this.price = price;
    }

    // Getters and Setters
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
