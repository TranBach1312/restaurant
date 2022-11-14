package entity;

import java.sql.Date;

public class Customer {
    private int id;
    private String name;
    private String phone;
    private String address;
    private Date birthDate;
    private int memberLevel;

    public Customer() {
    }

    public Customer(int id, String name, String phone, String address, Date birthDate, int memberLevel) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.birthDate = birthDate;
        this.memberLevel = memberLevel;
    }

    public Customer(String name, String phone, String address, Date birthDate) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.birthDate = birthDate;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public int getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(int memberLevel) {
        this.memberLevel = memberLevel;
    }

    @Override
    public String toString() {
        return this.phone;
    }
}
