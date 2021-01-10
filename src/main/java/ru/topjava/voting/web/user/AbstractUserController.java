package ru.topjava.voting.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.topjava.voting.model.User;
import ru.topjava.voting.service.UserService;
import ru.topjava.voting.to.UserTo;
import ru.topjava.voting.util.UserUtil;

import java.util.List;

import static ru.topjava.voting.util.ValidationUtil.assureIdConsistent;
import static ru.topjava.voting.util.ValidationUtil.checkNew;


public abstract class AbstractUserController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected UserService service;

    public List<User> getAll() {
        log.info("getAll");
        return service.getAll();
    }

    public User get(int id) {
        log.info("get {}", id);
        return service.get(id);
    }

    public User create(UserTo userTo) {
        return create(UserUtil.createNewFromTo(userTo));
    }

    public User create(User user) {
        log.info("create {}", user);
        checkNew(user);
        return service.create(user);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id);
    }

    public void update(User user, int id) {
        log.info("update {} with id={}", user, id);
        assureIdConsistent(user, id);
        service.update(user);
    }

    public void update(UserTo userTo, int id) {
        log.info("update {} with id={}", userTo, id);
        assureIdConsistent(userTo, id);
        service.update(userTo);
    }

    User getByEmail(String email) {
        log.info("getByEmail {}", email);
        return service.getByEmail(email);
    }
}