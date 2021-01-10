package ru.topjava.voting;

import org.springframework.test.web.servlet.ResultMatcher;
import ru.topjava.voting.model.Role;
import ru.topjava.voting.model.User;
import ru.topjava.voting.to.UserTo;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.topjava.voting.model.AbstractBaseEntity.START_SEQ;

public class UserTestData {
    public static final int USER_ID = START_SEQ;
    private static final int ADMIN_ID = START_SEQ + 1;
    private static final int USER2_ID = START_SEQ + 2;

    public static final User USER = new User(USER_ID, "User", "user@mail.ru", "password", Role.ROLE_USER);
    public static final User ADMIN = new User(ADMIN_ID, "Admin", "admin@mail.ru", "password", Role.ROLE_ADMIN, Role.ROLE_USER);
    public static final User USER2 = new User(USER2_ID, "User2", "user2@mail.ru", "password", Role.ROLE_USER);

    public static UserTo getCreated() {
        return new UserTo(null, "New", "new@mail.ru", "newpass");
    }

    public static UserTo getUpdated() {
        return new UserTo(null, "Updated", "updated@mail.ru", "newpass");
    }

    public static UserTo getDuplicated() {
        return new UserTo(null, "New", "user@mail.ru", "password");
    }

    public static void assertMatch(User actual, User expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "password");
    }

    public static void assertMatch(Iterable<User> actual, User... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<User> actual, Iterable<User> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("password").isEqualTo(expected);
    }

    public static ResultMatcher contentJson(User... expected) {
        return result -> assertMatch(TestUtil.readListFromJsonMvcResult(result, User.class), List.of(expected));
    }

}
