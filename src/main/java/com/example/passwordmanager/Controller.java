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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Comparator;


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


    /** Main */
    public void refreshAccountList() throws IOException {
        // update accountsJson
        updateAccountsJson();

        if (accountList == null){
            return;
        }

        // adjust accountList Height
        int listLength = 0;
        for (AccountButton b : PW_Application.Accounts){
            listLength += b.getHeight();
        }
        accountList.setPrefHeight(listLength);

        // clear accountList
        accountList.getChildren().clear();

        // sort accountList by account name
        PW_Application.Accounts.sort(Comparator.comparing(AccountButton::getAccount));

        // display the button in the window
        for(int i = 0; i < PW_Application.Accounts.size(); i++){
            accountList.getChildren().add(PW_Application.Accounts.get(i));
        }
    }

    public void addAccount() throws IOException {

        String account = accountText.getText();
        String id = idText.getText();
        String pw = pwText.getText();
        String additional = additionalText.getText();

        // create a Button
        String info = MessageFormat.format
                ("{0}\nID: {1}\nPW: {2}", account, id, pw);
        AccountButton accountButton = new AccountButton(info, account, id, pw, additional);

        // add action event on click
        accountButton.setOnAction(this::clickAccount);

        // add button in the Accounts ArrayList
        PW_Application.Accounts.add(accountButton);

        // refresh accountList
        refreshAccountList();

        // focus on the newest button
        accountButton.requestFocus();
    }

    public void deleteAccount() throws IOException {
        // identify the AccountButton
        AccountButton selectedButton = (AccountButton) PW_Application.scene.getFocusOwner();

        // remove identified AccountButton from Accounts ArrayList
        PW_Application.Accounts.remove(selectedButton);

        // refresh accountList;
        refreshAccountList();
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
            TextField editAccountText = new TextField();
            editAccountText.setText(selectedButton.getAccount());
            accountFlow.getChildren().addAll(accountLabel, editAccountText);

            // id pane
            FlowPane idFlow = new FlowPane();
            idFlow.setPadding(new Insets(10, 0 ,10, 10));
            idFlow.setHgap(5);
            Label idLabel = new Label("ID:");
            idLabel.setAlignment(Pos.CENTER_LEFT);
            idLabel.setPrefWidth(50);
            TextField editIdText = new TextField();
            editIdText.setText(selectedButton.getID());
            idFlow.getChildren().addAll(idLabel, editIdText);

            // pw pane
            FlowPane pwFlow = new FlowPane();
            pwFlow.setPadding(new Insets(10, 0 ,10, 10));
            pwFlow.setHgap(5);
            Label pwLabel = new Label("PW:");
            pwLabel.setAlignment(Pos.CENTER_LEFT);
            pwLabel.setPrefWidth(50);
            TextField editPwText = new TextField();
            editPwText.setText(selectedButton.getPW());
            pwFlow.getChildren().addAll(pwLabel, editPwText);

            // additional note pane
            VBox additionalPane = new VBox();
            additionalPane.setPadding(new Insets(0, 10 ,10, 10));
            Label additionalLabel = new Label("Additional Note:");
            additionalLabel.setPadding(new Insets(0, 0, 5, 0));
            TextArea editAdditionalText = new TextArea();
            editAdditionalText.setText(selectedButton.getAdditional());
            additionalPane.getChildren().addAll(additionalLabel, editAdditionalText);

            // reset button
            Button resetButton = new Button("Reset");
            resetButton.setPrefSize(65,40);
            resetButton.setOnAction(actionEvent -> {
                editAccountText.setText(selectedButton.getAccount());
                editIdText.setText(selectedButton.getID());
                editPwText.setText(selectedButton.getPW());
                editAdditionalText.setText(selectedButton.getAdditional());
            });

            // save button
            Button saveButton = new Button("Save");
            saveButton.setPrefSize(65,40);
            saveButton.setOnAction(actionEvent -> {
                // get modified texts
                String newAccount = editAccountText.getText();
                String newID = editIdText.getText();
                String newPW = editPwText.getText();
                String newAdditional = editAdditionalText.getText();

                // create new AccountButton
                String info = MessageFormat.format
                        ("{0}\nID: {1}\nPW: {2}", newAccount, newID, newPW);
                AccountButton newAccountButton = new AccountButton(info, newAccount, newID, newPW, newAdditional);
                newAccountButton.setOnAction(event -> {
                    // identify the selected AccountButton
                    AccountButton newSelectedButton = (AccountButton) event.getSource();

                    // set the TextFields from the selectedButton
                    accountText.setText(newSelectedButton.getAccount());
                    idText.setText(newSelectedButton.getID());
                    pwText.setText(newSelectedButton.getPW());
                    additionalText.setText(newSelectedButton.getAdditional());
                });

                // remove selected AccountButton from Accounts ArrayList
                PW_Application.Accounts.remove(selectedButton);
                // add modified AccountButton
                PW_Application.Accounts.add(newAccountButton);
                // clear accountList
                accountList.getChildren().clear();
                // sort accountList by account name
                PW_Application.Accounts.sort(Comparator.comparing(AccountButton::getAccount));
                // display the button in the window
                for(int i = 0; i < PW_Application.Accounts.size(); i++){
                    accountList.getChildren().add(PW_Application.Accounts.get(i));
                }

                //close current stage
                stage.close();
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

    public void clickSave() throws IOException {
        FileChooser filechooser = new FileChooser();
        filechooser.setTitle("Please locate the directory of the file");
        filechooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        filechooser.setInitialFileName("accounts.txt");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT", "*.txt");
        filechooser.getExtensionFilters().add(extFilter);
        File selectedFile = filechooser.showSaveDialog(PW_Application.scene.getWindow());

        if (selectedFile != null){
            FileWriter writer = new FileWriter(selectedFile);
            for (int i = 0; i < PW_Application.Accounts.size(); i++){
                writer.write(PW_Application.Accounts.get(i).toString());
            }
            writer.flush();
            writer.close();
        }
    }

    public void clickOpenTxt(){

    }

    public void clickClose(){
        System.exit(0);
    }

    /** Password */
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

    /** JSON */
    public void readAccountsJson() throws IOException, ParseException {
        // read it if file exists
        if (new File("accounts.json").isFile()){
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader("accounts.json");
            Object obj = parser.parse(reader);
            PW_Application.AccountsJson = (JSONArray) obj;

            PW_Application.AccountsJson.forEach(accountButton -> parseAccountButton((JSONObject) accountButton));
        }
    }

    private void parseAccountButton(JSONObject accountButtonJson){
        JSONObject accountButtonObject = (JSONObject) accountButtonJson.get("accountButton");
        String account = (String) accountButtonObject.get("account");
        String id = (String) accountButtonObject.get("id");
        String pw = (String) accountButtonObject.get("pw");
        String additional = (String) accountButtonObject.get("additional");

        // create a Button
        String info = MessageFormat.format
                ("{0}\nID: {1}\nPW: {2}", account, id, pw);
        AccountButton accountButton = new AccountButton(info, account, id, pw, additional);
        accountButton.setOnAction(event -> {
            // identify the selected AccountButton
            AccountButton selectedButton = (AccountButton) event.getSource();

            // set the TextFields from the selectedButton
            accountText.setText(selectedButton.getAccount());
            idText.setText(selectedButton.getID());
            pwText.setText(selectedButton.getPW());
            additionalText.setText(selectedButton.getAdditional());
        });

        // add it into Accounts (ArrayList)
        PW_Application.Accounts.add(accountButton);
    }

    public void updateAccountsJson() throws IOException {
        PW_Application.AccountsJson.clear();

        // iterate Accounts(ArrayList) to create JSONArray
        PW_Application.Accounts.forEach(this::putAccountButton);

        FileWriter writer = new FileWriter("accounts.json");
        writer.write(PW_Application.AccountsJson.toJSONString());
        writer.flush();
        writer.close();
    }

    private void putAccountButton(AccountButton accountButton) {
        JSONObject accountButtonJsonDetail = new JSONObject();
        accountButtonJsonDetail.put("account", accountButton.getAccount());
        accountButtonJsonDetail.put("id", accountButton.getID());
        accountButtonJsonDetail.put("pw", accountButton.getPW());
        accountButtonJsonDetail.put("additional", accountButton.getAdditional());

        JSONObject accountButtonJson = new JSONObject();
        accountButtonJson.put("accountButton", accountButtonJsonDetail);

        PW_Application.AccountsJson.add(accountButtonJson);
    }
}