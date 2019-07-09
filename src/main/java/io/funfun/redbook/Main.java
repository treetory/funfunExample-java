package io.funfun.redbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        System.out.println("Hello World!");

        // 1. Sample 로 사용할 ConsList 만든다.
        ConsList<Integer> consList = new Cons<Integer>(1, new Cons(2, new Cons(3, new Cons(4, Nil.getNil()))));

        //LOG.debug("{}[[[[TARGET]]]]{}{}", System.lineSeparator(), System.lineSeparator(), consList.toString());

        // 2. ConsList 에 구현한 map 함수를 테스트 해본다.
        ConsList<Integer> mapped = consList.map(Nil.getNil(), o -> o + 1);

        //LOG.debug("{}[[[[MAPPED]]]]{}{}", System.lineSeparator(), System.lineSeparator(), mapped.toString());

        // 3. ConsList 에 구현한 filter 함수를 테스트 해본다.
        ConsList<Integer> filtered = consList.filter(Nil.getNil(), o -> {
                    if (o > 3) {
                        return true;
                    } else {
                        return false;
                    }
                });

        //LOG.debug("{}[[[[FILTERED]]]]{}{}", System.lineSeparator(), System.lineSeparator(), filtered.toString());

        // 4. ConsList 에 구현한 flatMap 함수를 테스트 해본다.
        ConsList<String> stringList = new Cons("One flew over the cuckoo's nest", new Cons("To Kill a muckingbird", new Cons("Gone with the wind", Nil.getNil())));

        // java8 의 Stream 에서 제공하는 flatMap 을 이용하여 처리해본 것
        String[] strs = { "One flew over the cuckoo's nest", "To Kill a muckingbird", "Gone with the wind" };
        Stream<String> stream = Arrays.stream(strs).flatMap(s -> Arrays.asList(s.split(" ")).stream());
        //stream.forEach(s -> LOG.debug("{}", s));

        // ConsList 를 구현한 것에 의존하여 처리해볼 것
        ConsList<String> flatMapped = stringList.flatMap(Nil.getNil(), s -> Arrays.asList(s.split(" ")));

        LOG.debug("{}[[[[flatMapped]]]]{}{}", System.lineSeparator(), System.lineSeparator(), flatMapped.toString());

    }


}
