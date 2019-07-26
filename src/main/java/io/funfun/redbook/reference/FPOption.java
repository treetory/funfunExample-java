package io.funfun.redbook.reference;

import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class FPOption<T> {

    public static final FPOption NONE = new None();

    public static class Some<T> extends FPOption<T> {

        private final T value;

        public Some(T value) {
            this.value = value;
        }

        @Override
        public T get() {
            return this.value;
        }

        @Override
        public <T> FPOption<T> map(Function<T, FPOption<T>> mapper) {
            return new Some<T>((T) mapper.apply((T) this.value));
        }

        @Override
        public FPOption<T> filter(Predicate<FPOption> predicate) {
            return predicate.test((FPOption) this.value) ? this : NONE;
        }

        @Override
        public FPOption<T> getOrElse() {
            return get() != null ? new Some(this.value) : NONE;
        }

        @Override
        public FPOption<T> flatMap(Function<T, FPOption<T>> mapper) {
            return mapper.apply(this.value);
        }
    }

    public static class None<T> extends FPOption<Void> {

        public None() {
            super();
        }

        @Override
        public Void get() {
            throw new NoSuchElementException("No value present");
        }

        @Override
        public <T> FPOption<T> map(Function<T, FPOption<T>> mapper) {
            return NONE;
        }

        @Override
        public FPOption<Void> filter(Predicate<FPOption> predicate) {
            return NONE;
        }

        @Override
        public FPOption<Void> getOrElse() {
            return NONE;
        }

        @Override
        public FPOption<Void> flatMap(Function<Void, FPOption<Void>> mapper) {
            return NONE;
        }
    }

    public abstract T get();
    public abstract <T> FPOption<T> map(Function<T, FPOption<T>> mapper);
    public abstract FPOption<T> filter(Predicate<FPOption> predicate);
    public abstract FPOption<T> getOrElse();
    public abstract FPOption<T> flatMap(Function<T, FPOption<T>> mapper);

}
