package io.alexandru.ppmtool.exceptions;

public class UsernameNotFoundExceptionResponse {
    private String usernameNotFound;

    public UsernameNotFoundExceptionResponse(String usernameNotFound) {
        this.usernameNotFound = usernameNotFound;
    }

    public String getUsernameNotFound() {
        return usernameNotFound;
    }

    public void setUsernameNotFound(String usernameNotFound) {
        this.usernameNotFound = usernameNotFound;
    }
}
