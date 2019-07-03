package io.funfun.redbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        System.out.println("Hello World!");

        // 1. Sample 로 사용할 ConsList 만든다.
        ConsList<Integer> consList = new Cons<Integer>(1, new Cons(2, new Cons(3, new Cons(4, Nil.getNil()))));

        LOG.debug("{}[[[[TARGET]]]]{}{}", System.lineSeparator(), System.lineSeparator(), consList.toString());

        ConsList<Integer> mapped = consList.map(Nil.getNil(), o -> o + 1);

        LOG.debug("{}[[[[MAPPED]]]]{}{}", System.lineSeparator(), System.lineSeparator(), mapped.toString());

        ConsList<Integer> filtered = consList.filter(Nil.getNil(), o -> {
                    if (o > 3) {
                        return true;
                    } else {
                        return false;
                    }
                });

        LOG.debug("{}[[[[FILTERED]]]]{}{}", System.lineSeparator(), System.lineSeparator(), filtered.toString());

        //ConsList stringList = new Cons("One flw over the cuckoo's nest", new Cons("To Kill a muckingbird", new Cons("Gone with the wind", Nil.getNil())));

        //ConsList<Integer> flatMapped = consList.flatMap(Nil.getNil(), list -> { });

        //LOG.debug("{}[[[[flatMapped]]]]{}{}", System.lineSeparator(), System.lineSeparator(), flatMapped.toString());

    }


}
