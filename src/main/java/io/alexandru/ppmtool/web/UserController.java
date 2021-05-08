package io.alexandru.ppmtool.web;


import io.alexandru.ppmtool.Security.JwtTokenProvider;
import io.alexandru.ppmtool.Validator.ChangePasswordValidator;
import io.alexandru.ppmtool.Validator.UserValidator;
import io.alexandru.ppmtool.domain.User;
import io.alexandru.ppmtool.payload.ChangePasswordRequest;
import io.alexandru.ppmtool.payload.JWTLoginSuccessReponse;
import io.alexandru.ppmtool.payload.LoginRequest;
import io.alexandru.ppmtool.payload.ResetPasswordEmail;
import io.alexandru.ppmtool.services.MapValidationErrorService;
import io.alexandru.ppmtool.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.security.Principal;

import static io.alexandru.ppmtool.Security.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ChangePasswordValidator changePasswordValidator;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult result) {

        ResponseEntity<?> errorMap = mapValidationErrorService.validateMapError(result);
        if (errorMap != null) return errorMap;

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = TOKEN_PREFIX + tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JWTLoginSuccessReponse(true, jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result) {

        //Validate passwords
        userValidator.validate(user, result);
        ResponseEntity<?> errorMap = mapValidationErrorService.validateMapError(result);

        if (errorMap != null) return errorMap;
        User user1 = userService.saveUser(user);
        return new ResponseEntity<User>(user1, HttpStatus.CREATED);
    }

    @PostMapping("/updatePassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest,
                                            BindingResult result, Principal principal) {

        changePasswordValidator.validate(changePasswordRequest,result);
        ResponseEntity<?> errorMap = mapValidationErrorService.validateMapError(result);

        if (errorMap != null) return errorMap;

        userService.changePassword(changePasswordRequest, principal.getName());
        return new ResponseEntity<String>("Password changed successfully", HttpStatus.OK);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPasswordProcess(@Valid @RequestBody ResetPasswordEmail email, BindingResult result, HttpServletRequest request) {
        ResponseEntity<?> errorMap = mapValidationErrorService.validateMapError(result);

        if (errorMap != null) return errorMap;

        userService.forgotPassword(email, result, request);
        return new ResponseEntity<String>("Your password change request has been successfully! Check your email!", HttpStatus.OK);
    }
}
