package io.funfun.redbook.stream;

import java.util.function.Predicate;

public abstract class Stream<T> {

    T head;
    Lazy<Stream<T>> tail;

    public abstract T head();
    public abstract Stream<T> tail();

    public Stream<T> filter(Stream<T> acc, Predicate<T> predicate) {

        if (this instanceof Nil) {
            return acc;
        } else {
            if (predicate.test(this.head)) {
                return new Cons<T>(this.head, () -> this.tail().filter(acc, predicate));
            } else {
                return this.tail();
            }
        }

    }

}
