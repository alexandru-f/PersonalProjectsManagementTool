package io.alexandru.ppmtool.exceptions;

public class TokenExpiredResponse {
    private String tokenExpired;

    public TokenExpiredResponse(String tokenExpired) {
        this.tokenExpired = tokenExpired;
    }

    public String getTokenExpired() {
        return tokenExpired;
    }

    public void setTokenExpired(String tokenExpired) {
        this.tokenExpired = tokenExpired;
    }
}
