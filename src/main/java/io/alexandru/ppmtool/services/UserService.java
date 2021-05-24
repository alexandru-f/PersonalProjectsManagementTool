package io.alexandru.ppmtool.services;

import io.alexandru.ppmtool.domain.User;
import io.alexandru.ppmtool.exceptions.*;
import io.alexandru.ppmtool.payload.*;
import io.alexandru.ppmtool.repositories.PasswordResetTokenRepository;
import io.alexandru.ppmtool.repositories.UserRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class UserService {


    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final PasswordResetTokenRepository tokenRepository;

    private final EmailService emailService;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                       PasswordResetTokenRepository tokenRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
    }

    public User saveUser(User newUser) {

        try {
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            newUser.setConfirmPassword("");
            return userRepository.save(newUser);
        } catch (Exception ex) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
    }
    public void changePassword(ChangePasswordRequest changePasswordRequest, String username) {

        User user = userRepository.findByUsername(username);

        if (!bCryptPasswordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new PasswordsDoNotMatchException("Your old password doesn't match the one you provided");
        }

        user.setPassword(bCryptPasswordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);

    }

    public void forgotPassword(ResetPasswordEmail userEmail, BindingResult result, HttpServletRequest request) {

        User user = userRepository.findByUsername(userEmail.getEmail());

        if (user == null) {
            throw new UsernameNotFoundException("We could not find an account for that e-mail address");
        }

        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(30);

        if (tokenRepository.findByToken(token.getToken()) == null) {
            tokenRepository.save(token);
        }

        Mail mail = new Mail();
        mail.setFrom("no-reply@personal-management.com");
        mail.setTo(user.getUsername());
        mail.setSubject("Password reset request");
        Map<String, Object> model = new HashMap<>();
        model.put("token", token);
        model.put("user", user);
        model.put("signature", "https://personal-management.herokuapp.com/");
        String url = request.getScheme() + "://" + request.getServerName();
        model.put("resetUrl", url + "/resetPassword?token=" + token.getToken());
        mail.setModel(model);
        emailService.sendEmail(mail);
    }

    public PasswordResetToken checkIncomeToken(String token) {

        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        if (resetToken == null) {
            throw new TokenNotFoundException("Token is not valid, generate another token");
        } else if (resetToken.isExpired()) {
            throw new TokenExpiredException("Token has expired, please request a new password request");
        }
        return resetToken;
    }

    public void resetPassword(String token, ResetPasswordRequest providedPasswords) {

        PasswordResetToken validToken = checkIncomeToken(token);

        User user = validToken.getUser();

        if (user == null) {
            throw new UsernameNotFoundException("Token provided doesn't match any account");
        }

        user.setPassword(bCryptPasswordEncoder.encode(providedPasswords.getNewPassword()));
        userRepository.save(user);
        tokenRepository.deleteAllByUserId(user.getId());
    }

}
