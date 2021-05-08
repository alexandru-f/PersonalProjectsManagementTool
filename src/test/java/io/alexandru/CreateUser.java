package io.alexandru;

import io.alexandru.ppmtool.domain.User;

public class CreateUser {

    String username;
    String fullName;
    String password;

    public CreateUser(String username, String fullName, String password) {
        this.username = username;
        this.fullName = fullName;
        this.password = password;
    }

    public User create() {

        User user = new User();

        user.setUsername(username);
        user.setFullName(fullName);
        user.setPassword(password);

        return user;
    }
}
