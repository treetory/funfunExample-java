package io.funfun.redbook.stream;

public abstract class Stream<T> {

    T head;
    Lazy<Stream<T>> tail;

    public abstract T head();
    public abstract Lazy<Stream<T>> tail();
}
