package io.alexandru.ppmtool.Validator;

import io.alexandru.ppmtool.payload.ResetPasswordRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ResetPasswordValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return ResetPasswordRequest.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {

        ResetPasswordRequest resetPasswordRequest = (ResetPasswordRequest) object;

        if (resetPasswordRequest.getNewPassword().length() <= 6)
            errors.rejectValue("newPassword", "Length", "Password must be at least 6 characters long");

        if (!resetPasswordRequest.getNewPassword().equals(resetPasswordRequest.getConfirmNewPassword()))
            errors.rejectValue("confirmNewPassword", "Match", "Passwords must match");
    }
}
