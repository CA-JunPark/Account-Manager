package com.example.passwordmanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.io.IOException;

public class PW_Application extends Application {

    public static Scene scene;

    public static ArrayList<AccountButton> Accounts = new ArrayList<>();

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PW_Application.class.getResource("main.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage.setTitle("Password Manager");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}