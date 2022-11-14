package entity;

import javafx.scene.image.ImageView;

public class Dish {
    private int id;
    private String name;
    private double price;
    private ImageView image;
    private int totalSold;
    private DishCategory category;
    private String description;
    private boolean deleted;
    private double averageCostPrice;
    private boolean soldOut;
    private String soldOutText;

    public Dish() {
    }

    @Override
    public String toString() {
        return this.name;
    }

    public Dish(int id, String name, double price, ImageView image, int totalSold, DishCategory category, String description, boolean deleted, double averageCostPrice, boolean soldOut) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.totalSold = totalSold;
        this.category = category;
        this.description = description;
        this.deleted = deleted;
        this.averageCostPrice = averageCostPrice;
        this.soldOut = soldOut;
        this.soldOutText = (soldOut?"Out of Stock":"On Stock");
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getTotalSold() {
        return totalSold;
    }

    public void setTotalSold(int totalSold) {
        this.totalSold = totalSold;
    }

    public DishCategory getCategory() {
        return category;
    }

    public void setCategory(DishCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public double getAverageCostPrice() {
        return averageCostPrice;
    }

    public void setAverageCostPrice(double averageCostPrice) {
        this.averageCostPrice = averageCostPrice;
    }

    public boolean isSoldOut() {
        return soldOut;
    }

    public void setSoldOut(boolean soldOut) {
        this.soldOut = soldOut;
        this.soldOutText = (soldOut?"Out of Stock":"On Stock");
    }

    public String getSoldOutText() {
        return soldOutText;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }
}
