package io.alexandru.ppmtool.Validator;

import io.alexandru.ppmtool.payload.ChangePasswordRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ChangePasswordValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return ChangePasswordRequest.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {
        ChangePasswordRequest changePasswordRequest = (ChangePasswordRequest) object;

        if (changePasswordRequest.getNewPassword().length() <= 6) {
            errors.rejectValue("newPassword", "Length", "New password must be at least 6 characters long");
        }

        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmNewPassword())) {
            errors.rejectValue("confirmNewPassword", "Match", "Passwords must match");
        }
    }
}
