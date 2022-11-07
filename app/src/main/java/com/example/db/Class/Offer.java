package com.example.db.Class;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.time.LocalDate;

public class Offer implements Serializable {
    private int id;
    private short placesNumber;
    private double price;
    private LocalDate startDate;
    private LocalDate endDate;
    private int hotelId;
    private Hotel hotel;
    private int foodId;
    private Food food;
    private short peopleCount;

    public Offer(short placesNumber, double price, LocalDate startDate, LocalDate endDate, int hotelId, int foodId) {
        this.placesNumber = placesNumber;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hotelId = hotelId;
        this.foodId = foodId;
    }

    public Offer(int id, short placesNumber, double price, LocalDate startDate, LocalDate endDate, int hotelId, int foodId) {
        this.id = id;
        this.placesNumber = placesNumber;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hotelId = hotelId;
        this.foodId = foodId;
    }

    public Offer(int id, short placesNumber, double price, LocalDate startDate, LocalDate endDate, Hotel hotel, Food food) {
        this.id = id;
        this.placesNumber = placesNumber;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hotel = hotel;
        this.food = food;
    }

    public Offer(int id, short placesNumber, double price, LocalDate startDate, LocalDate endDate, Hotel hotel, Food food, short peopleCount) {
        this.id = id;
        this.placesNumber = placesNumber;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hotel = hotel;
        this.food = food;
        this.peopleCount = peopleCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public short getPlacesNumber() {
        return placesNumber;
    }

    public void setPlacesNumber(short placesNumber) {
        this.placesNumber = placesNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
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

    public short getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(short peopleCount) {
        this.peopleCount = peopleCount;
    }

    @NonNull
    @Override
    public String toString() {
        return "Offer id = " + id;
    }
}
