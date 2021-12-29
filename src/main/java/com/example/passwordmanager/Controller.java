package com.example.passwordmanager;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;

import java.io.File;
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
    @FXML
    private TextField createPwText;
    @FXML
    private TextField createPwConfirmText;
    @FXML
    private TextField checkPwText;
    @FXML
    private TextField changeCurrentPwText;
    @FXML
    private TextField changeNewPwText;
    @FXML
    private TextField ChangeNewPwConfirmText;
    @FXML
    private TextField resetEmailText;
    @FXML
    private TextField resetEmailConfirmText;

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

        // adjust accountList Height
        int listLength = 0;
        for (AccountButton b : PW_Application.Accounts){
            listLength += b.getHeight();
        }
        accountList.setPrefHeight(listLength);

        // clear accountList
        accountList.getChildren().clear();

        // sort accountList by account name
        PW_Application.Accounts.sort((AccountButton a1, AccountButton a2) -> a1.getAccount().compareTo(a2.getAccount()));

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
        PW_Application.Accounts.sort((AccountButton a1, AccountButton a2) -> a1.getAccount().compareTo(a2.getAccount()));

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

    public void clickEdit(){
        try{

            // get current selected account
            AccountButton selectedButton = (AccountButton) PW_Application.scene.getFocusOwner();

            Stage stage = new Stage();

            // account pane
            FlowPane accountFlow = new FlowPane();
            accountFlow.setPadding(new Insets(10, 0 ,10, 10));
            accountFlow.setHgap(5);
            Label accountLabel = new Label("Account:");
            accountLabel.setAlignment(Pos.CENTER_LEFT);
            accountLabel.setPrefWidth(50);
            TextField accountText = new TextField();
            accountText.setText(selectedButton.getAccount());
            accountFlow.getChildren().addAll(accountLabel, accountText);

            // id pane
            FlowPane idFlow = new FlowPane();
            idFlow.setPadding(new Insets(10, 0 ,10, 10));
            idFlow.setHgap(5);
            Label idLabel = new Label("ID:");
            idLabel.setAlignment(Pos.CENTER_LEFT);
            idLabel.setPrefWidth(50);
            TextField idText = new TextField();
            idText.setText(selectedButton.getID());
            idFlow.getChildren().addAll(idLabel, idText);

            // pw pane
            FlowPane pwFlow = new FlowPane();
            pwFlow.setPadding(new Insets(10, 0 ,10, 10));
            pwFlow.setHgap(5);
            Label pwLabel = new Label("PW:");
            pwLabel.setAlignment(Pos.CENTER_LEFT);
            pwLabel.setPrefWidth(50);
            TextField pwText = new TextField();
            pwText.setText(selectedButton.getPW());
            pwFlow.getChildren().addAll(pwLabel, pwText);

            // additional note pane
            VBox additionalPane = new VBox();
            additionalPane.setPadding(new Insets(0, 10 ,10, 10));
            Label additionalLabel = new Label("Additional Note:");
            additionalLabel.setPadding(new Insets(0, 0, 5, 0));
            TextArea additionalText = new TextArea();
            additionalText.setText(selectedButton.getAdditional());
            additionalPane.getChildren().addAll(additionalLabel, additionalText);

            // reset button
            Button resetButton = new Button("Reset");
            resetButton.setPrefSize(65,40);
            resetButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    accountText.setText(selectedButton.getAccount());
                    idText.setText(selectedButton.getID());
                    pwText.setText(selectedButton.getPW());
                    additionalText.setText(selectedButton.getAdditional());
                }
            });
            // save button
            Button saveButton = new Button("Save");
            saveButton.setPrefSize(65,40);
            saveButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    // get modified texts
                    String newAccount = accountText.getText();
                    String newID = idText.getText();
                    String newPW = pwText.getText();
                    String newAdditional = additionalText.getText();

                    // create new AccountButton
                    String info = MessageFormat.format
                            ("{0}\nID: {1}\nPW: {2}", newAccount, newID, newPW);
                    AccountButton newAccountButton = new AccountButton(info, newAccount, newID, newPW, newAdditional);

                    // remove selected AccountButton from Accounts ArrayList
                    PW_Application.Accounts.remove(selectedButton);
                    // add modified AccountButton
                    PW_Application.Accounts.add(newAccountButton);
                    // clear accountList
                    accountList.getChildren().clear();
                    // sort accountList by account name
                    PW_Application.Accounts.sort((AccountButton a1, AccountButton a2) -> a1.getAccount().compareTo(a2.getAccount()));
                    // display the button in the window
                    for(int i = 0; i < PW_Application.Accounts.size(); i++){
                        accountList.getChildren().add(PW_Application.Accounts.get(i));
                    }

                    //close current stage
                    stage.close();
                }
            });
            // buttons
            FlowPane buttons = new FlowPane();
            buttons.setPadding(new Insets(0, 0, 0, 100));
            buttons.setHgap(10);
            buttons.getChildren().addAll(resetButton, saveButton);

            // Scene
            VBox main = new VBox();
            main.getChildren().addAll(accountFlow, idFlow, pwFlow, additionalPane, buttons);
            Scene scene = new Scene(main, 250, 390);

            // stage
            stage.setTitle("Edit");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setX(650);
            stage.setY(180);
            stage.show();
        }
        catch(Exception e){
            AlertErrorWindow("Account is not selected");
        }
    }

    public void clickSave(){

    }

    public void createPw(){
        String pw = createPwText.getText();
        String confirm = createPwConfirmText.getText();

        if (pw.equals(confirm)){
            // hashing pw and save
            BCryptPassword.hashPassword(pw);

            // close window
            Stage currentStage = (Stage) createPwText.getScene().getWindow();
            currentStage.close();
        }
        else{
            // error message
            AlertErrorWindow("Password Does Not Match");

            // reset the TextFields
            createPwText.clear();
            createPwConfirmText.clear();
            createPwText.requestFocus();
        }
    }

    public void checkPw() throws IOException, ParseException{

        if (new File("pw.json").isFile()){
            // compare input and hashedPw
            if (BCryptPassword.checkPw(checkPwText.getText())){
                PW_Application.pwAccepted = true;

                // close window
                Stage currentStage = (Stage) checkPwText.getScene().getWindow();
                currentStage.close();

            } else {
                // error message
                AlertErrorWindow("Password Does Not Match");
                // reset TextField
                checkPwText.clear();
                checkPwText.requestFocus();
            }
        }
    }

    public void openChangePwWindow() throws IOException {
        // Change Password window
        Stage changePwStage = new Stage();
        FXMLLoader pwChange = new FXMLLoader(PW_Application.class.getResource("pwChange.fxml"));
        Scene pwChangeScene = new Scene(pwChange.load());
        changePwStage.setTitle("Change Password");
        changePwStage.setScene(pwChangeScene);
        changePwStage.setResizable(false);
        changePwStage.showAndWait();
    }

    public void changePW() throws IOException, ParseException {
        String currentPW = changeCurrentPwText.getText();

        // check input currentPw is current hashedPw
        if (BCryptPassword.checkPw(currentPW)){
            String newPw = changeNewPwText.getText();
            String newPwConfirm = ChangeNewPwConfirmText.getText();
            // if newPW is confirmed
            if (newPw.equals(newPwConfirm)){
                // change pw
                BCryptPassword.changePw(newPw);

                // close all the windows
                Stage ChangePwWindow = (Stage) changeCurrentPwText.getScene().getWindow();
                ChangePwWindow.close();

                // success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Password Changed");
                alert.setHeaderText("Your Password Has Been Changed");
                alert.setContentText("Application will be closed");
                alert.showAndWait();

                // exit application
                System.exit(0);
            } else{
                // New Password Does Not Match error message
                AlertErrorWindow("New Password Does Not Match");

                // reset
                changeCurrentPwText.clear();
                changeNewPwText.clear();
                ChangeNewPwConfirmText.clear();
                changeNewPwText.requestFocus();

            }
        } else{
            // Current Password Does not Match error message
            AlertErrorWindow("Current Password Does Not Match");

            // reset
            changeCurrentPwText.clear();
            changeNewPwText.clear();
            ChangeNewPwConfirmText.clear();
            changeNewPwText.requestFocus();
        }
    }

    public static void AlertErrorWindow(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

}