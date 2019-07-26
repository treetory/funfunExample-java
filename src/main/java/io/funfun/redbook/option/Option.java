package io.funfun.redbook.option;

import java.util.function.Function;
import java.util.function.Predicate;

public abstract class Option<T> {

    T value;

    public abstract Option<T> get();

    public static <T> Option<T> of(T t) {
        return new Some<>(t);
    }

    public Option<T> filter(Predicate<T> predicate) {
        return predicate.test(this.value) ? new Some<>(this.value) : Nil.getNil();
    }

    public <R> Option<R> map(Function<T, ? extends R> mapper) {
        return new Some<>(mapper.apply(this.value));
    }

    public Option<T> getOrElse() {
        return (get() instanceof Nil) ? Nil.getNil() : this;
    }

}
