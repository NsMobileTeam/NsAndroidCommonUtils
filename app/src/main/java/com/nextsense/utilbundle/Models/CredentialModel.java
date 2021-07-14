package com.nextsense.utilbundle.Models;

public class CredentialModel {
    public String username;
    public String password;
    public String pin;

    public CredentialModel(String username, String password, String pin) {
        this.username = username;
        this.password = password;
        this.pin = pin;
    }
}
