package ru.topjava.voting;

import ru.topjava.voting.model.User;
import ru.topjava.voting.to.UserTo;
import ru.topjava.voting.util.UserUtil;

public class AuthUser extends org.springframework.security.core.userdetails.User{
private static final long serialVersionUID = 1L;

private UserTo userTo;

public AuthUser(User user) {
        super(user.getEmail(), user.getPassword(), user.getRoles());
        this.userTo = UserUtil.asTo(user);
        }

public int getId() {
        return userTo.getId();
        }

public void update(UserTo newTo) {
        userTo = newTo;
        }

public UserTo getUserTo() {
        return userTo;
        }

@Override
public String toString() {
        return userTo.toString();
        }

}