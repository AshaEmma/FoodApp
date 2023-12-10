package com.cs407.newfridge;

public class FoodItem {
    private String name;
    private String category;
    private String quantity;
    private String unit;
    private String storageType;
    private String expiryDate;
    private int iconResId;

    public FoodItem(String name, String category, String quantity, String unit, String storageType, String expiryDate) {
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.unit = unit;
        this.storageType = storageType;
        this.expiryDate = expiryDate;
        this.iconResId = getIconResId(category);
    }

    // Getter and setter methods
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public String getStorageType() { return storageType; }
    public String getExpiryDate() { return expiryDate; }
    public int getIconResId() { return iconResId; }

    private static int getIconResId(String category) {
        switch (category) {
            case "Vegetables":
                return R.drawable.vegetables;
            case "Fruits":
                return R.drawable.fruits;
            case "Grains":
                return R.drawable.grains;
            case "Meat":
                return R.drawable.meat;
            case "Dairy":
                return R.drawable.dairy;
            case "Oils":
                return R.drawable.oils;
            case "Beverages":
                return R.drawable.beverages;
            case "Other":
                return R.drawable.other;
            default:
                return R.drawable.other;
        }
    }
}