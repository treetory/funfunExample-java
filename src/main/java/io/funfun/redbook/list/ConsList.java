package io.funfun.redbook.list;

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

    // flatMap2 를 위해 구현... 여러 아이템을 받아서 ConsList 형태로 리턴할 수 있도록 만들었다.
    public static <T> ConsList<T> asList(T... a) {

        ConsList<T> temp = Nil.getNil();

        for (T t : a) {
            temp = new Cons<>(t, temp);
        }

        return temp;
    }

    // 순서 뒤집기
    public ConsList<T> reverse(ConsList<T> acc) {
        if (this instanceof  Nil) {
            return acc;
        } else {
            ConsList<T> temp = acc;
            acc = new Cons<>(this.head, temp);
            return this.tail.reverse(acc);
        }
    }

    // 해당 아이템을 head 에 붙이기
    public ConsList<T> addHead(ConsList<T> item) {
        //LOG.debug("{} : {}", item.head, this);
        return new Cons<>(item.head, this);
    }

    // 입력받은 n개의 아이템을 앞에서부터 drop 시키기
    public ConsList<T> drop(int count) {
        if (count == 0) {
            return this;
        } else {
            int cnt = count;
            cnt--;
            return this.tail.drop(cnt--);
        }
    }

    // map 함수 -> 입력받은 mapper 함수를 각 원소에 적용
    public <R> ConsList<R> map(ConsList<R> acc, Function<T, R> mapper) {
        if (this instanceof Nil) {
            //LOG.debug("head : {}, tail : {}, acc : {}", this.head, this.tail, acc);
            return acc.reverse(Nil.getNil());
        } else {
            //LOG.debug("head : {}, tail : {}, acc : {}", this.head, this.tail, acc);
            return this.tail.map(new Cons<>(mapper.apply(this.head), acc), mapper);
        }
    }

    // filter 함수 -> 입력받은 predicate 에 해당하는 것들만 누적시킨다.
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

    // fold, flatten, append 를 쓸 수 있는데... -> 일단 잘 모르겠어서 iterable 을 extends 하고, 각 원소별로 head 에 붙이도록 구현했다...
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

    // append 를 이용해서 구현
    public <R> ConsList<R> flatMap2(ConsList<R> acc, Function<T, ? extends AbstractList<R>> mapper) {
        if (this instanceof Nil) {
            return acc.reverse(Nil.getNil());
        } else {
            List<R> mapped = (mapper.apply(this.head));
            //LOG.debug("cur : {}", mapped);
            //LOG.debug("before : {}", acc);
            acc = ((ConsList<R>) mapped).reverse(Nil.getNil()).append(acc);
            //LOG.debug("after  : {}", acc);
            return this.tail.flatMap2(acc, mapper);
        }
    }

    // 현재 아이템을 accumulator 에 붙이는 형식으로 구현... -> 사실 맞는건지 잘 모르겠다. 일단 결과는 원한대로 나왔으나...
    private ConsList<T> append(ConsList<T> acc/*, ConsList<T> item*/) {
        if (this instanceof Nil) {
            return acc;
        } else {
            ConsList<T> temp = acc;
            //LOG.debug("{}", this.head);
            temp = new Cons<>(this.head, acc);
            acc = temp;
        }
        //LOG.debug("{}", acc);
        return this.tail.append(acc);
    }

    @Override
    public String toString() {
        return "ConsList{" +
                "head=" + head +
                ", tail=" + tail +
                '}';
    }
}