package io.alexandru.ppmtool.exceptions;

public class UsernameAlreadyExistsResponse {

    private String usernameAlreadyExists;

    public UsernameAlreadyExistsResponse(String usernameAlreadyExists) {
        this.usernameAlreadyExists = usernameAlreadyExists;
    }

    public String getUsernameAlreadyExists() {
        return usernameAlreadyExists;
    }

    public void setUsernameAlreadyExists(String usernameAlreadyExists) {
        this.usernameAlreadyExists = usernameAlreadyExists;
    }
}
