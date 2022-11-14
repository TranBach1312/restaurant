package entity;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Order {
    private int id;
    private DinnerTable table;
    ArrayList<OrderItem> orderItems;
    private double totalMoney;
    private double totalDiscount;
    private int staffId;
    private Timestamp creatAt;
    private Timestamp updateAt;
    private boolean status;
    private Timestamp endedAt;

    public Order() {
    }

    public Order(int id, DinnerTable table, ArrayList<OrderItem> orderItems, double totalMoney, double totalDiscount, int staffId, Timestamp creatAt, Timestamp updateAt, boolean status, Timestamp endedAt) {
        this.id = id;
        this.table = table;
        this.orderItems = orderItems;
        this.totalMoney = totalMoney;
        this.totalDiscount = totalDiscount;
        this.staffId = staffId;
        this.creatAt = creatAt;
        this.updateAt = updateAt;
        this.status = status;
        this.endedAt = endedAt;
    }

    public Order(DinnerTable table, ArrayList<OrderItem> orderItems, int staffId) {
        this.table = table;
        this.orderItems = orderItems;
        this.staffId = staffId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DinnerTable getTable() {
        return table;
    }

    public void setTable(DinnerTable table) {
        this.table = table;
    }

    public ArrayList<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(ArrayList<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public Timestamp getCreatAt() {
        return creatAt;
    }

    public void setCreatAt(Timestamp creatAt) {
        this.creatAt = creatAt;
    }

    public Timestamp getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Timestamp updateAt) {
        this.updateAt = updateAt;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Timestamp getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(Timestamp endedAt) {
        this.endedAt = endedAt;
    }
}
