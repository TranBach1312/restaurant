package entity;

import java.sql.Timestamp;

public class Bill {
    private int id;
    private String billNumber;
    private Order order;
    private Customer customer;
    private DinnerTable dinnerTable;
    private boolean haveVat;
    private double vat;
    private double totalDiscount;
    private double totalMoney;
    private String payMethod;
    private String note;
    private Timestamp createdAt;

    private User billCreator;

    public Bill() {
    }

    public Bill(int id, String billNumber, Order order, Customer customer, boolean haveVat, double vat, double totalDiscount, double totalMoney, String payMethod, String note, Timestamp createdAt) {
        this.id = id;
        this.billNumber = billNumber;
        this.order = order;
        this.customer = customer;
        this.haveVat = haveVat;
        this.vat = vat;
        this.totalDiscount = totalDiscount;
        this.totalMoney = totalMoney;
        this.payMethod = payMethod;
        this.note = note;
        this.createdAt = createdAt;
        this.dinnerTable = this.order.getTable();
    }

    public Bill(String billNumber, Order order, Customer customer, boolean haveVat, double vat, double totalDiscount, double totalMoney, String payMethod, String note) {
        this.billNumber = billNumber;
        this.order = order;
        this.customer = customer;
        this.haveVat = haveVat;
        this.vat = vat;
        this.totalDiscount = totalDiscount;
        this.totalMoney = totalMoney;
        this.payMethod = payMethod;
        this.note = note;
        this.dinnerTable = this.order.getTable();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
        this.dinnerTable = this.order.getTable();
    }

    public DinnerTable getDinnerTable() {
        return dinnerTable;
    }

    public void setDinnerTable(DinnerTable dinnerTable) {
        this.dinnerTable = dinnerTable;
    }

    public User getBillCreator() {
        return billCreator;
    }

    public void setBillCreator(User billCreator) {
        this.billCreator = billCreator;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public boolean isHaveVat() {
        return haveVat;
    }

    public void setHaveVat(boolean haveVat) {
        this.haveVat = haveVat;
    }

    public double getVat() {
        return vat;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
