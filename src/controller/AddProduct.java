package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static model.Inventory.lookupPart;

/**
 *
 * @author rickswanson
 *
 * Creates class in which user can add products to inventory
 *
 */

public class AddProduct implements Initializable {

    // Table of parts to associate with product
    public TableView addAssocPartsTable;
    public TableColumn partIDColumn;
    public TableColumn partNameColumn;
    public TableColumn partInvColumn;
    public TableColumn partCostColumn;

    // Parts currently associated with product
    public TableView assocPartsTable;
    public TableColumn assocIDColumn;
    public TableColumn assocNameColumn;
    public TableColumn assocInvColumn;
    public TableColumn assocCostColumn;

    // Text Fields for the input
    public TextField IDText;
    public TextField nameText;
    public TextField invText;
    public TextField priceText;
    public TextField minText;
    public TextField maxText;
    public TextField searchAssocPart;

    private ObservableList<Part> assocParts = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //set items in the tables to the values of the corresponding classes
        addAssocPartsTable.setItems(Inventory.getAllParts()); // Sets the partsTable view to the observable list
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        assocIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    static int autoId = 100; // assigns id to a starting number and increments it when adding a product

    /**
     * allows the table to be searchable by input
     *
     * @param actionEvent typed characters
     */
    @FXML
    void onSearchAssocPart(ActionEvent actionEvent) {

        ObservableList<Part> allParts = Inventory.getAllParts();
        ObservableList<Part> partsFound = FXCollections.observableArrayList();
        String searchString = searchAssocPart.getText();

        for (Part part : allParts) {
            if (String.valueOf(part.getId()).contains(searchString) ||
                    part.getName().contains(searchString)) {
                partsFound.add(part);
            }
        }

        addAssocPartsTable.setItems(partsFound);

        if (partsFound.size() == 0) {
            Alert noSearch = new Alert(Alert.AlertType.WARNING, "No Parts Found");
            noSearch.setTitle("No Parts Found");
            Optional<ButtonType> result = noSearch.showAndWait();
        }
    }

    /**
     * This method resets the parts table when nothing is entered
     *
     * @param keyEvent typing of any key
     */
    @FXML
    void onSearchAssocPartKey(KeyEvent keyEvent) {
        if (searchAssocPart.getText().isEmpty()) {
            addAssocPartsTable.setItems(Inventory.getAllParts());
        }
    }

    /**
     * Cancels the inputted information and returns to the main screen
     *
     * @param actionEvent cancelling add product
     * @throws IOException FXMLLoader
     */
    public void onCancelAddProduct(ActionEvent actionEvent) throws IOException {
        Alert cancel = new Alert(Alert.AlertType.WARNING, "This will erase all typed information and return to the main window. Would you like to proceed?");
        cancel.setTitle("Cancel?");
        Optional<ButtonType> dResult = cancel.showAndWait();
        if (dResult.isPresent() && dResult.get() == ButtonType.YES) ;

        MainForm.returnToMain(actionEvent);
    }

    /**
     * Adds the selected associated part to the product
     *
     * @param actionEvent associate selected part
     */
    public void onAddAssocPart (ActionEvent actionEvent) {
        Part SP = (Part) addAssocPartsTable.getSelectionModel().getSelectedItem();

        if (SP == null) {
            return;
        }
        assocParts.add(SP);
        assocPartsTable.setItems(assocParts);
    }

    /**
     * Removes the selected associated part from the product
     *
     * @param actionEvent remove associaed part
     */
    public void onRemoveAssocPart (ActionEvent actionEvent) {
        Part SP = (Part) assocPartsTable.getSelectionModel().getSelectedItem();

        if (SP == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to remove associated part?");
        alert.setTitle("Remove Part?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            result.get();
            assocParts.remove(SP);
            assocPartsTable.setItems(assocParts);
        }
    }

    /**
     * Adds a new product to the products table
     *
     * @param actionEvent save product
     * @throws IOException FXMLLoader
     */
    public void onSaveProduct (ActionEvent actionEvent) throws IOException {
        try {
            int id = autoId;
            String name = nameText.getText();
            double price = Double.parseDouble(priceText.getText());
            int stock = Integer.parseInt(invText.getText());
            int min = Integer.parseInt(minText.getText());
            int max = Integer.parseInt(maxText.getText());

            if (name.isEmpty()) { // Alerts user if name textbox is empty
                Alert alert = new Alert(Alert.AlertType.ERROR, "ERROR: Part must have a name");
                alert.setTitle("ERROR");
                alert.showAndWait();
            }

            else if (min > max) { // Alerts user if 'min' input is larger than the 'max' input
                Alert alert = new Alert(Alert.AlertType.ERROR, "ERROR: The minimum must be equal to or less than the maximum");
                alert.setTitle("ERROR");
                alert.showAndWait();
            }

            else if (stock < min || stock > max) { // Alerts user to input a cost for the product
                Alert alert = new Alert(Alert.AlertType.ERROR, "ERROR: The part must have an inventory within the bounds of Min and Max");
                alert.setTitle("ERROR");
                alert.showAndWait();
            }

            else {
                Product newProduct = new Product(id, name, price, stock, min, max);

                for (Part part : assocParts) {
                    newProduct.addAssociatedPart(part);
                }

                Inventory.addProduct(newProduct);
                autoId++;
                MainForm.returnToMain(actionEvent);

                }
        }
        catch (Exception e) { // Alerts user if nothing is inputted
            Alert emptyBox = new Alert(Alert.AlertType.ERROR, "The text boxes are invalid or empty.");
            emptyBox.setTitle("ERROR");
            emptyBox.showAndWait();
        }
    }
}

