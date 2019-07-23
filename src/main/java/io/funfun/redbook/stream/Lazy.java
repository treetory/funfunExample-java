package io.funfun.redbook.stream;

import java.util.Objects;
import java.util.function.Supplier;

public final class Lazy<T> implements Supplier<T>{

    private transient volatile Supplier<T> supplier;
    private T value;

    private Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public static <T> Lazy<T> of(Supplier<T> supplier) {
        Objects.requireNonNull(supplier);
        if (supplier instanceof Lazy) {
            return (Lazy<T>) supplier;
        } else {
            return new Lazy<>(supplier);
        }
    }

    @Override
    public T get() {
        return this.supplier == null ? value : computeValue();
    }

    private synchronized T computeValue() {
        final Supplier<T> s = supplier;
        if (s != null) {
            value = s.get();
            supplier = null;
        }
        return value;
    }
}
