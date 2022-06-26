package main;
/**
 * Javadoc file zip is submitted along with this code zip file
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author rickswanson
 */

public class Main extends Application {

    @Override
    public void start(Stage startingWindow) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        startingWindow.setTitle("Inventory Management System");
        startingWindow.setScene(new Scene(root, 1400, 600));
        startingWindow.show();
    }

    public static void main(String[] args) {

        launch(args);

    }

}