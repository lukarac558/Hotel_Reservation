package com.example.db.Class;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Country implements Serializable, Comparable<Country> {

    private short id;
    private String name;

    public Country(String name) {
        this.name = name;
    }

    public Country(short id, String name) {
        this.id = id;
        this.name = name;
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
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
        return name;
    }

    @Override
    public int compareTo(Country country) {
        return this.getName().compareToIgnoreCase(country.getName());
    }
}
