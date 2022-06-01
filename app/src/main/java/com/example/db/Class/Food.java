package com.example.db.Class;

import java.io.Serializable;

public class Food implements Serializable, Comparable<Food> {
    private int id;
    private String type;

    public Food(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

    @Override
    public int compareTo(Food food) {
        return this.getType().compareToIgnoreCase(food.getType());
    }
}
