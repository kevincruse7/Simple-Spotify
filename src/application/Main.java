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

    public static void exitWithException(Exception e, String action) {
        System.err.println("Exception when " + action + ":");
        e.printStackTrace();
        System.exit(1);
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
            Main.exitWithException(e, "reading FXML files");
        }

        AnchorPane root = null;
        try {
            root = loader.<AnchorPane>load(loginPageStream);
        }
        catch (IOException e) {
            Main.exitWithException(e, "loading FXML files");
        }

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simple Spotify");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}