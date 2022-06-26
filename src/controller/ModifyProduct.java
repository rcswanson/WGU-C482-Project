package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import model.Inventory;
import model.Part;

import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static controller.MainForm.modifyIndex;

/**
 *
 * @author rickswanson
 *
 * creates modify class for users to modify products
 *
 */

public class ModifyProduct implements Initializable {

    Product selectedProduct;

    // Table of available parts to associate to the product
    public TableView<Part> addAssocPartsTable;
    public TableColumn partIDColumn;
    public TableColumn partNameColumn;
    public TableColumn partInvColumn;
    public TableColumn partCostColumn;

    // Parts associated with selected product
    public TableView<Part> assocPartsTable;
    public TableColumn partAssocIDColumn;
    public TableColumn partAssocNameColumn;
    public TableColumn partAssocInvColumn;
    public TableColumn partAssocCostColumn;

    // Text Fields for input
    public TextField IDText;
    public TextField nameText;
    public TextField invText;
    public TextField priceText;
    public TextField minText;
    public TextField maxText;
    public TextField searchAssocPart;

    // Buttons
    public Button cancelModifyProduct;
    public Button removeAssocPart;
    public Button saveModifyProduct;

    private ObservableList<Part> assocParts = FXCollections.observableArrayList();
    private Product product;
    int productIndex = modifyIndex();

    /**
     *
     * allows the table to be searchable
     * @param actionEvent input of text field
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
     * This method resets the Parts table when nothing is entered in the search bar
     *
     * @param keyEvent any key typed
     */
    @FXML
    void onSearchAssocPartKey(KeyEvent keyEvent) {
        if (searchAssocPart.getText().isEmpty()) {
            addAssocPartsTable.setItems(Inventory.getAllParts());
        }
    }

    /**
     * Cancels modify product and returns to main form
     *
     * @param actionEvent cancels product
     * @throws IOException FXMLLoader
     */
    public void onCancelModifyProduct(ActionEvent actionEvent) throws IOException {
        Alert cancel = new Alert(Alert.AlertType.WARNING, "This will erase all typed information and return to the main window. Would you like to proceed?");
        cancel.setTitle("Cancel?");
        Optional<ButtonType> dResult = cancel.showAndWait();
        if (dResult.isPresent()) {
            dResult.get();
        }

        MainForm.returnToMain(actionEvent);
    }

    /**
     * Adds the selected associated part to the product
     * @param actionEvent associate part
     */
    public void onAddAssocPart (ActionEvent actionEvent) {
        Part SP = addAssocPartsTable.getSelectionModel().getSelectedItem();

        if (SP == null) {
            return;
        }
        assocParts.add(SP);
        assocPartsTable.setItems(assocParts);
    }

    /**
     * Removes the selected associated part from the product
     *
     * @param actionEvent remove associated part
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
     * Adds the modified product to the products table
     * RUNTIME ERROR: Product will not save if price input is a double. Changed the price to Double.parseDouble instead of integer fixed the issue
     * @param actionEvent saves product to inventory
     *
     */
    public void onSaveModifyProduct (ActionEvent actionEvent) {
        try {
            int id = Integer.parseInt(IDText.getText());
            String name = nameText.getText();
            double price = Double.parseDouble(priceText.getText());
            int stock = Integer.parseInt(invText.getText());
            int min = Integer.parseInt(minText.getText());
            int max = Integer.parseInt(maxText.getText());

            if (name.isEmpty()) { // Alerts user if name text box is empty
                Alert alert = new Alert(Alert.AlertType.ERROR, "ERROR: Part must have a name");
                alert.setTitle("ERROR");
                alert.showAndWait();
            }

            else if (min > max) { // Alerts user if 'min' input is larger than the 'max' input
                Alert alert = new Alert(Alert.AlertType.ERROR, "ERROR: The minimum must be equal to or less than the maximum");
                alert.setTitle("ERROR");
                alert.showAndWait();
            }

            else if (stock < min || stock > max) { // Alerts  user if no cost is inputted
                Alert alert = new Alert(Alert.AlertType.ERROR, "ERROR: The part must have an inventory within the bounds of Min and Max");
                alert.setTitle("ERROR");
                alert.showAndWait();
            }

            Product newProduct = new Product(id, name, price, stock, min, max);

            for (Part part : assocParts) {
                newProduct.addAssociatedPart(part);
            }

            Inventory.updateProduct(productIndex, product);
            MainForm.returnToMain(actionEvent);

        }
        catch (Exception e) {
            Alert emptyBox = new Alert(Alert.AlertType.ERROR, "The text boxes are invalid or  empty.");
            emptyBox.setTitle("Error");
            emptyBox.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        selectedProduct = MainForm.getProductToModify();
        assocParts = selectedProduct.getAssocParts();

        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        addAssocPartsTable.setItems(Inventory.getAllParts());

        partAssocIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partAssocNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partAssocInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partAssocCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        assocPartsTable.setItems(assocParts);

        IDText.setText(String.valueOf(selectedProduct.getId()));
        nameText.setText(selectedProduct.getName());
        invText.setText(String.valueOf(selectedProduct.getStock()));
        priceText.setText(String.valueOf(selectedProduct.getPrice()));
        maxText.setText(String.valueOf(selectedProduct.getMax()));
        minText.setText(String.valueOf(selectedProduct.getMin()));

    }
}
