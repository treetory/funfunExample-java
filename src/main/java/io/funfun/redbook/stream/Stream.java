package io.funfun.redbook.stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.Predicate;

public abstract class Stream<T> {

    private static final Logger LOG = LoggerFactory.getLogger(Stream.class);

    T head;
    Lazy<Stream<T>> tail;

    public abstract T head();
    public abstract Stream<T> tail();

    public static <T> Stream<T> of(T element) {
        return new Cons<>(element, Nil::getNil);
    }

    public static <T> Stream<T> of (T... elements) {
        Objects.requireNonNull(elements);
        return Stream.ofAll(elements);
    }

    private static <T> Stream<T> ofAll(T[] elements) {
        Stream<T> stream = Nil.getNil();
        for (T element : elements) {
            final Stream<T> temp = stream;
            stream = new Cons<T>(element, () -> temp);
        }
        return stream;
    }

    public Stream<T> filter(Stream<T> acc, Predicate<T> predicate) {

        if (this instanceof Nil) {
            LOG.debug("<<<  : {}", acc.toString());
            return acc;
        } else {
            //LOG.debug(">>> head : {}", this.head);
            //LOG.debug(">>> tail : {}", this.tail());
            Stream<T> temp = acc;
            if (predicate.test(this.head)) {
                acc = new Cons<T>(this.head, () -> this.tail().filter(temp, predicate));
                //LOG.debug(">>> true : {}", acc.head);
            }
            return this.tail().filter(acc, predicate);
        }

    }

    @Override
    public String toString() {
        return "Stream{" +
                "head=" + head +
                ", tail=" + tail +
                '}';
    }
}
