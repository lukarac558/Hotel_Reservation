package com.example.db.Class;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Hotel implements Serializable {
    private int id;
    private short countryId;
    private Country country;
    private int cityId;
    private City city;
    private int foodId;
    private Food food;
    private short starCount;
    private String description;
    private int nameId;
    private HotelName name;
    private SerializableBitmap image;

    public Hotel(int id, short countryId, int cityId, int foodId, short starCount, String description, int nameId, SerializableBitmap image) {
        this.id = id;
        this.countryId = countryId;
        this.cityId = cityId;
        this.foodId = foodId;
        this.starCount = starCount;
        this.description = description;
        this.nameId = nameId;
        this.image = image;
    }

    public Hotel(int id, short countryId, int cityId, int foodId, short starCount, String description, int nameId) {
        this.id = id;
        this.countryId = countryId;
        this.cityId = cityId;
        this.foodId = foodId;
        this.starCount = starCount;
        this.description = description;
        this.nameId = nameId;
    }

    public Hotel(int id, Country country, City city, Food food, short starCount, String description, HotelName name, SerializableBitmap image) {
        this.id = id;
        this.country = country;
        this.city = city;
        this.food = food;
        this.starCount = starCount;
        this.description = description;
        this.name = name;
        this.image = image;
    }

    public SerializableBitmap getImage() {
        return image;
    }

    public void setImage(SerializableBitmap image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public short getCountryId() {
        return countryId;
    }

    public void setCountryId(short countryId) {
        this.countryId = countryId;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public short getStarCount() {
        return starCount;
    }

    public void setStarCount(short starCount) {
        this.starCount = starCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNameId() {
        return nameId;
    }

    public void setNameId(int nameId) {
        this.nameId = nameId;
    }

    public HotelName getName() {
        return name;
    }

    public void setName(HotelName name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return name.getName() + '(' + starCount + "*), " + country.getName() + ", " + city.getName() + ", " + food.getType();
    }
}
