package ru.javawebinar.graduation.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.graduation.RestaurantTestData;
import ru.javawebinar.graduation.model.Restaurant;
import ru.javawebinar.graduation.util.exception.NotFoundException;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.graduation.RestaurantTestData.*;

public class RestaurantServiceTest extends AbstractServiceTest {

    @Autowired
    protected RestaurantService service;

    @Test
    void create() {
        Restaurant newRest = RestaurantTestData.getCreated();
        Restaurant created = service.create(newRest);
        assertMatch(newRest, created);
    }

    @Test
    void get() {
        Restaurant restaurant = service.get(RESTAURANT1_ID);
        assertMatch(restaurant, RESTAURANT1);
    }

    @Test
    void getNotFound() {
        assertThrows(EntityNotFoundException.class, () -> service.get(1));
    }

    @Test
    void update() {
        Restaurant restaurant = service.update(getUpdated());
        assertMatch(restaurant, service.get(RESTAURANT1_ID));
    }

    @Test
    void updateNotFound() {
        Restaurant restaurant = new Restaurant(1, "New");
        assertThrows(EntityNotFoundException.class, () -> service.update(restaurant));
    }

    @Test
    void delete() {
        service.delete(RESTAURANT1_ID);
        assertMatch(service.getAllWithoutDish(), Arrays.asList(RESTAURANT2, RESTAURANT3));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(1));
    }

}
