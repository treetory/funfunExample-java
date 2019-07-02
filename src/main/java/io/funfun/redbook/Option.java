package io.funfun.redbook;

import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class Option<T> {

    public static final Option NONE = new None();

    public static class Some<T> extends Option<T> {

        private final T value;

        public Some(T value) {
            this.value = value;
        }

        @Override
        public T get() {
            return this.value;
        }

        @Override
        public <T> Option<T> map(Function<T, Option<T>> mapper) {
            return new Some<T>((T) mapper.apply((T) this.value));
        }

        @Override
        public Option<T> filter(Predicate<Option> predicate) {
            return predicate.test((Option) this.value) ? this : NONE;
        }

        @Override
        public Option<T> getOrElse() {
            return get() != null ? new Some(this.value) : NONE;
        }

        @Override
        public Option<T> flatMap(Function<T, Option<T>> mapper) {
            return mapper.apply(this.value);
        }
    }

    public static class None<T> extends Option<Void> {

        public None() {
            super();
        }

        @Override
        public Void get() {
            throw new NoSuchElementException("No value present");
        }

        @Override
        public <T> Option<T> map(Function<T, Option<T>> mapper) {
            return NONE;
        }

        @Override
        public Option<Void> filter(Predicate<Option> predicate) {
            return NONE;
        }

        @Override
        public Option<Void> getOrElse() {
            return NONE;
        }

        @Override
        public Option<Void> flatMap(Function<Void, Option<Void>> mapper) {
            return NONE;
        }
    }

    public abstract T get();
    public abstract <T> Option<T> map(Function<T, Option<T>> mapper);
    public abstract Option<T> filter(Predicate<Option> predicate);
    public abstract Option<T> getOrElse();
    public abstract Option<T> flatMap(Function<T, Option<T>> mapper);

}
