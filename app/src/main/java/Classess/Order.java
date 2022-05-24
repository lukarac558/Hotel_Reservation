package Classess;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class Order implements Serializable {
    private int id;
    private double totalCost;
    private short peopleCount;
    private int hotelId;
    private Hotel hotel;
    private LocalDate orderDate;
    private int userId;
    private User user;

    public Order(int id, double totalCost, short peopleCount, int hotelId, LocalDate orderDate, int userId) {
        this.id = id;
        this.totalCost = totalCost;
        this.peopleCount = peopleCount;
        this.hotelId = hotelId;
        this.orderDate = orderDate;
        this.userId = userId;
    }

    public Order(int id, double totalCost, short peopleCount, Hotel hotel, LocalDate orderDate, User user) {
        this.id = id;
        this.totalCost = totalCost;
        this.peopleCount = peopleCount;
        this.hotel = hotel;
        this.orderDate = orderDate;
        this.user = user;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public short getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(short peopleCount) {
        this.peopleCount = peopleCount;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public Hotel getHotel() { return hotel; }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }
}
