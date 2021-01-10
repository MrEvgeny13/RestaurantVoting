package ru.topjava.voting.web.restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.topjava.voting.model.Restaurant;
import ru.topjava.voting.service.RestaurantService;

import java.time.LocalDate;
import java.util.List;

@RestController

@RequestMapping(value = ProfileRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileRestaurantController {

    static final String REST_URL = "/rest/restaurants";
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestaurantService service;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Restaurant> getAllForId(@PathVariable("id") int id,
                                        @RequestParam(value = "date", required = false) LocalDate date) {
        log.info("getAll for date {} from restaurant {}", date, id);
        return service.getAllForId(checkDate(date), id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Restaurant> getAll(@RequestParam(value = "date", required = false) LocalDate date) {
        log.info("get all restaurants");
        return service.getAll(checkDate(date));
    }

    private static LocalDate checkDate(LocalDate date) {
        return date == null ? LocalDate.now() : date;
    }

}
