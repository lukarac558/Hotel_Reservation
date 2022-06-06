package com.example.db.Class;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class HotelName implements Serializable, Comparable<HotelName> {
    private int id;
    private String name;

    public HotelName(int id, String name) {
        this.id = id;
        this.name = name;
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

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(HotelName hotelName) {
        return this.getName().compareToIgnoreCase(hotelName.getName());
    }
}
