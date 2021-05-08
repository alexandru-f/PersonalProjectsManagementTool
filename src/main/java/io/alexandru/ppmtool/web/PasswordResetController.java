package io.alexandru.ppmtool.web;
import io.alexandru.ppmtool.Validator.ResetPasswordValidator;
import io.alexandru.ppmtool.payload.PasswordResetToken;
import io.alexandru.ppmtool.payload.ResetPasswordRequest;
import io.alexandru.ppmtool.repositories.PasswordResetTokenRepository;
import io.alexandru.ppmtool.services.MapValidationErrorService;
import io.alexandru.ppmtool.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/users/resetPassword")
@CrossOrigin
public class PasswordResetController {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private MapValidationErrorService mapValidationErrorService;
    @Autowired
    private ResetPasswordValidator resetPasswordValidator;

    @GetMapping
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        PasswordResetToken resetToken = userService.checkIncomeToken(token);
        Map<String, String> responseOk = new HashMap<>();
        responseOk.put("token", resetToken.getToken());
        return new ResponseEntity<Map>(responseOk, HttpStatus.OK);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> handlePasswordReset(@RequestParam String token, @Valid @RequestBody ResetPasswordRequest resetPasswordRequest, BindingResult result) {

        resetPasswordValidator.validate(resetPasswordRequest, result);
        ResponseEntity<?> errorMap = mapValidationErrorService.validateMapError(result);
        if (errorMap != null) return errorMap;

        userService.resetPassword(token, resetPasswordRequest);

        return new ResponseEntity<String>("Your password has been changed", HttpStatus.OK);
    }
}
