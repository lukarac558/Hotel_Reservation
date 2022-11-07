package com.example.db.Class;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class City implements Serializable, Comparable<City> {
    private int id;
    private String name;
    private String countryCode;
    private Country country;

    public City(String name) {
        this.name = name;
    }

    public City(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public City(int id, String name, String countryCode) {
        this.id = id;
        this.name = name;
        this.countryCode = countryCode;
    }

    public City(int id, String name, Country country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    public City(String name, Country country){
        this.name = name;
        this.country = country;
    }

    public City(String name, String countryCode){
        this.name = name;
        this.countryCode = countryCode;
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

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
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
