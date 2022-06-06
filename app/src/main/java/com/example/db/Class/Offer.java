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

    public Offer(short placesNumber, double price, LocalDate startDate, LocalDate endDate, int hotelId) {
        this.placesNumber = placesNumber;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hotelId = hotelId;
    }

    public Offer(int id, short placesNumber, double price, LocalDate startDate, LocalDate endDate, int hotelId) {
        this.id = id;
        this.placesNumber = placesNumber;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hotelId = hotelId;
    }

    public Offer(int id, short placesNumber, double price, LocalDate startDate, LocalDate endDate, Hotel hotel) {
        this.id = id;
        this.placesNumber = placesNumber;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hotel = hotel;
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

    @NonNull
    @Override
    public String toString() {
        return "Offer{" +
                "id=" + id +
                ", placesNumber=" + placesNumber +
                ", price=" + price +
                ", startDate=" + startDate.toString() +
                ", endDate=" + endDate.toString() +
                ", hotelId=" + hotelId +
                ", hotel=" + hotel +
                '}';
    }
}
