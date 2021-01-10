package ru.topjava.voting.util;

import ru.topjava.voting.HasUserId;
import ru.topjava.voting.util.exception.IllegalRequestDataException;
import ru.topjava.voting.util.exception.NotFoundException;
import ru.topjava.voting.util.exception.VoteTimeViolationException;

import java.time.LocalTime;

public class ValidationUtil {

    private static LocalTime DEADLINE_TIME = LocalTime.of(11, 00);

    private ValidationUtil() {
    }

    public static <T> T checkNotFoundWithId(T object, int id, Class<T> clazz) throws NotFoundException {
        return checkNotFound(object, "type: " + clazz.getSimpleName() + ", id=" + id);
    }

    public static <T> T checkNotFoundWithId(T object, int id) {
        return checkNotFound(object, "id=" + id);
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }

    public static void checkNew(HasUserId entity) {
        if (!entity.isNew()) {
            throw new IllegalRequestDataException(entity + " should be new (id = null)");
        }
    }

    public static void assureIdConsistent(HasUserId entity, int id) {
//      http://stackoverflow.com/a/32728226/548473
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.getId() != id) {
            throw new IllegalArgumentException(entity + " should be with id = " + id);
        }
    }

    //  http://stackoverflow.com/a/28565320/548473
    public static Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }

    public static void checkTimeForOperations(LocalTime time) {
        if (time.isAfter(DEADLINE_TIME)) {
            throw new VoteTimeViolationException("It's too late to change your vote");
        }
    }

    public static LocalTime getDeadLineTime() {
        return DEADLINE_TIME;
    }

    public static void setDeadLineTime(LocalTime time) {
        DEADLINE_TIME = time;
    }
}