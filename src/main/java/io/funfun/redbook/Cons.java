package io.funfun.redbook;

public class Cons<T> extends ConsList<T> {

    public Cons(T head, ConsList<T> tail) {
        this.head = head;
        this.tail = tail;
    }

    public static <T> Cons<T> cons(T head, ConsList<T> tail) {
        return new Cons<>(head, tail);
    }

}
