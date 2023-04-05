package com.fashionstore.tlstore.Model;


import java.util.Collection;
import java.util.List;

public class ProductModel {
    private long id;
    private String productName;
    private int quantity;
    private int sold;
    private String description;
    private int price;
    private Boolean isActive = Boolean.TRUE;
    private CategoryModel category;

    private List<ProductImageModel> productImages;


    public ProductModel(long id, String productName, int price) {
        this.id = id;
        this.productName = productName;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public CategoryModel getCategory() {
        return category;
    }

    public void setCategory(CategoryModel category) {
        this.category = category;
    }

    public List<ProductImageModel> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImageModel> productImages) {
        this.productImages = productImages;
    }
}
