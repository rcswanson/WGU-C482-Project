package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static model.Inventory.*;

/**
 *
 * @author rickswanson
 *
 */

//----------------------------------------------------------------------------------------------

public class MainForm implements Initializable {

    static Stage stage;
    static Parent scene;

    private static Part partToModify;
    private static Product productToModify;

    // Parts Table
    public TableView<Part> partsTable;
    public TableColumn partIDColumn;
    public TableColumn partNameColumn;
    public TableColumn partInvColumn;
    public TableColumn partCostColumn;
    //Parts Buttons
    public Button addPartB;
    public Button deletePartB;
    public Button modifyPartB;
    // Products Table
    public TableView<Product> productsTable;
    public TableColumn productIDColumn;
    public TableColumn productNameColumn;
    public TableColumn productInvColumn;
    public TableColumn productCostColumn;
    // Product Buttons
    public Button modifyProductB;
    public Button deleteProductB;
    public Button addProductB;
    // Search Bars
    public TextField searchPart;
    public TextField searchProduct;

    private static int index;
    public static int modifyIndex() { return index; }

    /**
     * this is called to return the user to main form whenever it is called
     * @param actionEvent returns user to main form controller
     * @throws IOException FXMLLoader
     */
    public static void returnToMain(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(MainForm.class.getResource("/view/MainForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

////// PARTS SECTION -------------------------------------------------------------------------------

    /** FUTURE ENHANCEMENTS: The search box is very specific with which it searches for items in both the parts and products search.
     * For example, it is picky between lowercase and uppercase.
     * Also, would be a neat upgrade to have it constantly refresh table with any typed character instead of hitting enter with each search
     *
     * allows table to be searchable
     *
     */
    @FXML
    void onSearchPart(ActionEvent event) {

        ObservableList<Part> allParts = Inventory.getAllParts();
        ObservableList<Part> partsFound = FXCollections.observableArrayList();
        String searchString = searchPart.getText();

        for (Part part : allParts) {
            if (String.valueOf(part.getId()).contains(searchString) ||
                    part.getName().contains(searchString)) {
                partsFound.add(part);
            }
        }

        partsTable.setItems(partsFound);

        if (partsFound.size() == 0) {
            Alert noSearch = new Alert(Alert.AlertType.WARNING, "No Parts Found");
            noSearch.setTitle("No Parts Found");
            Optional<ButtonType> result = noSearch.showAndWait();
        }
    }

    /**
     * resets table if nothing is inputted
     * @param keyEvent key type
     */
    public void onSearchPartKey(KeyEvent keyEvent) {
        if (searchPart.getText().isEmpty()) {
            partsTable.setItems(getAllParts());
        }
    }

    /**
     * Opens Add Part Controller
     *
     * @param actionEvent open add part window
     * @throws IOException FXMLLoader
     */
    public void onAddPartB(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }


    /**
     * returns selected part to the modify window
     * @return part to modify
     */
    public static Part getPartToModify() { return partToModify; }

    /**
     * Opens Modify Part Controller
     *
     * @param actionEvent open modify window
     * @throws IOException FXMLLoader
     */
    public void onModifyPartB(ActionEvent actionEvent) throws IOException {

        partToModify = partsTable.getSelectionModel().getSelectedItem();
        index = getAllParts().indexOf(partToModify);

        Parent parent = FXMLLoader.load(getClass().getResource("/view/ModifyPart.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

        }

    /**
     * Deletes selected/highlighted part from the observable list and alerts user to confirm deletion
     *
     * @param actionEvent delete selected part
     */
    public void onDeletePartB(ActionEvent actionEvent) {
        Part SP = partsTable.getSelectionModel().getSelectedItem();

        if (SP == null) {
            return;
        }

        Alert deleteWarning = new Alert(Alert.AlertType.CONFIRMATION, "This will delete the selected part from inventory, do you wish to continue?");
        deleteWarning.setTitle("Delete Part?");
        Optional<ButtonType> result = deleteWarning.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Inventory.deletePart(SP);

            Alert deleted = new Alert(Alert.AlertType.WARNING, "Part Deleted");
            Optional<ButtonType> dResult = deleted.showAndWait();
            if (dResult.isPresent()) {
                dResult.get();
            }
        }
    }

////// PRODUCTS SECTION --------------------------------------------------------------------------------

    /**
     * searches products in table
     * @param actionEvent typed characters with enter key action
     */
    @FXML
    void onSearchProduct (ActionEvent actionEvent) {
        ObservableList<Product> allProducts = Inventory.getAllProducts();
        ObservableList<Product> productsFound = FXCollections.observableArrayList();
        String searchString = searchProduct.getText();

        for (Product product : allProducts) {
            if (String.valueOf(product.getId()).contains(searchString) ||
                    product.getName().contains(searchString)) {
                productsFound.add(product);
            }
        }

        productsTable.setItems(productsFound);

        if (productsFound.size() == 0) {
            Alert noSearch = new Alert(Alert.AlertType.WARNING, "No Products Found");
            noSearch.setTitle("No Products Found");
            Optional<ButtonType> result = noSearch.showAndWait();
        }
    }

    /**
     * Reset Products table to full list if nothing is typed in the search bar
     *
     * @param keyEvent any key typed
     */
    public void onSearchProductKey(KeyEvent keyEvent) {
        if (searchProduct.getText().isEmpty()) {
            productsTable.setItems(getAllProducts());
        }
    }

    /**
     * Opens Add Product Controller
     *
     * @param actionEvent open add product window
     * @throws IOException FXMLLoader
     */
    public void onAddProductB(ActionEvent actionEvent) throws IOException {

        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     * returns selected product to modify window
     * @return product to modify
     */
    public static Product getProductToModify() {
        return productToModify;
    }

    /**
     * Opens Modify Product Controller
     *
     * @param actionEvent open modify product window
     * @throws IOException FXMLLoader
     */
    public void onModifyProductB(ActionEvent actionEvent) throws IOException {

        productToModify = productsTable.getSelectionModel().getSelectedItem();
        index = getAllProducts().indexOf(productToModify);

        Parent parent = FXMLLoader.load(getClass().getResource("../view/ModifyProduct.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Deletes selected/highlighted product from the observable list and alerts user to confirm deletion, reports error if product has assocaited parts
     *
     * @param actionEvent delete selected product
     */
    public void onDeleteProductB(ActionEvent actionEvent) {

        Product SP = productsTable.getSelectionModel().getSelectedItem();

        if (SP == null) {
            return;
        }

        if (SP.getAssocParts().size() > 0) {
            Alert cantDelete = new Alert(Alert.AlertType.ERROR);
            cantDelete.setTitle("Error Message");
            cantDelete.setContentText("Remove associated parts before you delete the product.");
            cantDelete.showAndWait();
            return;
        }

        Alert deleteWarning = new Alert(Alert.AlertType.CONFIRMATION, "This will delete the selected product from inventory, do you wish to continue?");
        deleteWarning.setTitle("Delete Product?");
        Optional<ButtonType> result = deleteWarning.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deleteProduct(SP);
                Alert deleted = new Alert(Alert.AlertType.WARNING, "Product Deleted");
                Optional<ButtonType> dResult = deleted.showAndWait();
                if (dResult.isPresent()) {
                    dResult.get();
            }
        }
    }

    // Exit Button closes application
    public void onExitB(ActionEvent actionEvent) {
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //set items in the tables to the values of the corresponding classes
        partsTable.setItems(getAllParts()); // Sets the partsTable view to the observable list
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        productsTable.setItems(getAllProducts()); // Sets the productsTable view to the observable list
        productIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}
