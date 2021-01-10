package ru.topjava.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.topjava.voting.AuthUser;
import ru.topjava.voting.model.User;
import ru.topjava.voting.repository.UserRepository;
import ru.topjava.voting.to.UserTo;
import ru.topjava.voting.util.UserUtil;
import ru.topjava.voting.util.ValidationUtil;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static ru.topjava.voting.util.ValidationUtil.checkNotFound;
import static ru.topjava.voting.util.ValidationUtil.checkNotFoundWithId;

@Service("userService")
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name", "email");

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User create(User user) {
        Assert.notNull(user, "user should not be null");
        return repository.save(UserUtil.prepareToSave(user, passwordEncoder));
    }

    @Override
    public void delete(int id) {
        ValidationUtil.checkNotFoundWithId(repository.delete(id), id);
    }

    @Override
    public User get(int id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("User is not found by id: " + id));
    }

    @Override
    public User getByEmail(String email) {
        Assert.notNull(email, "email should not be null");
        return ValidationUtil.checkNotFound(repository.getByEmail(email), "email=" + email);
    }

    @Override
    public void update(User user) {
        Assert.notNull(user, "user should not be null");
        ValidationUtil.checkNotFoundWithId(repository.save(UserUtil.prepareToSave(user, passwordEncoder)), user.getId());
    }

    @Transactional
    @Override
    public void update(UserTo userTo) {
        User user = UserUtil.updateFromTo(get(userTo.getId()), userTo);
        repository.save(UserUtil.prepareToSave(user, passwordEncoder));
    }

    @Override
    public List<User> getAll() {
        return repository.findAll(SORT_NAME);
    }

    @Override
    public AuthUser loadUserByUsername(String email) {
        User user = repository.getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthUser(user);
    }
}