package ru.topjava.voting;

public interface HasUserId {
    Integer getId();

    void setId(Integer id);

    default boolean isNew() {
        return getId() == null;
    }
}