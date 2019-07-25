package io.funfun.redbook.stream;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Nil<T> extends Stream<T> {

    private static Nil Nil;

    public static <T> Nil<T> getNil() {
        if (Nil == null) {
            Nil = new Nil<T>();
        }
        return Nil;
    }

    private Nil() {}

    @Override
    public T head() {
        throw new NoSuchElementException("head of empty stream");
    }

    @Override
    public Stream<T> tail() {
        throw new UnsupportedOperationException("tail of empty stream");
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public T next() {
                return (T) getNil();
            }
        };
    }

}