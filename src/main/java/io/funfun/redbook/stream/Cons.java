package io.funfun.redbook.stream;

import java.util.function.Supplier;

public class Cons<T> extends Stream<T> {

    public Cons(T head, Supplier<Stream<T>> tail) {
        this.head = head;
        this.tail = Lazy.of(tail);
    }

    @Override
    public T head() {
        return this.head;
    }

    @Override
    public Stream<T> tail() {
        return this.tail.get();
    }

}
