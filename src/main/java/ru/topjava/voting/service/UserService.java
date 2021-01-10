package ru.topjava.voting.service;

import ru.topjava.voting.model.User;
import ru.topjava.voting.to.UserTo;

import java.util.List;

public interface UserService {

    User create(User user);

    void delete(int id);

    User get(int id);

    User getByEmail(String email);

    void update(User user);

    void update(UserTo user);

    List<User> getAll();
}
