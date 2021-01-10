package ru.topjava.voting.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.topjava.voting.TestUtil;
import ru.topjava.voting.model.Role;
import ru.topjava.voting.model.User;
import ru.topjava.voting.service.UserService;
import ru.topjava.voting.web.AbstractControllerTest;
import ru.topjava.voting.web.json.JsonUtil;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.topjava.voting.UserTestData.*;

class AdminUserControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AdminUserController.REST_URL + "/";

    @Autowired
    private UserService userService;

    @Test
    void getAll() throws Exception {
        mockMvc.perform(get(REST_URL)
                .with(TestUtil.userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(ADMIN, USER, USER2));
    }

    @Test
    void getUser() throws Exception {
        ResultActions action = mockMvc.perform(get(REST_URL + USER_ID)
                .with(TestUtil.userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        User returned = TestUtil.readFromJson(action, User.class);
        assertMatch(USER, returned);
    }

    @Test
    void getNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + 1)
                .with(TestUtil.userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.detail").value("User not found by id 1"));
    }

    @Test
    void getUnauth() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getByEmail() throws Exception {
        mockMvc.perform(get(REST_URL + "by?email=" + USER.getEmail())
                .with(TestUtil.userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(USER));
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + USER_ID)
                .with(TestUtil.userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(userService.getAll(), ADMIN, USER2);
    }

    @Test
    void createUser() throws Exception {
        User expected = new User(null, "New", "new@mail.ru", "newPass", Role.ROLE_USER);
        ResultActions action = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(TestUtil.userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isCreated());

        User returned = TestUtil.readFromJson(action, User.class);
        expected.setId(returned.getId());

        assertMatch(returned, expected);
        assertMatch(userService.getAll(), ADMIN, expected, USER, USER2);
    }

    @Test
    void createDuplicate() throws Exception {
        User created = USER;
        mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(TestUtil.userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(created)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.detail").value(created + " must be new (id=null)"));
    }

}