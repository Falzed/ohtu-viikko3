package ohtu.services;

import ohtu.domain.User;
import java.util.ArrayList;
import java.util.List;
import ohtu.data_access.UserDao;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class AuthenticationService {

    private UserDao userDao;

    @Autowired
    public AuthenticationService(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean logIn(String username, String password) {
        for (User user : userDao.listAll()) {
            if (user.getUsername().equals(username))  {
                return user.getPassword().equals(password);
            }
        }

        return false;
    }

    public boolean createUser(String username, String password) {
        if (userDao.findByName(username) != null || invalid(username,password)) {
            return false;
        }
        userDao.add(new User(username, password));
        return true;
    }

    private boolean invalid(String username, String password) {
        // validity check of username and password
        if (usernameInvalid(username)) {
            return true;
        }
        return passwordInvalid(password);
    }
    private boolean usernameInvalid(String username) {
        return (username.length() < 3);
    }
    private boolean passwordInvalid(String password) {
        return (password.matches("[a-zA-Z]*") || password.length() < 8);
    }
}
