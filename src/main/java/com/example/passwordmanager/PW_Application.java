package com.example.passwordmanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.io.IOException;

public class PW_Application extends Application {

    public static Scene scene;

    public static ArrayList<AccountButton> Accounts = new ArrayList<>();

    public static boolean pwAccepted = false;

    @Override
    public void start(Stage stage) throws IOException {
        // check pw exists or not
        if (!new File("pw.json").isFile()){
            // Create Password window
            Stage CreatePwStage = new Stage();
            FXMLLoader pwSetUp = new FXMLLoader(PW_Application.class.getResource("pwSetUp.fxml"));
            Scene pwSetUpScene = new Scene(pwSetUp.load());
            CreatePwStage.setTitle("Create Password");
            CreatePwStage.setScene(pwSetUpScene);
            CreatePwStage.setResizable(false);
            CreatePwStage.showAndWait();
            if (new File("pw.json").isFile()){
                // Password Check window
                pwCheckWindow();
            }
        } else{
            // Password Check window
            pwCheckWindow();
        }

        if (pwAccepted){
            // Main application window
            FXMLLoader fxmlLoader = new FXMLLoader(PW_Application.class.getResource("main.fxml"));
            scene = new Scene(fxmlLoader.load());
            stage.setTitle("Accounts Manager");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }
    }

    public static void pwCheckWindow() throws IOException {
        Stage pwStage = new Stage();
        FXMLLoader pw = new FXMLLoader(PW_Application.class.getResource("pw.fxml"));
        Scene pwScene = new Scene(pw.load());
        pwStage.setTitle("Accounts Manager");
        pwStage.setScene(pwScene);
        pwStage.setResizable(false);
        pwStage.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}