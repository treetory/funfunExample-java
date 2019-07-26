package io.funfun.redbook.option;

public class Nil<T> extends Option<T> {

    private static Nil Nil;

    public static <T> Nil<T> getNil() {
        if (Nil == null) {
            Nil = new Nil();
        }
        return Nil;
    }

    @Override
    public Option<T> get() {
        return getNil();
    }

}
