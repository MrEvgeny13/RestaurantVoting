package ru.topjava.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.voting.model.Vote;
import ru.topjava.voting.repository.RestaurantRepository;
import ru.topjava.voting.repository.UserRepository;
import ru.topjava.voting.repository.VoteRepository;
import ru.topjava.voting.to.VoteTo;
import ru.topjava.voting.util.ValidationUtil;
import ru.topjava.voting.util.VoteUtil;
import ru.topjava.voting.util.exception.DoubleViolationException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service("voteService")
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    public VoteTo getVote(int userId, LocalDate date) {
        return VoteUtil.asTo(voteRepository.get(date, userId));
    }

    public List<Vote> getAll(int userId) {
        return voteRepository.findByUserIdOrderByDateDesc(userId);
    }

    public List<VoteTo> getBetweenDates(@Nullable LocalDate startDate, @Nullable LocalDate endDate) {
        return VoteUtil.asTo(voteRepository.getAllBetweenDate(startDate, endDate).orElseThrow(() -> new IllegalArgumentException("not found votes for period from [ " + startDate + " ] to [ " + endDate + " ]")));
    }

    public List<VoteTo> getBetweenDatesByUser(int userId, LocalDate startDate, LocalDate endDate) {
        return VoteUtil.asTo(voteRepository.getAllBetweenDateWithUserId(userId, startDate, endDate).orElseThrow(() -> new IllegalArgumentException("not found votes for period from [ " + startDate + " ] to [ " + endDate + " ] and user id : \" + userId")));
    }

    @Transactional
    public VoteTo save(int userId, LocalDate date, VoteTo voteTo) {
        Vote vote = voteRepository.get(date, userId);

        if (voteTo.isNew() && vote != null) {
            throw new DoubleViolationException("Have you already voted today");
        }
        if (!voteTo.isNew() && vote != null) {
            ValidationUtil.checkTimeForOperations(LocalTime.now());
        }
        if (voteTo.isNew() && vote == null) {
            vote = new Vote();
            vote.setUser(userRepository.getOne(userId));
        }

        vote.setRestaurant(restaurantRepository.getOne(voteTo.getRestaurantId()));
        vote.setDate(LocalDate.now());

        return VoteUtil.asTo(voteRepository.save(vote));
    }


}
