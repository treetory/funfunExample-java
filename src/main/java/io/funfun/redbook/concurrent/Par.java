package io.funfun.redbook.concurrent;

import io.funfun.redbook.list.ConsList;
import io.funfun.redbook.list.Nil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class Par<T> {

    private static final Logger LOG = LoggerFactory.getLogger(Par.class);

    private T t;

    public Par() {};

    public Par(T _t) {
        t = _t;
    }
/*
    public static Par<T> unit(T t) {
        return new Par(t);
    }
*/
    public T get() {
        return t;
    }

    public T get(Par<T> p) {
        return p.t;
    }

    public static Integer sum(Integer sumValue, ConsList<Integer> ints) {
        LOG.debug("{}", ints.toString());
        sumValue += ints.head();
        if (ints.tail() instanceof Nil) {
            return sumValue;
        } else {
            return sum(sumValue, ints.foldLeft(Nil.getNil()));
        }
    }

    public static Integer sumUsingDiviedAndConquer(Integer[] ints) {
        if (ints.length <= 1) {
            return ints.length == 0 ? 0 : ints[0];
        } else {
            return sum(0, ConsList.asList(Arrays.copyOfRange(ints, 0, (ints.length+1)/2))) + sum(0, ConsList.asList(Arrays.copyOfRange(ints, (ints.length+1)/2, ints.length)));
        }
    }

    public static Integer sum(Integer[] ints) {
        if (ints.length <= 1) {
            return ints.length == 0 ? 0 : ints[0];
        } else {
            Integer[] l = Arrays.copyOfRange(ints, 0, (ints.length+1)/2);
            Integer[] r = Arrays.copyOfRange(ints, (ints.length+1)/2, ints.length);
            Par<Integer> sumL = new Par(sum(l));
            Par<Integer> sumR = new Par(sum(r));
            return sumL.get() + sumR.get();

            //return new Par()
        }
    }

}
