package ru.javawebinar.graduation;

import ru.javawebinar.graduation.model.Restaurant;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import static ru.javawebinar.graduation.DishTestData.*;
import static ru.javawebinar.graduation.model.AbstractBaseEntity.START_SEQ;

public class RestaurantTestData {
    public static final int RESTAURANT1_ID = START_SEQ + 3;
    private static final int RESTAURANT2_ID = START_SEQ + 4;
    private static final int RESTAURANT3_ID = START_SEQ + 5;

    public static final Restaurant RESTAURANT1 = new Restaurant(RESTAURANT1_ID, "Restaurant1");
    public static final Restaurant RESTAURANT2 = new Restaurant(RESTAURANT2_ID, "Restaurant2");
    public static final Restaurant RESTAURANT3 = new Restaurant(RESTAURANT3_ID, "Restaurant3");

    private static final Restaurant RESTAURANT1UPDATE = new Restaurant(RESTAURANT1_ID, "Restaurant1_Update");

    static {
        RESTAURANT1.setDishes(Arrays.asList(DISH1, DISH2, DISH3));
        RESTAURANT2.setDishes(Arrays.asList(DISH4, DISH5));
        RESTAURANT3.setDishes(Arrays.asList(DISH6, DISH7));
        RESTAURANT1UPDATE.setDishes(Arrays.asList(DISH1, DISH2, DISH3, DISHNEW));
    }

    public static Restaurant getCreated() {
        return new Restaurant("New Restaurant");
    }

    public static Restaurant getUpdated() {
        Restaurant updated = new Restaurant(RESTAURANT1_ID, "Restaurant1_updated");
        updated.setDishes(Arrays.asList(DISH1, DISH2, DISH3));
        return updated;
    }

    public static void assertMatch(Restaurant actual, Restaurant expected) {
        assertThat(actual).isEqualToComparingFieldByFieldRecursively(expected);
    }

    public static void assertMatch(Iterable<Restaurant> actual, Iterable<Restaurant> expected) {
        assertThat(actual).isEqualTo(expected);
    }

}
