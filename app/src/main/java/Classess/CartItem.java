package Classess;

public class CartItem {
    private int id;
    private int offerId;
    private int userId;

    public CartItem(int id, int offerId, int userId) {
        this.id = id;
        this.offerId = offerId;
        this.userId = userId;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
