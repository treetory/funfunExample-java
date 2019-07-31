package io.funfun.redbook.option;

import java.util.function.Function;
import java.util.function.Predicate;

public abstract class Option<T> {

    T value;

    public abstract Option<T> get();

    public static <T> Option<T> of(T t) {
        return new Some<>(t);
    }

    // 2019-07-30 리뷰 : 자기 자신이 Nil 일 때, Nil 리턴하도록 처리할 것
    public Option<T> filter(Predicate<T> predicate) {
        if (this instanceof Nil) {
            return Nil.getNil();
        } else {
            return predicate.test(this.value) ? new Some<>(this.value) : Nil.getNil();
        }
    }

    // 2019-07-30 리뷰 : 자기 자신이 Nil 일 때, Nil 리턴하도록 처리할 것
    public <R> Option<R> map(Function<T, ? extends R> mapper) {
        if (this instanceof Nil) {
            return Nil.getNil();
        } else {
            return new Some<>(mapper.apply(this.value));
        }
    }

    // 2019-07-30 리뷰 : 자기 자신이 Nil 일 때, Nil 리턴하도록 처리할 것
    public Option<T> getOrElse() {
        if (this instanceof Nil) {
            return Nil.getNil();
        } else {
            return (get() instanceof Nil) ? Nil.getNil() : this;
        }
    }

}
