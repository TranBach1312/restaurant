package entity;

import java.sql.Timestamp;

public class OrderItem {
    private Integer id;
    private Dish dish;
    private Double price;
    private Integer quantity;
    private double originTotalPrice;
    private double discount;
    private Double totalMoney;
    private Timestamp createAt;

    public OrderItem() {
    }

    public OrderItem(int id, Dish dish, int quantity, double originTotalPrice, double discount, double totalMoney, Timestamp createAt) {
        this.id = id;
        this.dish = dish;
        this.price = dish.getPrice();
        this.quantity = quantity;
        this.originTotalPrice = originTotalPrice;
        this.discount = discount;
        this.totalMoney = totalMoney;
        this.createAt = createAt;
    }

    public OrderItem(Dish dish, int quantity) {
        this.dish = dish;
        this.price = dish.getPrice();
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
        this.price = dish.getPrice();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getOriginTotalPrice() {
        return originTotalPrice;
    }

    public void setOriginTotalPrice(double originTotalPrice) {
        this.originTotalPrice = originTotalPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public Double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }
    public void updateTotal(){
        this.totalMoney = dish.getPrice() * quantity;
    }
}
