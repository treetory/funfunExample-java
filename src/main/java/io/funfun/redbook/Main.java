package io.funfun.redbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        System.out.println("Hello World!");

        // 1. Sample 로 사용할 ConsList 만든다.
        ConsList consList = new Cons(1, new Cons(2, new Cons(3, new Cons(4, Nil.getNil()))));

        LOG.debug("{}[[[[TARGET]]]]{}{}", System.lineSeparator(), System.lineSeparator(), consList.toString());

        ConsList mapped = consList.map(Nil.getNil(), o -> (int)o + 1);

        LOG.debug("{}[[[[MAPPED]]]]{}{}", System.lineSeparator(), System.lineSeparator(), mapped.toString());

        ConsList filtered = consList.filter(Nil.getNil(), o -> {
                    if ((int)o > 3) {
                        return true;
                    } else {
                        return false;
                    }
                });

        LOG.debug("{}[[[[FILTERED]]]]{}{}", System.lineSeparator(), System.lineSeparator(), filtered.toString());

    }

}
