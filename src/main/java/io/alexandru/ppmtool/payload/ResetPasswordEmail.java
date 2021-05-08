package io.alexandru.ppmtool.payload;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

public class ResetPasswordEmail {

    @NotBlank(message = "Email cannot be blank")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
