package ru.javawebinar.graduation;

import org.springframework.test.web.servlet.ResultMatcher;
import ru.javawebinar.graduation.to.VoteTo;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.javawebinar.graduation.model.AbstractBaseEntity.START_SEQ;

public class VoteTestData {
    private static final int VOTE1 = START_SEQ + 13; //20
    private static final int VOTE2 = START_SEQ + 14;
    private static final int VOTE3 = START_SEQ + 15;
    private static final int VOTE4 = START_SEQ + 16;
    public static final int VOTE5 = START_SEQ + 17;
    private static final int VOTE6 = START_SEQ + 18;

    public static final VoteTo VOTE1_TO = new VoteTo(VOTE1, LocalDate.of(2019, 12, 23), 100004);
    public static final VoteTo VOTE2_TO = new VoteTo(VOTE2, LocalDate.of(2019, 12, 23), 100004);
    public static final VoteTo VOTE3_TO = new VoteTo(VOTE3, LocalDate.of(2019, 12, 24), 100003);
    public static final VoteTo VOTE4_TO = new VoteTo(VOTE4, LocalDate.of(2019, 12, 24), 100003);
    public static final VoteTo VOTE5_TO = new VoteTo(VOTE5, LocalDate.now(), 100003);
    public static final VoteTo VOTE6_TO = new VoteTo(VOTE6, LocalDate.now(), 100004);

    public static VoteTo getCreated() {
        return new VoteTo(LocalDate.now(), 100003);
    }

    public static VoteTo getUpdated() {
        return new VoteTo(VOTE5, LocalDate.now(), 100004);
    }

    public static void assertMatch(VoteTo actual, VoteTo expected) {
        assertThat(actual).isEqualToComparingFieldByFieldRecursively(expected);
        assertEquals(actual.getDate(), expected.getDate());
    }

    public static ResultMatcher contentJson(VoteTo... expected) {
        return contentJson(List.of(expected));
    }

    private static ResultMatcher contentJson(Iterable<VoteTo> expected) {
        return result -> assertThat(TestUtil.readListFromJsonMvcResult(result, VoteTo.class)).isEqualTo(expected);
    }

}
