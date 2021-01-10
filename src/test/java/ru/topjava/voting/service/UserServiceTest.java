package ru.topjava.voting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.topjava.voting.model.Role;
import ru.topjava.voting.model.User;

import java.util.Collections;
import java.util.List;

import static ru.topjava.voting.UserTestData.*;

public class UserServiceTest extends AbstractServiceTest {

    @Autowired
    protected UserService service;

    @Test
    void create() throws Exception {
        User newUser = new User(null, "New", "mail@mail.ru", "newPass", Collections.singleton(Role.ROLE_USER));
        User created = service.create(new User(newUser));
        newUser.setId(created.getId());
        assertMatch(newUser, created);
    }

    @Test
    void delete() throws Exception {
        service.delete(USER_ID);
        assertMatch(service.getAll(), ADMIN, USER2);
    }

    @Test
    void get() throws Exception {
        User user = service.get(USER_ID);
        assertMatch(user, USER);
    }

    @Test
    void getByEmail() throws Exception {
        User user = service.getByEmail("user@mail.ru");
        assertMatch(user, USER);
        User admin = service.getByEmail("admin@mail.ru");
        assertMatch(admin, ADMIN);
    }

    @Test
    void update() throws Exception {
        User updated = new User(USER);
        updated.setName("UpdatedName");
        updated.setRoles(Collections.singletonList(Role.ROLE_ADMIN));
        service.update(updated);
        assertMatch(service.get(USER_ID), updated);
    }

    @Test
    void getAll() throws Exception {
        List<User> all = service.getAll();
        assertMatch(all, ADMIN, USER, USER2);
    }
}
