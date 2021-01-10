package ru.topjava.voting.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.topjava.voting.TestUtil;
import ru.topjava.voting.VoteTestData;
import ru.topjava.voting.service.VoteService;
import ru.topjava.voting.to.VoteTo;
import ru.topjava.voting.util.ValidationUtil;
import ru.topjava.voting.web.AbstractControllerTest;
import ru.topjava.voting.web.json.JsonUtil;
import ru.topjava.voting.UserTestData;

import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProfileVoteControllerTest extends AbstractControllerTest {
    private static final String REST_URL = ProfileVoteController.REST_URL + '/';

    @Autowired
    private VoteService service;

    @Test
    void doubleVote() throws Exception {
        VoteTo created = VoteTestData.getCreated();
        mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(created))
                .with(TestUtil.userHttpBasic(UserTestData.USER)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.detail").value("You have already voted today"));
    }

    @Test
    void create() throws Exception {
        VoteTo created = VoteTestData.getCreated();
        ResultActions action = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(created))
                .with(TestUtil.userHttpBasic(UserTestData.USER2)))
                .andDo(print())
                .andExpect(status().isCreated());

        VoteTo returned = TestUtil.readFromJson(action, VoteTo.class);
        created.setId(returned.getId());
        VoteTestData.assertMatch(returned, created);
    }

    @Test
    void update() throws Exception {
        VoteTo updated = VoteTestData.getUpdated();
        ValidationUtil.setDeadLineTime(LocalTime.now().plusMinutes(1));
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + VoteTestData.VOTE5)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(TestUtil.userHttpBasic(UserTestData.USER)))
                .andDo(print())
                .andExpect(status().isOk());

        VoteTo returned = service.getVote(UserTestData.USER.getId(), updated.getDate());
        VoteTestData.assertMatch(returned, updated);
    }

    @Test
    void updateAfter() throws Exception {
        VoteTo updated = VoteTestData.getUpdated();
        ValidationUtil.setDeadLineTime(LocalTime.now().minusMinutes(1));
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + VoteTestData.VOTE5)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(TestUtil.userHttpBasic(UserTestData.USER)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.detail").value("It's too late to change your vote"));
    }

    @Test
    void getTodayVote() throws Exception {
        mockMvc.perform(get(REST_URL)
                .with(TestUtil.userHttpBasic(UserTestData.USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VoteTestData.contentJson(VoteTestData.VOTE5_TO));
    }

    @Test
    void getBetween() throws Exception {
        mockMvc.perform(get(REST_URL + "filter")
                .param("startDate", "2019-12-23")
                .param("endDate", "2019-12-24")
                .with(TestUtil.userHttpBasic(UserTestData.USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VoteTestData.contentJson(VoteTestData.VOTE1_TO, VoteTestData.VOTE3_TO));
    }

    @Test
    void getUnauth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

}
