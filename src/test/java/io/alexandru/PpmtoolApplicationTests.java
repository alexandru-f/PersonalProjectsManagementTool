package io.alexandru;
import io.alexandru.ppmtool.domain.User;
import io.alexandru.ppmtool.exceptions.PasswordsDoNotMatchException;
import io.alexandru.ppmtool.exceptions.UsernameAlreadyExistsException;
import io.alexandru.ppmtool.payload.ChangePasswordRequest;
import io.alexandru.ppmtool.repositories.PasswordResetTokenRepository;
import io.alexandru.ppmtool.repositories.UserRepository;
import io.alexandru.ppmtool.services.EmailService;
import io.alexandru.ppmtool.services.UserService;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.validation.*;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
@RunWith(MockitoJUnitRunner.class)
public class PpmtoolApplicationTests {


    @Mock
    private static UserRepository mockedUserRepository;

    @Mock
    private static PasswordResetTokenRepository tokenRepository;

    @Mock
    private static EmailService emailService;

    @Mock
    private static ChangePasswordRequest changePasswordRequest;

    @Mock
    private static BCryptPasswordEncoder bCryptPasswordEncoder;

    private static UserService userService;

    private static ValidatorFactory validatorFactory;

    private static Validator validator;

    @Before
    public void setUps() {
        userService = new UserService(mockedUserRepository, bCryptPasswordEncoder, tokenRepository, emailService);
    }

    @BeforeClass
    public static void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterClass
    public static void closeUp() {
        validatorFactory.close();
    }

    @Test
    public void should_return_user_when_saved() {
        User user = new CreateUser("alexandru@gmail.com", "alexandru", "password").create();

        //Preparation / Stub
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        when(mockedUserRepository.save(captor.capture())).thenReturn(user);
        when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");

        //Action
        User created = userService.saveUser(user);

        //Assertions
        verify(bCryptPasswordEncoder, times(1)).encode("password");
        assertThat(captor.getValue().getPassword()).isEqualTo("encodedPassword");
        assertThat(user.getUsername()).isEqualTo(created.getUsername());
        assertThat(created).isEqualTo(user);
    }

    @Test
    public void should_throw_exception_if_username_exists() throws Exception {
        User user = new User();

        //Preparation / Stub
        when(mockedUserRepository.save(user)).thenThrow(DuplicateKeyException.class);

        //Action
        try {
            userService.saveUser(user);
            fail("This should have failed");
        } catch (UsernameAlreadyExistsException ex) {
            //Assertions
            assertNotNull(ex);
            assertEquals("Username already exists", ex.getMessage());
        }
    }

    @Test
    public void should_change_password() throws Exception {
        User user = new CreateUser("alexandru@gmail.com", "alexandru", "password").create();

        //Preparation / Stub
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        when(mockedUserRepository.findByUsername("alexandru@gmail.com")).thenReturn(user);
        when(changePasswordRequest.getOldPassword()).thenReturn("password");
        when(changePasswordRequest.getNewPassword()).thenReturn("newPassword");
        when(bCryptPasswordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())).thenReturn(true);
        when(bCryptPasswordEncoder.encode(changePasswordRequest.getNewPassword())).thenReturn("encodedPassword");
        when(mockedUserRepository.save(captor.capture())).thenReturn(user);

        //Action
        userService.changePassword(changePasswordRequest, "alexandru@gmail.com");

        //Assertions
        verify(bCryptPasswordEncoder, times(1)).matches("password", "password");
        verify(bCryptPasswordEncoder, times(1)).encode("newPassword");
        assertThat(captor.getValue().getPassword()).isEqualTo("encodedPassword");

    }

    @Test
    public void should_throw_exception_if_passwords_do_not_match() throws Exception {
        User user = new CreateUser("alexandru@gmail.com", "alexandru", "password").create();

        //Preparation
        when(mockedUserRepository.findByUsername("alexandru@gmail.com")).thenReturn(user);
        when(changePasswordRequest.getOldPassword()).thenReturn("wrongOldPass");
        when(bCryptPasswordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())).thenReturn(false);

        //Action
        try {
            userService.changePassword(changePasswordRequest, "alexandru@gmail.com");
            fail("This should have failed");
        } catch (PasswordsDoNotMatchException ex) {
            assertNotNull(ex);
            assertEquals("Your old password doesn't match the one you provided", ex.getMessage());
        }


    }


    /* ---------- Bean Validation tests ---------- */

    @Test
    public void should_detect_empty_username() {

        Set<ConstraintViolation<User>> violations = validator.validate(
                new CreateUser("", "alexandru", "alexandru").create());

        assertEquals(violations.size(), 1);

        ConstraintViolation<User> violation = violations.iterator().next();

        assertEquals("Username is required", violation.getMessage());
    }

    @Test
    public void should_detect_invalid_username() {

        Set<ConstraintViolation<User>> violations = validator.validate(
                new CreateUser("invalidusername", "alexandru", "password").create());

        assertEquals(violations.size(), 1);

        ConstraintViolation<User> violation = violations.iterator().next();

        assertEquals("Username needs to be an email", violation.getMessage());

    }

    @Test
    public void should_detect_empty_fullName() {

        Set<ConstraintViolation<User>> violations = validator.validate(
                new CreateUser("alexandru@alexandru.com", "", "password").create());

        assertEquals(violations.size(), 1);

        ConstraintViolation<User> violation = violations.iterator().next();

        assertEquals("Please enter your full name", violation.getMessage());

    }

    @Test
    public void should_detect_empty_password() {

        Set<ConstraintViolation<User>> violations = validator.validate(
                new CreateUser("alexandru@alexandru.com", "fullname", "").create());

        assertEquals(violations.size(), 1);

        ConstraintViolation<User> violation = violations.iterator().next();

        assertEquals("Password field is required", violation.getMessage());

    }
}

