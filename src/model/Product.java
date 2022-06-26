package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {

    private int id;
    private String name;
    private int stock;
    private double price;
    private int min;
    private int max;

    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    // getters and setters for each variable of the products class

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * @return the associated parts
     */
    public ObservableList<Part> getAssocParts() {
        return assocParts;
    }

    public void addAssociatedPart(Part part) {
        assocParts.add(part);
    }

    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        if (assocParts.contains(selectedAssociatedPart)) {
            assocParts.remove(selectedAssociatedPart);
            return true;
        }
        else
            return false;
    }

    public ObservableList<Part> assocParts = FXCollections.observableArrayList();

    public ObservableList<Part> getAllAssociatedParts() {
        return assocParts;
    }
}
