package ru.topjava.voting.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.topjava.voting.service.RestaurantService;
import ru.topjava.voting.web.AbstractControllerTest;
import ru.topjava.voting.RestaurantTestData;
import ru.topjava.voting.TestUtil;
import ru.topjava.voting.UserTestData;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;

import static java.time.LocalDate.of;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.topjava.voting.RestaurantTestData.*;

class ProfileRestaurantControllerTest extends AbstractControllerTest {
    private static final String REST_URL = ProfileRestaurantController.REST_URL + "/";

    @Autowired
    private RestaurantService restaurantService;

    @Test
    void getAllByDate() throws Exception{
        mockMvc.perform(get(REST_URL + "?date=2019-11-23")
                .with(TestUtil.userHttpBasic(UserTestData.USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        assertMatch(restaurantService.getAll(of(2019, Month.NOVEMBER, 23)), Arrays.asList(RestaurantTestData.RESTAURANT1));
    }

    @Test
    void getAllByIdAndByDate() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + RestaurantTestData.RESTAURANT1_ID  + "?date=2019-11-23")
                .with(TestUtil.userHttpBasic(UserTestData.USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        assertMatch(restaurantService.getAllForId(of(2019, Month.NOVEMBER, 23), RestaurantTestData.RESTAURANT1_ID), Arrays.asList(RestaurantTestData.RESTAURANT1));
    }

    @Test
    void getAllByToday() throws Exception{
        mockMvc.perform(get(REST_URL)
                .with(TestUtil.userHttpBasic(UserTestData.USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        assertMatch(restaurantService.getAll(LocalDate.now()), Arrays.asList(RestaurantTestData.RESTAURANT2, RestaurantTestData.RESTAURANT3));
    }
}