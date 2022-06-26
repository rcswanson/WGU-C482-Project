package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.RadioButton;

/**
 * @author rickswanson
 */

public class Inventory {

    // Creates observable lists later linked to the table views
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();

    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /**
     * allows the adding of a new part to the parts table
     * @param newPart
     */
    public static void addPart (Part newPart) {
        allParts.add(newPart);
    }

    /**
     * allows the adding of a new product to the products table
     * @param newProduct
     */
    public static void addProduct (Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * searches parts by ID
     * @param partId
     * @return partFound
     */
    public static Part lookupPart(int partId) {
        Part partFound = null;

        for (Part prt : allParts) {
            if (prt.getId() == partId) {
                partFound = prt;
            }
        }

        return partFound;
    }

    /**
     * searches parts by name
     * @param partName
     * @return partsFound
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> partsFound = FXCollections.observableArrayList();

        for (Part prt : allParts) {
            if (prt.getName().equals(partName)) {
                partsFound.add(prt);
            }
        }

        return partsFound;
    }

    /**
     * searches product by ID
     * @param productId
     * @return productFound
     */
    public static Product lookupProduct(int productId) {
        Product productFound = null;

        for (Product prd : allProducts) {
            if (prd.getId() == productId) {
                productFound = prd;
            }
        }

        return productFound;
    }

    /**
     * searches product by name
     * @param productName
     * @return
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> productsFound = FXCollections.observableArrayList();

        for (Product prd : allProducts) {
            if (prd.getName().equals(productName)) {
                productsFound.add(prd);
            }
        }

        return productsFound;
    }

    /**
     * After the part is modified and save is clicked, this updates the part in its index instead of saving a new one and deleting the old one
     * @param index index of part on table
     * @param selectedPart part clicked on
     */
    public static void updatePart (int index, Part selectedPart) {
    }

    /**
     * After the product is modified and save is clicked, this updates the product in its index instead of saving a new one and deleting the old one
     * @param index index of product in table
     * @param selectedProduct product clicked on
     */
    public static void updateProduct (int index, Product selectedProduct) {
    }

    /**
     *
     * @param selectedPart part selected from an observable list
     * @return bool
     */
    public static boolean deletePart(Part selectedPart) {
        if (allParts.contains(selectedPart)) {
            allParts.remove(selectedPart);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     *
     * @param selectedProduct product selected from observable list
     * @return bool
     */
    public static boolean deleteProduct(Product selectedProduct) {
        if (allProducts.contains(selectedProduct)) {
            allProducts.remove(selectedProduct);
            return true;
        }
        else {
            return false;
        }
    }
}