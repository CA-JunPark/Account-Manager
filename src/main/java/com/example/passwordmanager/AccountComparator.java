package com.example.passwordmanager;

import java.util.Comparator;

public class AccountComparator implements Comparator<AccountButton>{

    @Override
    public int compare(AccountButton a, AccountButton b) {

        String account1 = a.getAccount();
        String account2 = b.getAccount();

        return account1.compareTo(account2);
    }
}
