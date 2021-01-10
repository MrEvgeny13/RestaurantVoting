package ru.javawebinar.graduation.web.vote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.graduation.service.VoteService;
import ru.javawebinar.graduation.to.VoteTo;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.graduation.web.SecurityUtil.authUserId;

@RestController
@RequestMapping(value = ProfileVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileVoteController {
    static final String REST_URL = "/rest/votes";

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private VoteService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VoteTo> create(@Valid @RequestBody VoteTo voteTo) {
        log.info("save Vote for user {}", authUserId());
        VoteTo created = service.save(authUserId(), LocalDate.now(), voteTo);
        URI uriOfNewResource = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VoteTo> update(@Valid @RequestBody VoteTo voteTo, @PathVariable("id") int id) {
        log.info("update Vote {} for user {}", id, authUserId());
        voteTo.setId(id);
        VoteTo updated = service.save(authUserId(), LocalDate.now(), voteTo);

        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<VoteTo> getBetweenWithUser(@RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                           @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("Vote getBetween dates({} - {}) for user {}", startDate, endDate, authUserId());
        return service.getBetweenDatesByUser(authUserId(), startDate, endDate);
    }

    @GetMapping
    public VoteTo getToday() {
        log.info("get today Vote for user {}", authUserId());
        return service.getVote(authUserId(), LocalDate.now());
    }

}
