package entity;

public class DinnerTable {
    private int id;
    private String name;
    private int servingQuantity;
    private double surcharge;
    private boolean inUse;

    public DinnerTable() {
    }

    public DinnerTable(int id, String name, int servingQuantity, double surcharge, boolean inUse) {
        this.id = id;
        this.name = name;
        this.servingQuantity = servingQuantity;
        this.surcharge = surcharge;
        this.inUse = inUse;
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

    public int getServingQuantity() {
        return servingQuantity;
    }

    public void setServingQuantity(int servingQuantity) {
        this.servingQuantity = servingQuantity;
    }

    public double getSurcharge() {
        return surcharge;
    }

    public void setSurcharge(double surcharge) {
        this.surcharge = surcharge;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
