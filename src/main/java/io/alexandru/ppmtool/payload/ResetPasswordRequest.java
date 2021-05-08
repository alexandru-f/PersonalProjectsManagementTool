package io.alexandru.ppmtool.payload;

import javax.validation.constraints.NotBlank;

public class ResetPasswordRequest {

    @NotBlank(message = "New password is required")
    private String newPassword;

    @NotBlank(message = "Confirmation password is required")
    private String confirmNewPassword;


    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }
}
