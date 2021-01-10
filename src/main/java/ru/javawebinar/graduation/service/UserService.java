package ru.javawebinar.graduation.service;

import ru.javawebinar.graduation.model.User;
import ru.javawebinar.graduation.to.UserTo;

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
