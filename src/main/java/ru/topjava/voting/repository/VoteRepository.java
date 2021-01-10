package ru.topjava.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.voting.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    @Override
    @Transactional
    Vote save(Vote vote);

    List<Vote> findByUserIdOrderByDateDesc(int userId);

    @Query("SELECT v FROM Vote v WHERE v.date = :date AND v.user.id=:userId")
    Vote get(@Param("date") LocalDate date, @Param("userId") int userId);

    @Query("SELECT v from Vote v WHERE v.date BETWEEN :startDate AND :endDate")
    Optional<List<Vote>> getAllBetweenDate(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId AND v.date BETWEEN :startDate AND :endDate")
    Optional<List<Vote>> getAllBetweenDateWithUserId(@Param("userId") int userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
