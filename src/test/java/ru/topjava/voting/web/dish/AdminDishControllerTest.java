package ru.topjava.voting.web.dish;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.topjava.voting.DishTestData;
import ru.topjava.voting.TestUtil;
import ru.topjava.voting.model.Dish;
import ru.topjava.voting.service.DishService;
import ru.topjava.voting.web.AbstractControllerTest;
import ru.topjava.voting.web.json.JsonUtil;
import ru.topjava.voting.RestaurantTestData;

import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.topjava.voting.DishTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static ru.topjava.voting.UserTestData.ADMIN;

class AdminDishControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AdminDishController.REST_URL + "/";

    @Autowired
    private DishService dishService;

    @Test
    void getAllByRest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + RestaurantTestData.RESTAURANT1_ID + "/dishes")
                .with(TestUtil.userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        assertMatch(dishService.getAll(RestaurantTestData.RESTAURANT1_ID), Arrays.asList(DishTestData.DISH1, DishTestData.DISH2, DishTestData.DISH3));
    }

    @Test
    void testGet() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + RestaurantTestData.RESTAURANT1_ID + "/dishes/" + DishTestData.DISH1_ID)
                .with(TestUtil.userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(DishTestData.DISH1, TestUtil.readFromJsonMvcResult(result, Dish.class)));
    }

    @Test
    void getNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + RestaurantTestData.RESTAURANT1_ID + "/dishes/" + 1)
                .with(TestUtil.userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.detail").value("Dish not found by id 1"));
    }

    @Test
    void getUnauth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + RestaurantTestData.RESTAURANT1_ID + "/dishes/" + DishTestData.DISH1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + RestaurantTestData.RESTAURANT1_ID + "/dishes/" + DishTestData.DISH1_ID)
                .with(TestUtil.userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent());
        DishTestData.assertMatch(dishService.getAll(RestaurantTestData.RESTAURANT1_ID), DishTestData.DISH2, DishTestData.DISH3);
    }

    @Test
    void update() throws Exception {
        Dish updated = DishTestData.getUpdated();
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + RestaurantTestData.RESTAURANT1_ID + "/dishes/" + DishTestData.DISH1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(TestUtil.userHttpBasic(ADMIN)))
                .andExpect(result -> assertEquals(updated, TestUtil.readFromJsonMvcResult(result, Dish.class)));
    }

    @Test
    void save() throws Exception {
        Dish created = DishTestData.getCreated();
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL + RestaurantTestData.RESTAURANT1_ID + "/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(created))
                .with(TestUtil.userHttpBasic(ADMIN)))
                .andExpect(status().isCreated());

        Dish returned = TestUtil.readFromJson(action, Dish.class);
        created.setId(returned.getId());

        DishTestData.assertMatch(dishService.getAll(RestaurantTestData.RESTAURANT1_ID), DishTestData.DISH1, DishTestData.DISH2, DishTestData.DISH3, created);
    }

    @Test
    void saveDuplicate() throws Exception {
        Dish created = DishTestData.DISH1;

        mockMvc.perform(MockMvcRequestBuilders.post(REST_URL + RestaurantTestData.RESTAURANT1_ID + "/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(created))
                .with(TestUtil.userHttpBasic(ADMIN)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.detail").value(created + " must be new (id=null)"));
    }
}
