package com.example.passwordmanager;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.MessageFormat;

public class Controller {

    @FXML
    private TextField accountText;
    @FXML
    private TextField idText;
    @FXML
    private TextField pwText;
    @FXML
    private TextArea additionalText;
    @FXML
    private VBox accountList;

    public void addAccount(){

        String account = accountText.getText();
        String id = idText.getText();
        String pw = pwText.getText();
        String additional = additionalText.getText();

        // create a Button and add it into Accounts
        String info = MessageFormat.format
                ("{0}\nID: {1}\nPW: {2}", account, id, pw);
        AccountButton accountButton = new AccountButton(info, account, id, pw, additional);

        // add action event on click
        accountButton.setOnAction(this::clickAccount);

        // add button in the Accounts ArrayList
        PW_Application.Accounts.add(accountButton);

        // sort the Accounts ArrayList by alphabetical order
        PW_Application.Accounts.sort(new AccountComparator());

        // adjust accountList Height
        int listLength = 0;
        for (AccountButton b : PW_Application.Accounts){
            listLength += b.getHeight();
        }
        accountList.setPrefHeight(listLength);

        // clear accountList
        accountList.getChildren().clear();

        // sort accountList by account name
        PW_Application.Accounts.sort(new AccountComparator());

        // display the button in the window
        for(int i = 0; i < PW_Application.Accounts.size(); i++){
            accountList.getChildren().add(PW_Application.Accounts.get(i));
        }

        // focus on the newest button
        accountButton.requestFocus();
    }

    public void deleteAccount(){
        // identify the AccountButton
        AccountButton selectedButton = (AccountButton) PW_Application.scene.getFocusOwner();

        // remove identified AccountButton from Accounts ArrayList
        PW_Application.Accounts.remove(selectedButton);

        // adjust accountList Height
        int listLength = 0;
        for (AccountButton b : PW_Application.Accounts){
            listLength += b.getHeight();
        }
        accountList.setPrefHeight(listLength);

        // clear accountList
        accountList.getChildren().clear();

        // sort accountList by account name
        PW_Application.Accounts.sort(new AccountComparator());

        // display the button in the window
        for(int i = 0; i < PW_Application.Accounts.size(); i++){
            accountList.getChildren().add(PW_Application.Accounts.get(i));
        }
    }

    public void clickAccount(ActionEvent event){
        // identify the selected AccountButton
        AccountButton selectedButton = (AccountButton) event.getSource();

        // set the TextFields from the selectedButton
        accountText.setText(selectedButton.getAccount());
        idText.setText(selectedButton.getID());
        pwText.setText(selectedButton.getPW());
        additionalText.setText(selectedButton.getAdditional());
    }

    public void clickReset(){
        // clear all texts
        accountText.clear();
        idText.clear();
        pwText.clear();
        additionalText.clear();

        // focus on the accountText
        accountText.requestFocus();
    }

}