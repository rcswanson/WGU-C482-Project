package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Optional;

import model.Inventory;
import model.InHouse;
import model.Outsourced;

/**
 * @author rickswanson
 *
 * Creates the class in which user can add parts to inventory
 */

public class AddPart {

    // Radio Buttons
    public RadioButton inHouse;
    public RadioButton outsourced;

    // Machine ID or Company Name Label
    public Label IDorCompany;

    // Text Fields
    public TextField IDText;
    public TextField nameText;
    public TextField invText;
    public TextField priceText;
    public TextField maxText;
    public TextField inOutText;
    public TextField minText;

    // Starting integer for auto generated ID's
    static int autoId = 1;

    /**
     * Adjusts text box in correlation with the radio button selected
     */
    public void onInHouse() { IDorCompany.setText("Machine ID"); }
    public void onOutsourced() { IDorCompany.setText("Company Name"); }

    /**
     * Alerts the user to confirm that they would like to cancel part
     * Returns user back to Main Form window
     *
     * @param actionEvent cancels adding part
     * @throws IOException FXMLLoader
     */
    public void onCancelAddPart(ActionEvent actionEvent) throws IOException {

        Alert cancel = new Alert(Alert.AlertType.WARNING, "This will erase all typed information and return to the main window. Would you like to proceed?");
        cancel.setTitle("Cancel?");
        Optional<ButtonType> dResult = cancel.showAndWait();
        if (dResult.isPresent() && dResult.get() == ButtonType.YES);

        MainForm.returnToMain(actionEvent);
    }

    /** RUNTIME ERROR: price was initially parsing as an integer by mistake, causing an error when input was an actual double.
     * Changed price to parseDouble allowed input of doubles as well as integers.
     *
     * Gathers the inputted text from each text box, and parses the integers, adding each piece of information to the observable list
     *
     */
    public void onSavePart(ActionEvent actionEvent) throws IOException {
        try {
            int id = autoId;
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

            else if (stock < min || stock > max) { // Alerts  user if inventory is smaller than min or larger than max
                Alert alert = new Alert(Alert.AlertType.ERROR, "ERROR: The part must have an inventory within the bounds of Min and Max");
                alert.setTitle("ERROR");
                alert.showAndWait();
            }

            else { // gathers the information depending on if the inhouse radio button or outsourced radio button is selected
                if (inHouse.isSelected()) {
                    int machineId = Integer.parseInt(inOutText.getText());
                    InHouse newInHousePart = new InHouse(id, name, stock, price, min, max, machineId);
                    Inventory.addPart(newInHousePart);
                    autoId++;
                    MainForm.returnToMain(actionEvent);
                }
                else if (outsourced.isSelected()) {
                    String companyName = inOutText.getText();
                    Outsourced newOutsourcedPart = new Outsourced(id, name, stock, price, min, max, companyName);
                    Inventory.addPart(newOutsourcedPart);
                    autoId++;
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
}
    


