package com.example.db.Class;

import java.io.Serializable;
import java.time.LocalDate;

public class Order implements Serializable {
    private int id;
    private double totalCost;
    private short peopleCount;
    private int offerId;
    private Offer offer;
    private LocalDate orderDate;
    private int userId;
    private User user;

    public Order(int id, double totalCost, short peopleCount, int offerId, LocalDate orderDate, int userId) {
        this.id = id;
        this.totalCost = totalCost;
        this.peopleCount = peopleCount;
        this.offerId = offerId;
        this.orderDate = orderDate;
        this.userId = userId;
    }

    public Order(int id, double totalCost, short peopleCount, Offer offer, LocalDate orderDate, User user) {
        this.id = id;
        this.totalCost = totalCost;
        this.peopleCount = peopleCount;
        this.offer = offer;
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

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }
}
