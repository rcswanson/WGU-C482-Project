package controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.*;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static controller.MainForm.modifyIndex;

/**
 *
 * @author rickswanson
 *
 * Creates modify class for user to modify parts
 *
 */

public class ModifyPart implements Initializable {

    // Label
    public Label IDorCompany;

    // Radio Buttons
    public RadioButton inHouse;
    public RadioButton outsourced;

    // Text Fields for input
    public TextField IDText;
    public TextField nameText;
    public TextField invText;
    public TextField priceText;
    public TextField minText;
    public TextField inOutText;
    public TextField maxText;

    Part selectedPart;

    /**
     * Changes the label dependent on the selection of the radio buttons
     */
    public void onInHouse() {
        IDorCompany.setText("Machine ID");
    }
    public void onOutsourced() {
        IDorCompany.setText("Company Name");
    }

    /**
     * Saves the newly modified part to the parts table
     *
     * @param actionEvent save part to inventory
     * @throws IOException FXMLLoader
     */
    public void onSaveModifyPart(ActionEvent actionEvent) throws IOException {
        try {
            int id = Integer.parseInt(IDText.getText());
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

            else if (stock < min || stock > max) { // Alerts  user if no cost is inputted
                Alert alert = new Alert(Alert.AlertType.ERROR, "ERROR: The part must have an inventory within the bounds of Min and Max");
                alert.setTitle("ERROR");
                alert.showAndWait();
            }

            else { // gathers the information depending on if the inhouse radio button or outsourced radio button is selected
                if (inHouse.isSelected()) {
                    int machineId = Integer.parseInt(inOutText.getText());
                    InHouse newInHousePart = new InHouse(id, name, stock, price, min, max, machineId);
                    Inventory.deletePart(selectedPart);
                    Inventory.addPart(newInHousePart);
                    MainForm.returnToMain(actionEvent);
                }
                else if (outsourced.isSelected()) {
                    String companyName = inOutText.getText();
                    Outsourced newOutsourcedPart = new Outsourced(id, name, stock, price, min, max, companyName);
                    Inventory.deletePart(selectedPart);
                    Inventory.addPart(newOutsourcedPart);
                    MainForm.returnToMain(actionEvent);
                }
            }
        }
        catch (Exception e) { // Alerts user if nothing is inputted
            Alert emptyBox = new Alert(Alert.AlertType.ERROR, "The text boxes are invalid or empty.");
            emptyBox.setTitle("ERROR");
            emptyBox.showAndWait();
        }
    }


    /**
     * Cancels the input and returns to the main screen
     *
     * @param actionEvent cancel part
     * @throws IOException FXMLLoader
     */
    public void onCancelModifyPart(ActionEvent actionEvent) throws IOException {
        Alert cancel = new Alert(Alert.AlertType.WARNING, "This will erase all typed information and return to the main window. Would you like to proceed?");
        cancel.setTitle("Cancel?");
        Optional<ButtonType> dResult = cancel.showAndWait();
        if (dResult.isPresent() && dResult.get() == ButtonType.YES) ;

        MainForm.returnToMain(actionEvent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectedPart = MainForm.getPartToModify();

        // When Modify Part Controller is opened, this method grabs the information of selected part and parses them to the window
        if (selectedPart instanceof InHouse) {
            inHouse.setSelected(true);
            IDorCompany.setText("Machine ID");
            inOutText.setText(String.valueOf(((InHouse) selectedPart).getMachineId()));
        }

        if (selectedPart instanceof Outsourced){
            outsourced.setSelected(true);
            IDorCompany.setText("Company Name");
            inOutText.setText(((Outsourced) selectedPart).getCompanyName());
        }

        IDText.setText(String.valueOf(selectedPart.getId()));
        nameText.setText(selectedPart.getName());
        invText.setText(String.valueOf(selectedPart.getStock()));
        priceText.setText(String.valueOf(selectedPart.getPrice()));
        maxText.setText(String.valueOf(selectedPart.getMax()));
        minText.setText(String.valueOf(selectedPart.getMin()));

    }
}
