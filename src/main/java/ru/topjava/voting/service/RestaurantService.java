package ru.topjava.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.voting.model.Restaurant;
import ru.topjava.voting.repository.RestaurantRepository;
import ru.topjava.voting.util.ValidationUtil;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

import static ru.topjava.voting.util.ValidationUtil.checkNotFoundWithId;

@Service("restaurantService")
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Cacheable("restaurants")
    public List<Restaurant> getAllWithDish(LocalDate date) {
        return restaurantRepository.findAllWithDish(date);
    }

    public List<Restaurant> getAllForId(LocalDate date, int id) {
        return ValidationUtil.checkNotFoundWithId(restaurantRepository.findAllWithDishForId(date, id), id);
    }

    public List<Restaurant> getAllWithoutDish() {
        return restaurantRepository.findAll();
    }

    public Restaurant get(int id) {
        return restaurantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Restaurant is not found by id: " + id));
    }

    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    public void delete(int id) {
        ValidationUtil.checkNotFoundWithId (restaurantRepository.delete(id) != 0, id);
    }

    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    public Restaurant create(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    public Restaurant update(Restaurant restaurant) {
        restaurantRepository.findById(restaurant.getId()).orElseThrow(() -> new EntityNotFoundException("Restaurant is not found by id: " + restaurant.getId()));
        return ValidationUtil.checkNotFoundWithId(restaurantRepository.save(restaurant), restaurant.getId());
    }
}