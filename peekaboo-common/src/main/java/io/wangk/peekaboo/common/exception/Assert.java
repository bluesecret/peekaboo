package io.wangk.peekaboo.common.exception;

import java.util.Collection;
import java.util.Map;

public abstract class Assert {

    public static void isTrue(boolean expression, Status status) {
        if (!expression) {
            throw new StatusException(status);
        }
    }

    public static void isTrue(boolean expression, String code, String msg) {
        if (!expression) {
            throw new StatusException(code, msg);
        }
    }

    public static void isFalse(boolean expression, Status status) {
        if (expression) {
            throw new StatusException(status);
        }
    }

    public static void isFalse(boolean expression, String code, String msg) {
        if (expression) {
            throw new StatusException(code, msg);
        }
    }

    public static void isTrue(boolean expression, String msg) {
        if (!expression) {
            throw new StatusException(msg);
        }
    }

    public static void isFalse(boolean expression, String msg) {
        if (expression) {
            throw new StatusException(msg);
        }
    }

    public static void isTrue(boolean expression) {
        isTrue(expression, "[Assertion failed] Must be true");
    }

    public static void isFalse(boolean expression) {
        isFalse(expression, "[Assertion failed] Must be false");
    }

    public static void notNull(Object object, Status status) {
        if (object == null) {
            throw new StatusException(status);
        }
    }

    public static void notNull(Object object, String code, String msg) {
        if (object == null) {
            throw new StatusException(code, msg);
        }
    }

    public static void notNull(Object object, String msg) {
        if (object == null) {
            throw new StatusException(msg);
        }
    }

    public static void notNull(Object object) {
        notNull(object, "[Assertion failed] Must not null");
    }

    public static void notEmpty(Collection<?> collection, Status status) {
        if (collection == null || collection.isEmpty()) {
            throw new StatusException(status);
        }
    }

    public static void notEmpty(Collection<?> collection, String code, String msg) {
        if (collection == null || collection.isEmpty()) {
            throw new StatusException(code, msg);
        }
    }

    public static void notEmpty(Collection<?> collection, String msg) {
        if (collection == null || collection.isEmpty()) {
            throw new StatusException(msg);
        }
    }

    public static void notEmpty(Collection<?> collection) {
        notEmpty(collection, "[Assertion failed] Collection must not be empty: it must contain at least 1 element");
    }

    public static void notEmpty(Map<?, ?> map, Status status) {
        if (map == null || map.isEmpty()) {
            throw new StatusException(status);
        }
    }

    public static void notEmpty(Map<?, ?> map, String code, String msg) {
        if (map == null || map.isEmpty()) {
            throw new StatusException(code, msg);
        }
    }

    public static void notEmpty(Map<?, ?> map, String msg) {
        if (map == null || map.isEmpty()) {
            throw new StatusException(msg);
        }
    }

    public static void notEmpty(Map<?, ?> map) {
        notEmpty(map, "[Assertion failed] Map must not be empty: it must contain at least one entry");
    }

}
