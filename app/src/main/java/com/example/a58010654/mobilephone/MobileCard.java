package com.example.a58010654.mobilephone;

public class MobileCard {
    private int id;
    private String name;
    private String description;
    private double price;
    private double rating;
    private Boolean isFavorite;
    private String image_url;
    private String brand;

    public MobileCard(int id, String name, String description, double price, double rating, Boolean isFavorite, String image_url, String brand) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.rating = rating;
        this.isFavorite = isFavorite;
        this.image_url = image_url;
        this.brand = brand;
    }

    public int getId() {return id;}

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public double getRating() {
        return rating;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getBrand() {
        return brand;
    }
}
