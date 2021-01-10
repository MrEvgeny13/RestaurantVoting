package ru.topjava.voting.to;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class VoteTo extends BaseTo {

    private LocalDate date;

    @NotNull
    private Integer restaurantId;

    public VoteTo() {
    }

    public VoteTo(LocalDate date, Integer restaurantId) {
        this(null, date, restaurantId);
    }

    public VoteTo(Integer id, LocalDate date, Integer restaurantId) {
        super(id);
        this.date = date;
        this.restaurantId = restaurantId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VoteTo voteTo = (VoteTo) o;

        if (!date.equals(voteTo.date)) return false;
        if (!restaurantId.equals(voteTo.restaurantId)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + restaurantId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "VoteTo{" +
                "date=" + date +
                ", restaurantId=" + restaurantId +
                '}';
    }
}