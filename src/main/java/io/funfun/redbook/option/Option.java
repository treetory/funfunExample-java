package io.funfun.redbook.option;

import java.util.function.Function;
import java.util.function.Predicate;

public abstract class Option<T> {

    T value;

    public abstract Option<T> get();

    public static <T> Option<T> of(T t) {
        return new Some<>(t);
    }

    // TODO 자기 자신이 Nil 일 때, Nil 리턴하도록 처리할 것
    public Option<T> filter(Predicate<T> predicate) {
        return predicate.test(this.value) ? new Some<>(this.value) : Nil.getNil();
    }

    // TODO 자기 자신이 Nil 일 때, Nil 리턴하도록 처리할 것
    public <R> Option<R> map(Function<T, ? extends R> mapper) {
        return new Some<>(mapper.apply(this.value));
    }

    // TODO 자기 자신이 Nil 일 때, Nil 리턴하도록 처리할 것
    public Option<T> getOrElse() {
        return (get() instanceof Nil) ? Nil.getNil() : this;
    }

}
