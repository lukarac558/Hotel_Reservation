package com.example.db.model;

import java.io.Serializable;

public class CartItem implements Serializable {

    private int id;
    private int offerId;
    private Offer offer;
    private short peopleCount;
    private int userId;
    private User user;

    public CartItem(int id, int offerId, short peopleCount, int userId) {
        this.id = id;
        this.offerId = offerId;
        this.peopleCount = peopleCount;
        this.userId = userId;
    }

    public CartItem(int offerId, short peopleCount) {
        this.offerId = offerId;
        this.peopleCount = peopleCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public short getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(short peopleCount) {
        this.peopleCount = peopleCount;
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

}
