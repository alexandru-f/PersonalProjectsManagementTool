package io.alexandru.ppmtool.exceptions;

public class PasswordsDoNotMatchResponse {

    private String passwordsDoNotMatch;


    public PasswordsDoNotMatchResponse(String passwordsDoNotMatch) {
        this.passwordsDoNotMatch = passwordsDoNotMatch;
    }

    public String getPasswordsDoNotMatch() {
        return passwordsDoNotMatch;
    }

    public void setPasswordsDoNotMatch(String passwordsDoNotMatch) {
        this.passwordsDoNotMatch = passwordsDoNotMatch;
    }
}
