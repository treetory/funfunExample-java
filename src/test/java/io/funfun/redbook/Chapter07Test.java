package io.funfun.redbook;

import io.funfun.redbook.concurrent.Par;
import io.funfun.redbook.concurrent.Parallel;
import io.funfun.redbook.list.ConsList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Chapter07Test {

    private static final Logger LOG = LoggerFactory.getLogger(Chapter07Test.class);

    @Test
    @DisplayName("[Concurrent] : foldLeft sum")
    void testSumByFoldLeft() {

        ConsList<Integer> ints = ConsList.asList(1, 2, 3, 4, 5);
        Integer _sum = Par.sum(0, ints);
        LOG.debug("{}{}", System.lineSeparator(), _sum);
        assertEquals(15, _sum);

    }

    @Test
    @DisplayName("[Concurrent] : divided and conquer")
    void testSumUsingDiviedAndConquer() {

        Integer _sum = Par.sumUsingDiviedAndConquer(new Integer[]{1,2,3,4,5});
        LOG.debug("{}{}", System.lineSeparator(), _sum);
        assertEquals(15, _sum);

    }

    @Test
    @DisplayName("[Concurrent] : Using new custom data structure(Par)")
    void testSumUsingPar() {

        Integer _sum = Par.sum(new Integer[]{1,2,3,4,5});
        LOG.debug("{}{}", System.lineSeparator(), _sum);
        assertEquals(15, _sum);

    }

}
