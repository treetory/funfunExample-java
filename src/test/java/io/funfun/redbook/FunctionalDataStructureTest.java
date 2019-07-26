package io.funfun.redbook;

import static org.junit.jupiter.api.Assertions.*;

import io.funfun.redbook.list.Cons;
import io.funfun.redbook.list.ConsList;
import io.funfun.redbook.option.Option;
import io.funfun.redbook.stream.Nil;
import io.funfun.redbook.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class FunctionalDataStructureTest {

    private static final Logger LOG = LoggerFactory.getLogger(FunctionalDataStructureTest.class);

    @Test
    @DisplayName("[ConsList] : making list")
    void createConsList() {
        ConsList<Integer> consList = new Cons<Integer>(1, new Cons(2, new Cons(3, new Cons(4, io.funfun.redbook.list.Nil.getNil()))));
        assertNotNull(consList);
    }

    @Test
    @DisplayName("[ConsList] : addHead (prepend)")
    void addHeadConsList() {
        ConsList<Integer> consList = new Cons<Integer>(1, new Cons(2, new Cons(3, new Cons(4, io.funfun.redbook.list.Nil.getNil()))));
        ConsList<Integer> prepended = consList.addHead(new Cons<>(0, io.funfun.redbook.list.Nil.getNil()));
        assertNotSame(consList, prepended);
        assertIterableEquals(prepended, new Cons<>(0, consList));
    }

    @Test
    @DisplayName("[ConsList] : dropHead")
    void dropHeadConsList() {
        ConsList<Integer> consList = new Cons<Integer>(1, new Cons(2, new Cons(3, new Cons(4, io.funfun.redbook.list.Nil.getNil()))));
        ConsList<Integer> dropped = consList.drop(1);
        assertNotSame(consList, dropped);
        assertIterableEquals(consList, new Cons<>(1, dropped));
    }

    @Test
    @DisplayName("[ConsList] : reverse")
    void reverseConsList() {
        ConsList<Integer> consList = new Cons<Integer>(1, new Cons(2, new Cons(3, new Cons(4, io.funfun.redbook.list.Nil.getNil()))));
        ConsList<Integer> reversed = consList.reverse(io.funfun.redbook.list.Nil.getNil());
        assertIterableEquals(consList, reversed.reverse(io.funfun.redbook.list.Nil.getNil()));
    }

    @Test
    @DisplayName("[ConsList] : map")
    void mapConsList() {
        ConsList<Integer> consList = new Cons<Integer>(1, new Cons(2, new Cons(3, new Cons(4, io.funfun.redbook.list.Nil.getNil()))));
        ConsList<Integer> mapped = consList.map(io.funfun.redbook.list.Nil.getNil(), o -> o + 1);
        assertIterableEquals(consList, mapped.map(io.funfun.redbook.list.Nil.getNil(), integer -> integer -1));
    }

    @Test
    @DisplayName("[ConsList] : filter")
    void filterConsList() {
        ConsList<Integer> consList = new Cons<Integer>(1, new Cons(2, new Cons(3, new Cons(4, io.funfun.redbook.list.Nil.getNil()))));
        ConsList<Integer> filtered = consList.filter(io.funfun.redbook.list.Nil.getNil(), o -> {
            if (o > 1) {
                return true;
            } else {
                return false;
            }
        });
        assertNotSame(consList, filtered);
        assertIterableEquals(filtered, new Cons<>(2, new Cons<>(3, new Cons<>(4, io.funfun.redbook.list.Nil.getNil()))));
    }

    @Test
    @DisplayName("[ConsList] : flatMap")
    void flatMapConsList() {
        ConsList<String> stringList = new Cons("One flew over the cuckoo's nest", new Cons("To Kill a muckingbird", new Cons("Gone with the wind", io.funfun.redbook.list.Nil.getNil())));
        ConsList<String> flatMapped = stringList.flatMap2(io.funfun.redbook.list.Nil.getNil(), s -> ConsList.asList(s.split(" ")));
        assertNotSame(stringList, flatMapped);
        assertIterableEquals(flatMapped,
                new Cons<>("One",
                        new Cons<>("flew",
                                new Cons<>("over",
                                        new Cons<>("the",
                                                new Cons<>("cuckoo's",
                                                        new Cons<>("nest",
                                                                new Cons<>("To",
                                                                        new Cons<>("Kill",
                                                                                new Cons<>("a",
                                                                                        new Cons<>("muckingbird",
                                                                                                new Cons<>("Gone",
                                                                                                        new Cons<>("with",
                                                                                                                new Cons<>("the",
                                                                                                                        new Cons<>("wind", io.funfun.redbook.list.Nil.getNil()))))))))))))))
        );
    }

    @Test
    @DisplayName("[ConsList] : foldLeft")
    void foldLeftConsList() {
        ConsList<Integer> consList = new Cons<Integer>(1, new Cons(2, new Cons(3, new Cons(4, io.funfun.redbook.list.Nil.getNil()))));
        ConsList<Integer> leftFolded = consList.foldLeft(io.funfun.redbook.list.Nil.getNil());
        assertNotSame(consList, leftFolded);
        assertIterableEquals(consList, leftFolded.addHead(new Cons<>(1, io.funfun.redbook.list.Nil.getNil())));
    }

    @Test
    @DisplayName("[ConsList] : foldRight")
    void foldRightConsList() {
        ConsList<Integer> consList = new Cons<Integer>(1, new Cons(2, new Cons(3, new Cons(4, io.funfun.redbook.list.Nil.getNil()))));
        ConsList<Integer> rightFolded = consList.foldRight(io.funfun.redbook.list.Nil.getNil());
        assertNotSame(consList, rightFolded);
        assertIterableEquals(rightFolded, new Cons<>(1, new Cons<>(2, new Cons<>(3, io.funfun.redbook.list.Nil.getNil()))));
    }

    @Test
    @DisplayName("[ConsList] : length")
    void lengthConsList() {
        ConsList<Integer> consList = ConsList.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        int length = consList.length(0);
        assertEquals(10, length);
    }

    @Test
    @DisplayName("[Stream] : making Stream being consisted in single element")
    void createStream1() {
        Stream<String> stringStream = Stream.of("AAA");
        ConsList<String> consList = ConsList.asList("AAA");

        assertEquals(stringStream.head(), consList.head());
    }

    @Test
    @DisplayName("[Stream] : making Stream being consisted in multi element")
    void createStream2() {
        Stream<String> stringStream = Stream.of("AAA", "BBB", "CCC");
        ConsList<String> consList = ConsList.asList("AAA", "BBB", "CCC");

        assertEquals(stringStream.head(), consList.head());
    }

    @Test
    @DisplayName("[Stream] : filter")
    void filterStream() {
        Stream<String> stringStream = Stream.of("AAA", "BBB", "CCC");
        ConsList<String> consList = ConsList.asList("AAA", "BBB", "CCC");

        assertEquals(
                stringStream.filter(io.funfun.redbook.stream.Nil.getNil(), s -> "AAA".equals(s)).head(),
                consList.filter(io.funfun.redbook.list.Nil.getNil(), s -> "AAA".equals(s)).head()
                );
    }

    @Test
    @DisplayName("[Stream] : reverse")
    void reverseStream() {
        Stream<String> stringStream = Stream.of("AAA", "BBB", "CCC", "DDD", "EEE");

        Stream<String> reversed = stringStream.reverse(io.funfun.redbook.stream.Nil.getNil());
        assertEquals(reversed.head(), "EEE");
        assertEquals(stringStream.head(), reversed.reverse(io.funfun.redbook.stream.Nil.getNil()).head());
    }

    @Test
    @DisplayName("[Stream] : length")
    void lengthStream() {
        Stream<String> stringStream = Stream.of("AAA", "BBB", "CCC", "DDD", "EEE");
        int length = stringStream.length(0);

        assertEquals(length, 5);
    }

    @Test
    @DisplayName("[Stream] : drop")
    void dropStream() {
        Stream<String> stringStream = Stream.of("AAA", "BBB", "CCC", "DDD", "EEE");
        Stream<String> dropped = stringStream.drop(3);

        assertEquals(dropped.head(), Stream.of("DDD", "EEE").head());
    }

    @Test
    @DisplayName("[Stream] : addHead (prepend)")
    void addHeadStream() {
        Stream<String> stringStream = Stream.of("AAA", "BBB", "CCC", "DDD", "EEE");
        Stream<String> added = stringStream.addHead("ZZZ");

        assertEquals(added.head(), "ZZZ");
        assertEquals(added.tail().head(), "AAA");
    }

    @Test
    @DisplayName("[Stream] : foldLeft")
    void foldLeftStream() {
        Stream<String> stringStream = Stream.of("AAA", "BBB", "CCC", "DDD", "EEE");
        Stream<String> folded = stringStream.foldLeft(Nil.getNil());

        assertEquals(folded.head(), "BBB");
        assertEquals(folded.tail().head(), "CCC");
        assertEquals(folded.tail().tail().head(), "DDD");
        assertEquals(folded.tail().tail().tail().head(), "EEE");
    }

    @Test
    @DisplayName("[Stream] : foldRight")
    void foldRightStream() {
        Stream<String> stringStream = Stream.of("AAA", "BBB", "CCC", "DDD", "EEE");
        Stream<String> folded = stringStream.foldRight(Nil.getNil());

        assertEquals(folded.head(), "AAA");
        assertEquals(folded.tail().head(), "BBB");
        assertEquals(folded.tail().tail().head(), "CCC");
        assertEquals(folded.tail().tail().tail().head(), "DDD");
        assertEquals(folded.tail().tail().tail().tail(), Nil.getNil());
    }

    @Test
    @DisplayName("[Stream] : append")
    void appendStream() {
        Stream<String> stringStream = Stream.of("AAA", "BBB", "CCC", "DDD", "EEE");
        Stream<String> appended = stringStream.append(Nil.getNil(), "FFF");

        assertEquals(appended.head(), "AAA");
        assertEquals(appended.tail().head(), "BBB");
        assertEquals(appended.tail().tail().head(), "CCC");
        assertEquals(appended.tail().tail().tail().head(), "DDD");
        assertEquals(appended.tail().tail().tail().tail().head(), "EEE");
        assertEquals(appended.tail().tail().tail().tail().tail().head(), "FFF");
    }

    @Test
    @DisplayName("[Stream] : map")
    void mapStream() {
        Stream<Integer> integerStream = Stream.of(1, 2, 3, 4, 5);
        Stream<Integer> mapped = integerStream.map(Nil.getNil(), integer -> integer * 2);

        assertEquals(mapped.head(), 2);
        assertEquals(mapped.tail().head(), 4);
        assertEquals(mapped.tail().tail().head(), 6);
        assertEquals(mapped.tail().tail().tail().head(), 8);
        assertEquals(mapped.tail().tail().tail().tail().head(), 10);
    }

    @Test
    @DisplayName("[Stream] : flatMap")
    void flatMapStream() {
        Stream<String> stringStream = Stream.of("One flew over the cuckoo's nest", "To Kill a muckingbird", "Gone with the wind");
        Stream<String> mapped = stringStream.flatMap(Nil.getNil(), s -> Arrays.asList(s.split(" ")) );

        assertEquals(mapped.head(), "One");
        assertEquals(mapped.tail().head(), "flew");
        assertEquals(mapped.tail().tail().head(), "over");
        assertEquals(mapped.tail().tail().tail().head(), "the");
        assertEquals(mapped.tail().tail().tail().tail().head(), "cuckoo's");
        assertEquals(mapped.tail().tail().tail().tail().tail().head(), "nest");
        assertEquals(mapped.tail().tail().tail().tail().tail().tail().head(), "To");
    }

    @Test
    @DisplayName("[Option] : of")
    void ofOption() {
        Option<String> stringOption = Option.of("AAA");
        //LOG.debug("{}", stringOption.get().toString());
        assertEquals(stringOption.toString(), "AAA");

        Option<Integer> integerOption = Option.of(1);
        assertEquals(integerOption.toString(), String.valueOf(1));

        Option<Float> floatOption = Option.of(1f);
        assertEquals(floatOption.toString(), String.valueOf(1f));

        Option<Long> longOption = Option.of(1L);
        assertEquals(longOption.toString(), String.valueOf(1L));

        //byte[] arr = "AAA".getBytes();
        //Option<byte[]> byteOption = Option.of(arr);
        //assertEquals(arr, byteOption.get());
        // [B@55a1c291 -> [B@55a1c291 로 같은데... 왜 assertEqual 은 FailedError 를 주는 걸까...
    }

    @Test
    @DisplayName("[Option] : filter")
    void filterOption() {
        Option<String> stringOption = Option.of("AAA");
        Option<String> filtered1 = stringOption.filter(s -> "AAA".equals(s));
        assertEquals(filtered1.get().toString(), "AAA");
        Option<String> filtered2 = stringOption.filter(s -> !"AAA".equals(s));
        assertEquals(filtered2.get(), io.funfun.redbook.option.Nil.getNil());
    }

    @Test
    @DisplayName("[Option] : map")
    void mapOption() {
        Option<Integer> integerOption = Option.of(1);
        Option<Integer> mapped = integerOption.map(integer -> integer + 2);
        assertEquals(mapped.get().toString(), "3");
    }

    @Test
    @DisplayName("[Option] : getOrElse")
    void getOrElseOption() {
        Option<String> stringOption = Option.of("AAA");
        assertEquals(stringOption.getOrElse(), stringOption);

        Option<Integer> integerOption = io.funfun.redbook.option.Nil.getNil();
        assertEquals(integerOption.getOrElse(), io.funfun.redbook.option.Nil.getNil());
    }

}
