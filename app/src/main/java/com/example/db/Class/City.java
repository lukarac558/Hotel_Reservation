package com.example.db.Class;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class City implements Serializable, Comparable<City> {
    private int id;
    private String name;
    private short countryId;
    private Country country;

    public City(int id, String name, short countryId) {
        this.id = id;
        this.name = name;
        this.countryId = countryId;
    }

    public City(int id, String name, Country country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(City city) {
        return this.getName().compareToIgnoreCase(city.getName());
    }
}
