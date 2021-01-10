package ru.topjava.voting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.topjava.voting.RestaurantTestData;
import ru.topjava.voting.model.Restaurant;
import ru.topjava.voting.util.exception.NotFoundException;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.topjava.voting.RestaurantTestData.*;

public class RestaurantServiceTest extends AbstractServiceTest {

    @Autowired
    protected RestaurantService service;

    @Test
    void create() {
        Restaurant newRest = RestaurantTestData.getCreated();
        Restaurant created = service.create(newRest);
        RestaurantTestData.assertMatch(newRest, created);
    }

    @Test
    void get() {
        Restaurant restaurant = service.get(RestaurantTestData.RESTAURANT1_ID);
        RestaurantTestData.assertMatch(restaurant, RestaurantTestData.RESTAURANT1);
    }

    @Test
    void getNotFound() {
        assertThrows(EntityNotFoundException.class, () -> service.get(1));
    }

    @Test
    void update() {
        Restaurant restaurant = service.update(RestaurantTestData.getUpdated());
        assertMatch(restaurant, service.get(RestaurantTestData.RESTAURANT1_ID));
    }

    @Test
    void updateNotFound() {
        Restaurant restaurant = new Restaurant(1, "New");
        assertThrows(EntityNotFoundException.class, () -> service.update(restaurant));
    }

    @Test
    void delete() {
        service.delete(RestaurantTestData.RESTAURANT1_ID);
        assertMatch(service.getAllWithoutDish(), Arrays.asList(RestaurantTestData.RESTAURANT2, RestaurantTestData.RESTAURANT3));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(1));
    }

}
