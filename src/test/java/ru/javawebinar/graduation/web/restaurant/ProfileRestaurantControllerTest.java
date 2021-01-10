package ru.javawebinar.graduation.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import ru.javawebinar.graduation.service.RestaurantService;
import ru.javawebinar.graduation.web.AbstractControllerTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;

import static java.time.LocalDate.of;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.graduation.RestaurantTestData.*;
import static ru.javawebinar.graduation.TestUtil.userHttpBasic;
import static ru.javawebinar.graduation.UserTestData.USER;

class ProfileRestaurantControllerTest extends AbstractControllerTest {
    private static final String REST_URL = ProfileRestaurantController.REST_URL + "/";

    @Autowired
    private RestaurantService restaurantService;

    @Test
    void getAllByDate() throws Exception{
        mockMvc.perform(get(REST_URL + "?date=2019-11-23")
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        assertMatch(restaurantService.getAll(of(2019, Month.NOVEMBER, 23)), Arrays.asList(RESTAURANT1));
    }

    @Test
    void getAllByIdAndByDate() throws Exception{
        mockMvc.perform(get(REST_URL + RESTAURANT1_ID  + "?date=2019-11-23")
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        assertMatch(restaurantService.getAllForId(of(2019, Month.NOVEMBER, 23), RESTAURANT1_ID), Arrays.asList(RESTAURANT1));
    }

    @Test
    void getAllByToday() throws Exception{
        mockMvc.perform(get(REST_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        assertMatch(restaurantService.getAll(LocalDate.now()), Arrays.asList(RESTAURANT2, RESTAURANT3));
    }
}