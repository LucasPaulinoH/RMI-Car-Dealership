package model;

import java.io.Serializable;

import types.Types.CarCategory;

public class Car implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private CarCategory category;
    private String renavam;
    private String manufactureYear;
    private double price;

    public Car(String name, CarCategory category, String renavam, String manufactureYear, double price) {
        this.name = name;
        this.category = category;
        this.renavam = renavam;
        this.manufactureYear = manufactureYear;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CarCategory getCategory() {
        return category;
    }

    public void setCategory(CarCategory category) {
        this.category = category;
    }

    public String getRenavam() {
        return renavam;
    }

    public void setRenavam(String renavam) {
        this.renavam = renavam;
    }

    public String getManufactureYear() {
        return manufactureYear;
    }

    public void setManufactureYear(String manufactureYear) {
        this.manufactureYear = manufactureYear;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "=========================\n" + name.toUpperCase() + " (" + manufactureYear + ")\nRenavam: " + renavam
                + "\nCategory: " + category
                + "\nPrice (R$): " + price + "\n=========================\n";
    }
}
