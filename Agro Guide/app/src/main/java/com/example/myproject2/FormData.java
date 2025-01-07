package com.example.myproject2;
public class FormData {
    public String name;
    public String phoneNumber;
    public String district;
    public String category;
    public String itemName;
    public String price;
    public String unit;
    public String imageUrl;

    // Default constructor required for calls to DataSnapshot.getValue(FormData.class)
    public FormData() {
    }

    public FormData(String name, String phoneNumber, String district, String category,
                    String itemName, String price, String unit, String imageUrl) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.district = district;
        this.category = category;
        this.itemName = itemName;
        this.price = price;
        this.unit = unit;
        this.imageUrl = imageUrl;
    }
}
