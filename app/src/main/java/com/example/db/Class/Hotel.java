package com.example.db.Class;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Hotel implements Serializable {
    private int id;
    private int cityId;
    private City city;
    private short starCount;
    private String description;
    private String name;
    private SerializableBitmap image;

    public Hotel(int id, int cityId, short starCount, String description, String name, SerializableBitmap image) {
        this.id = id;
        this.cityId = cityId;
        this.starCount = starCount;
        this.description = description;
        this.name = name;
        this.image = image;
    }

    public Hotel(int cityId, short starCount, String description, String name) {
        this.cityId = cityId;
        this.starCount = starCount;
        this.description = description;
        this.name = name;
    }

    public Hotel(int id, City city, short starCount, String description, String name, SerializableBitmap image) {
        this.id = id;
        this.city = city;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return name + '(' + starCount + "*), " + city.getCountry().getName() + ", " + city.getName();
    }
}
