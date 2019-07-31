package io.funfun.redbook.stream;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

public class Cons<T> extends Stream<T> {

    public Cons(T head/*Supplier<T> head*/, Supplier<Stream<T>> tail) {
        this.head = Lazy.of(head);
        this.tail = Lazy.of(tail);
    }

    @Override
    public T head() {
        return this.head.get();
    }

    @Override
    public Stream<T> tail() {
        return this.tail.get();
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {

            @Override
            public boolean hasNext() {
                return (tail() instanceof Nil) ? false : true ;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("tail is empty");
                }
                return tail().head();
            }
        };
    }

}
