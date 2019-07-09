package io.funfun.redbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class ConsList<T> extends AbstractList<T> {

    private static final Logger LOG = LoggerFactory.getLogger(ConsList.class);

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

    public ConsList<T> reverse(ConsList<T> acc) {
        if (this instanceof  Nil) {
            return acc;
        } else {
            ConsList<T> temp = acc;
            acc = new Cons<>(this.head, temp);
            return this.tail.reverse(acc);
        }
    }

    public <R> ConsList<R> map(ConsList<R> acc, Function<T, R> mapper) {
        if (this instanceof Nil) {
            //LOG.debug("head : {}, tail : {}, acc : {}", this.head, this.tail, acc);
            return acc.reverse(Nil.getNil());
        } else {
            //LOG.debug("head : {}, tail : {}, acc : {}", this.head, this.tail, acc);
            return this.tail.map(new Cons<>(mapper.apply(this.head), acc), mapper);
        }
    }

    public ConsList<T> filter(ConsList<T> acc, Predicate<T> predicate) {

        if (this instanceof Nil) {
            return acc.reverse(Nil.getNil());
        } else {
            ConsList<T> temp;
            if (predicate.test(this.head)) {
                temp = new Cons<>(this.head, acc);
            } else {
                temp = acc;
            }
            return this.tail.filter(temp, predicate);
        }
    }

    public <R> ConsList<R> flatMap(ConsList<R> acc, Function<T, ? extends Iterable<R>> mapper) {

        if (this instanceof Nil) {
            return acc.reverse(Nil.getNil());
        } else {

            Iterator<R> ir = (mapper.apply(this.head)).iterator();

            while(ir.hasNext()) {

                R cur = ir.next();

                //LOG.debug("{}, {}, {}", cur, acc.head, acc.tail);

                if (acc instanceof Nil) {
                    acc = new Cons<>(cur, Nil.getNil());
                } else {
                    ConsList<R> temp = acc;
                    acc = new Cons<>(cur, temp);
                }
            }

            return this.tail.flatMap(acc, mapper);
        }

    }

    @Override
    public String toString() {
        return "ConsList{" +
                "head=" + head +
                ", tail=" + tail +
                '}';
    }
}