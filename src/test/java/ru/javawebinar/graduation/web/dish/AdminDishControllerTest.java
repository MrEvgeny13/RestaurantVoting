package ru.javawebinar.graduation.web.dish;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.javawebinar.graduation.DishTestData;
import ru.javawebinar.graduation.TestUtil;
import ru.javawebinar.graduation.model.Dish;
import ru.javawebinar.graduation.service.DishService;
import ru.javawebinar.graduation.web.AbstractControllerTest;
import ru.javawebinar.graduation.web.json.JsonUtil;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.graduation.DishTestData.*;
import static ru.javawebinar.graduation.TestUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static ru.javawebinar.graduation.TestUtil.userHttpBasic;
import static ru.javawebinar.graduation.UserTestData.ADMIN;
import static ru.javawebinar.graduation.RestaurantTestData.*;

class AdminDishControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AdminDishController.REST_URL + "/";

    @Autowired
    private DishService dishService;

    @Test
    void getAllByRest() throws Exception {
        mockMvc.perform(get(REST_URL + RESTAURANT1_ID + "/dishes")
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        assertMatch(dishService.getAll(RESTAURANT1_ID), Arrays.asList(DISH1, DISH2, DISH3));
    }

    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + RESTAURANT1_ID + "/dishes/" + DISH1_ID)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(DISH1, readFromJsonMvcResult(result, Dish.class)));
    }

    @Test
    void getNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID + "/dishes/" + 1)
                .with(TestUtil.userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.detail").value("Dish not found by id 1"));
    }

    @Test
    void getUnauth() throws Exception {
        mockMvc.perform(get(REST_URL + RESTAURANT1_ID + "/dishes/" + DISH1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + RESTAURANT1_ID + "/dishes/" + DISH1_ID)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent());
        assertMatch(dishService.getAll(RESTAURANT1_ID), DISH2, DISH3);
    }

    @Test
    void update() throws Exception {
        Dish updated = DishTestData.getUpdated();
        mockMvc.perform(put(REST_URL + RESTAURANT1_ID + "/dishes/" + DISH1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(ADMIN)))
                .andExpect(result -> assertEquals(updated, readFromJsonMvcResult(result, Dish.class)));
    }

    @Test
    void save() throws Exception {
        Dish created = DishTestData.getCreated();
        ResultActions action = mockMvc.perform(post(REST_URL + RESTAURANT1_ID + "/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(created))
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isCreated());

        Dish returned = TestUtil.readFromJson(action, Dish.class);
        created.setId(returned.getId());

        assertMatch(dishService.getAll(RESTAURANT1_ID), DISH1, DISH2, DISH3, created);
    }

    @Test
    void saveDuplicate() throws Exception {
        Dish created = DISH1;

        mockMvc.perform(post(REST_URL + RESTAURANT1_ID + "/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(created))
                .with(userHttpBasic(ADMIN)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.detail").value(created + " must be new (id=null)"));
    }
}
