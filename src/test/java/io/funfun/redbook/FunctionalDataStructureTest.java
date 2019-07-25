package io.funfun.redbook;

import static org.junit.jupiter.api.Assertions.*;

import io.funfun.redbook.list.Cons;
import io.funfun.redbook.list.ConsList;
import io.funfun.redbook.list.Nil;
import io.funfun.redbook.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FunctionalDataStructureTest {

    @Test
    @DisplayName("[ConsList] : making list")
    void createConsList() {
        ConsList<Integer> consList = new Cons<Integer>(1, new Cons(2, new Cons(3, new Cons(4, Nil.getNil()))));
        assertNotNull(consList);
    }

    @Test
    @DisplayName("[ConsList] : addHead (prepend)")
    void addHeadConsList() {
        ConsList<Integer> consList = new Cons<Integer>(1, new Cons(2, new Cons(3, new Cons(4, Nil.getNil()))));
        ConsList<Integer> prepended = consList.addHead(new Cons<>(0, Nil.getNil()));
        assertNotSame(consList, prepended);
        assertIterableEquals(prepended, new Cons<>(0, consList));
    }

    @Test
    @DisplayName("[ConsList] : dropHead")
    void dropHeadConsList() {
        ConsList<Integer> consList = new Cons<Integer>(1, new Cons(2, new Cons(3, new Cons(4, Nil.getNil()))));
        ConsList<Integer> dropped = consList.drop(1);
        assertNotSame(consList, dropped);
        assertIterableEquals(consList, new Cons<>(1, dropped));
    }

    @Test
    @DisplayName("[ConsList] : reverse")
    void reverseConsList() {
        ConsList<Integer> consList = new Cons<Integer>(1, new Cons(2, new Cons(3, new Cons(4, Nil.getNil()))));
        ConsList<Integer> reversed = consList.reverse(Nil.getNil());
        assertIterableEquals(consList, reversed.reverse(Nil.getNil()));
    }

    @Test
    @DisplayName("[ConsList] : map")
    void mapConsList() {
        ConsList<Integer> consList = new Cons<Integer>(1, new Cons(2, new Cons(3, new Cons(4, Nil.getNil()))));
        ConsList<Integer> mapped = consList.map(Nil.getNil(), o -> o + 1);
        assertIterableEquals(consList, mapped.map(Nil.getNil(), integer -> integer -1));
    }

    @Test
    @DisplayName("[ConsList] : filter")
    void filterConsList() {
        ConsList<Integer> consList = new Cons<Integer>(1, new Cons(2, new Cons(3, new Cons(4, Nil.getNil()))));
        ConsList<Integer> filtered = consList.filter(Nil.getNil(), o -> {
            if (o > 1) {
                return true;
            } else {
                return false;
            }
        });
        assertNotSame(consList, filtered);
        assertIterableEquals(filtered, new Cons<>(2, new Cons<>(3, new Cons<>(4, Nil.getNil()))));
    }

    @Test
    @DisplayName("[ConsList] : flatMap")
    void flatMapConsList() {
        ConsList<String> stringList = new Cons("One flew over the cuckoo's nest", new Cons("To Kill a muckingbird", new Cons("Gone with the wind", Nil.getNil())));
        ConsList<String> flatMapped = stringList.flatMap2(Nil.getNil(), s -> ConsList.asList(s.split(" ")));
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
                                                                                                                        new Cons<>("wind", Nil.getNil()))))))))))))))
        );
    }

    @Test
    @DisplayName("[ConsList] : foldLeft")
    void foldLeftConsList() {
        ConsList<Integer> consList = new Cons<Integer>(1, new Cons(2, new Cons(3, new Cons(4, Nil.getNil()))));
        ConsList<Integer> leftFolded = consList.foldLeft(Nil.getNil());
        assertNotSame(consList, leftFolded);
        assertIterableEquals(consList, leftFolded.addHead(new Cons<>(1, Nil.getNil())));
    }

    @Test
    @DisplayName("[ConsList] : foldRight")
    void foldRightConsList() {
        ConsList<Integer> consList = new Cons<Integer>(1, new Cons(2, new Cons(3, new Cons(4, Nil.getNil()))));
        ConsList<Integer> rightFolded = consList.foldRight(Nil.getNil());
        assertNotSame(consList, rightFolded);
        assertIterableEquals(rightFolded, new Cons<>(1, new Cons<>(2, new Cons<>(3, Nil.getNil()))));
    }

    @Test
    @DisplayName("[ConsList] : length")
    void lengthConsList() {
        ConsList<Integer> consList = ConsList.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        int length = consList.length(0);
        assertEquals(10, length);
    }

    @Test
    @DisplayName("[Stream] : making Stream")
    void createStream() {
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
                consList.filter(Nil.getNil(), s -> "AAA".equals(s)).head()
                );
    }
}
