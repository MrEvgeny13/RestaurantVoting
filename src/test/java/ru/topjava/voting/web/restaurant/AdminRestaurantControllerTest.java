package ru.topjava.voting.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.topjava.voting.TestUtil;
import ru.topjava.voting.model.Restaurant;
import ru.topjava.voting.service.RestaurantService;
import ru.topjava.voting.web.AbstractControllerTest;
import ru.topjava.voting.web.json.JsonUtil;
import ru.topjava.voting.RestaurantTestData;
import ru.topjava.voting.UserTestData;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.topjava.voting.RestaurantTestData.*;

class AdminRestaurantControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AdminRestaurantController.REST_URL + "/";

    @Autowired
    private RestaurantService restaurantService;

    @Test
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .with(TestUtil.userHttpBasic(UserTestData.ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        assertMatch(restaurantService.getAllWithoutDish(), Arrays.asList(RestaurantTestData.RESTAURANT1, RestaurantTestData.RESTAURANT2, RestaurantTestData.RESTAURANT3));
    }

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + RestaurantTestData.RESTAURANT1_ID)
                .with(TestUtil.userHttpBasic(UserTestData.ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(TestUtil.readFromJsonMvcResult(result, Restaurant.class), RestaurantTestData.RESTAURANT1));
    }

    @Test
    void getUnauth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + RestaurantTestData.RESTAURANT1_ID))
                .andExpect(status().isUnauthorized());
    }

    // https://stackoverflow.com/questions/18336277/how-to-check-string-in-response-body-with-mockmvc
    @Test
    void getNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + 1)
                .with(TestUtil.userHttpBasic(UserTestData.ADMIN)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.detail").value("Restaurant not found by id 1"));
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + RestaurantTestData.RESTAURANT1_ID)
                .with(TestUtil.userHttpBasic(UserTestData.ADMIN)))
                .andExpect(status().isNoContent());

        assertMatch(restaurantService.getAllWithoutDish(), Arrays.asList(RestaurantTestData.RESTAURANT2, RestaurantTestData.RESTAURANT3));
    }

    @Test
    void update() throws Exception {
        Restaurant updated = RestaurantTestData.getUpdated();
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + RestaurantTestData.RESTAURANT1_ID)
                .with(TestUtil.userHttpBasic(UserTestData.ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(result -> assertEquals(TestUtil.readFromJsonMvcResult(result, Restaurant.class), RestaurantTestData.RESTAURANT1));
    }

    @Test
    void create() throws Exception {
        Restaurant created = RestaurantTestData.getCreated();

        ResultActions action = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(created))
                .with(TestUtil.userHttpBasic(UserTestData.ADMIN)));

        Restaurant returned = TestUtil.readFromJson(action, Restaurant.class);
        created.setId(returned.getId());

        assertMatch(restaurantService.getAllWithoutDish(), Arrays.asList(RestaurantTestData.RESTAURANT1, RestaurantTestData.RESTAURANT2, RestaurantTestData.RESTAURANT3, created));
    }

    @Test
    void createDuplicate() throws Exception {
        Restaurant created = RestaurantTestData.RESTAURANT1;

        mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(created))
                .with(TestUtil.userHttpBasic(UserTestData.ADMIN)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.detail").value(created + " must be new (id=null)"));
    }

}