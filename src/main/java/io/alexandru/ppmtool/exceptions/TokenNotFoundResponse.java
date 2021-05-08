package io.alexandru.ppmtool.exceptions;

public class TokenNotFoundResponse {

    private String tokenNotFound;

    public TokenNotFoundResponse(String tokenNotFound) {
        this.tokenNotFound = tokenNotFound;
    }

    public String getTokenNotFound() {
        return tokenNotFound;
    }

    public void setTokenNotFound(String tokenNotFound) {
        this.tokenNotFound = tokenNotFound;
    }
}
