package com.fiap.pj.core.util;

import java.util.Set;

import static java.util.Objects.nonNull;

public class CollectionUtils {

    private CollectionUtils() {
    }

    public static <T> void instanceNonNullCollection(Set<T> current, Set<T> submitted) {
        if (nonNull(current) && nonNull(submitted)) {
            current.removeIf(value -> (!submitted.contains(value)));
            current.addAll(submitted);
        }
    }

}
