package ru.topjava.voting.web.dish;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.topjava.voting.model.Dish;
import ru.topjava.voting.service.DishService;

import java.net.URI;
import java.util.List;

import static ru.topjava.voting.util.ValidationUtil.assureIdConsistent;
import static ru.topjava.voting.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminDishController {
    static final String REST_URL = "/rest/admin/restaurants";
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final DishService dishService;

    @Autowired
    public AdminDishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping(value = "/{restId}/dishes", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public List<Dish> getAllByRest(@PathVariable("restId") int restId) {
        log.info("get all dishes from restaurants {}", restId);
        return dishService.getAll(restId);
    }

    @GetMapping(value = "/{restId}/dishes/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Dish get(@PathVariable("restId") int restId, @PathVariable("id") int id) {
        log.info("get dish by Id {} in restaurant {}", id, restId);
        return dishService.get(id, restId);
    }

    @DeleteMapping("/{restId}/dishes/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("restId") int restId, @PathVariable("id") int id) {
        log.info("delete dish {} in restaurant {} ", id, restId);
        dishService.delete(id, restId);
    }

    @PutMapping(value = "/{restId}/dishes/{dishId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public Dish update(@RequestBody Dish dish, @PathVariable("restId") int restId, @PathVariable("dishId") int dishId) {
        assureIdConsistent(dish, dishId);
        log.info("update dish {}", dishId);
        return dishService.update(dish, restId);
    }

    @PostMapping(value = "/{restId}/dishes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> save(@RequestBody Dish dish, @PathVariable("restId") int restId) {
        checkNew(dish);
        log.info("create dish in restaurant {}", restId);
        Dish created = dishService.create(dish, restId);
        URI uriOfNewResource = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(REST_URL + "/" + "dishes/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}