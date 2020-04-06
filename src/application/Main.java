package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader();
        
        String loginPagePath = "resources/LoginPage.fxml";
        FileInputStream loginPageStream = null;
        try {
            loginPageStream = new FileInputStream(loginPagePath);
        }
        catch (FileNotFoundException e) {
            System.err.println("Exception when reading FXML files:");
            e.printStackTrace();
            System.exit(1);
        }

        AnchorPane root = null;
        try {
            root = loader.<AnchorPane>load(loginPageStream);
        }
        catch (IOException e) {
            System.err.println("Exception when loading FXML files:");
            e.printStackTrace();
            System.exit(2);
        }

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simple Spotify");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}