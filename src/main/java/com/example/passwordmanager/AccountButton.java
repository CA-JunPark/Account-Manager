package com.example.passwordmanager;

import javafx.geometry.Insets;
import javafx.scene.control.Button;

import java.text.MessageFormat;

public class AccountButton extends Button {

    private String account;
    private String id;
    private String pw;
    private String additional;

    public AccountButton(String val1, String account, String id, String pw, String additional){
        super(val1);
        this.account = account;
        this.id = id;
        this.pw = pw;
        this.additional = additional;

        this.setPrefWidth((200));
        this.setMinHeight(75);
        this.setPadding(new Insets(10,0,10,0));
    }

    public String getAccount(){return this.account;}
    public String getID(){return this.id;}
    public String getPW(){return this.pw;}
    public String getAdditional(){return this.additional;}

    public void setAccount(String account){this.account = account;}
    public void setID(String id){this.id = id;}
    public void setPW(String pw){this.pw = pw;}
    public void setAdditional(String additional){this.additional = additional;}



    public String toString(){
        return MessageFormat.format
                ("{0} \nID: {1} \nPW: {2} \nAdditional Note:\n{3}",
                        this.account, this.id, this.pw, this.additional);
    }

}
