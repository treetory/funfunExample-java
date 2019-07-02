package io.funfun.redbook;

import java.util.function.Function;
import java.util.function.Predicate;

public abstract class ConsList<T> {

    T head;
    ConsList<T> tail;

    public static <T> ConsList<T> getNil() {
        return getNil();
    }

    public T head() {
        return head;
    }

    public ConsList<T> tail() {
        return tail;
    }

    public boolean isEmpty() {
        return (this instanceof Nil) ? true : false;
    }

    public <R> ConsList<R> map(ConsList<R> acc, Function<T, R> mapper) {
        /*
        if (this instanceof Nil) {
            return acc;
        } else {
            return this.tail.map(new Cons<R>(mapper.apply(this.head), acc), mapper);
        }
        */
        return isEmpty() ? acc : this.tail.map(new Cons<R>(mapper.apply(this.head), acc), mapper);
    }

    public ConsList<T> filter(ConsList<T> acc, Predicate<T> predicate) {
        /*
        if (this instanceof Nil) {
            return acc;
        } else {
            ConsList<T> temp;
            if (predicate.test(this.head)) {
                temp = new Cons<T>(this.head, acc);
            } else {
                temp = acc;
            }
            return this.tail.filter(temp, predicate);
        }
        */
        return isEmpty() ? acc : (predicate.test(this.head) ? new Cons<T>(this.head, acc) : this.tail.filter(acc, predicate));
    }

    public ConsList<T> flatMap(ConsList<T> acc, Function<ConsList<T>, ConsList<T>> mapper) {
        return isEmpty() ? acc : mapper.apply(this.tail);
    }

}