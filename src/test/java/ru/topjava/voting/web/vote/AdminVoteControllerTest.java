package ru.topjava.voting.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.topjava.voting.web.AbstractControllerTest;
import ru.topjava.voting.TestUtil;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.topjava.voting.UserTestData.ADMIN;
import static ru.topjava.voting.VoteTestData.*;

class AdminVoteControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AdminVoteController.REST_URL + '/';

    @Test
    void getToday() throws Exception {
        mockMvc.perform(get(REST_URL + "votes")
                .with(TestUtil.userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(VOTE5_TO, VOTE6_TO));
    }

}