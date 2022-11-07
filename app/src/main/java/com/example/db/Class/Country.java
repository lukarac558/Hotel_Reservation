package com.example.db.Class;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Country implements Serializable, Comparable<Country> {

    private String code;
    private String name;

    public Country(String name) {
        this.name = name;
    }

    public Country(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
        return "[" + code + "] " + name;
    }

    @Override
    public int compareTo(Country country) {
        return this.getCode().compareToIgnoreCase(country.getCode());
    }
}
