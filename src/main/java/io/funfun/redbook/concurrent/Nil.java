package io.funfun.redbook.concurrent;

public class Nil<T> extends Par<T> {

    private static Nil Nil;

    public static <T> Nil<T> getNil() {
        if (Nil == null) {
            Nil = new Nil();
        }
        return Nil;
    }

    public T get() {
        return (T) getNil();
    }
}
