package ru.javawebinar.graduation.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.graduation.TestUtil;
import ru.javawebinar.graduation.model.User;
import ru.javawebinar.graduation.service.UserService;
import ru.javawebinar.graduation.to.UserTo;
import ru.javawebinar.graduation.util.UserUtil;
import ru.javawebinar.graduation.web.AbstractControllerTest;
import ru.javawebinar.graduation.web.json.JsonUtil;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.graduation.TestUtil.readFromJson;
import static ru.javawebinar.graduation.TestUtil.userHttpBasic;
import static ru.javawebinar.graduation.UserTestData.*;
import static ru.javawebinar.graduation.UserTestData.USER;

class ProfileUserControllerTest extends AbstractControllerTest {
    private static final String REST_URL = ProfileUserController.REST_URL + "/";

    @Autowired
    private UserService userService;

    @Test
    void getUser() throws Exception {
        mockMvc.perform(get(REST_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertMatch(TestUtil.readFromJsonMvcResult(result, User.class), USER));

    }

    @Test
    void getUnauth() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void update() throws Exception {
        UserTo updated = getUpdated();

        mockMvc.perform(put(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(USER))
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertMatch(userService.getByEmail("updated@mail.ru"), UserUtil.updateFromTo(new User(USER), updated));
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL)
                .with(userHttpBasic(USER)))
                .andDo(print());
        assertMatch(userService.getAll(), ADMIN, USER2);
    }

    @Test
    void register() throws Exception {
        UserTo createdTo = getCreated();

        ResultActions action = mockMvc.perform(post(REST_URL + "register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(createdTo)))
                .andExpect(status().isCreated());

        User returned = readFromJson(action, User.class);
        User created = UserUtil.createNewFromTo(createdTo);
        created.setId(returned.getId());

        assertMatch(userService.getByEmail("new@mail.ru"), created);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void registerDuplicate() throws Exception {
        UserTo createdDuplicate = getDuplicated();

        mockMvc.perform(post(REST_URL + "register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(createdDuplicate)))
                .andExpect(status().isConflict());
    }
}