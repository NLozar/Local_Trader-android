package com.example.localtrader;

public class ProfileEditRequestStatus {

    protected boolean usernameTaken;
    protected boolean wrongPassword;
    protected boolean success;

    public ProfileEditRequestStatus(boolean success) {
        this.success = success;
    }

    public void setUsernameTaken(boolean usernameTaken) {
        this.usernameTaken = usernameTaken;
    }

    public void setWrongPassword(boolean wrongPassword) {
        this.wrongPassword = wrongPassword;
    }
}
