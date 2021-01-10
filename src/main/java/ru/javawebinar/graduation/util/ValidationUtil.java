package ru.javawebinar.graduation.util;

import ru.javawebinar.graduation.HasId;
import ru.javawebinar.graduation.util.exception.IllegalRequestDataException;
import ru.javawebinar.graduation.util.exception.NotFoundException;
import ru.javawebinar.graduation.util.exception.VoteTimeViolationException;

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

    public static void checkNew(HasId entity) {
        if (!entity.isNew()) {
            throw new IllegalRequestDataException(entity + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(HasId entity, int id) {
//      http://stackoverflow.com/a/32728226/548473
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.getId() != id) {
            throw new IllegalArgumentException(entity + " must be with id=" + id);
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
            throw new VoteTimeViolationException("It's too late to change vote");
        }
    }

    public static LocalTime getDeadLineTime() {
        return DEADLINE_TIME;
    }

    public static void setDeadLineTime(LocalTime time) {
        DEADLINE_TIME = time;
    }

}
