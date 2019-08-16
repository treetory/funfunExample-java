package io.funfun.redbook;

import io.funfun.redbook.state.RNG;
import io.funfun.redbook.state.SimpleRNG;
import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.javatuples.Triplet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Chapter06Test {

    private static final Logger LOG = LoggerFactory.getLogger(Chapter06Test.class);

    @Test
    @DisplayName("[State] : Simple Random Number Generator")
    void testSimpleRNG() {
        // 난수 발생기를 생성
        RNG rng = new SimpleRNG(421232138281912L);
        // 모두 같은 난수 발생기로부터 난수를 발생시켜본다.
        Pair pair1 = rng.nextInt();
        Pair pair2 = rng.nextInt();
        Pair pair3 = rng.nextInt();
        // 각각 비교하는데,
        // SimpleRNG 는 순수 함수적 난수 발생기이기 때문에
        // 1. 발생한 난수 값이 같아야 한다.
        assertEquals(pair1.getValue0(), pair2.getValue0());
        // 2. 난수 생성시 쓰이는 상태값(seed) 도 같아야 한다.
        assertEquals(((RNG)pair1.getValue1()).getSeed(), ((RNG)pair2.getValue1()).getSeed());
        // -> 테스트 결과 OK 이면, 순수 함수적이다.

        // 아래도 마찬가지... 1, 2, 3 모두 같은 결과를 내뱉어야 한다. 그래서 크로스 체크했다.
        assertEquals(pair1.getValue0(), pair3.getValue0());
        assertEquals(((RNG)pair1.getValue1()).getSeed(), ((RNG)pair3.getValue1()).getSeed());

        assertEquals(pair2.getValue0(), pair3.getValue0());
        assertEquals(((RNG)pair2.getValue1()).getSeed(), ((RNG)pair3.getValue1()).getSeed());

        // 이것은 반복 테스트, 10000번 실험
        Pair<BigInteger, RNG> pair = null;
        for (int i=0; i<10000; i++) {
            if (pair == null) {
                pair = rng.nextInt();
            } else {
                pair = pair.getValue1().nextInt();
            }
            LOG.debug("{} ---> {} : {}", pair.getValue0(), pair.getValue1().getSeed(), (pair.getValue0()).intValue() >= 0 ? true : false);
        }

    }

    @Test
    @DisplayName("[State] : None Negative Random Integer Number")
    void testNonNegativeInt() {
        // 난수 발생기를 생성
        // 샘플 10000개 돌려봄
        RNG rng = new SimpleRNG(42L);
        Pair<BigInteger, RNG> pair = null;
        for (int i=0; i<10000; i++) {
            if (pair == null) {
                pair = rng.nonNegativeInt();
            } else {
                pair = pair.getValue1().nonNegativeInt();
            }
            LOG.debug("{} ---> {} : {}", pair.getValue0(), pair.getValue1().getSeed(), (pair.getValue0()).intValue() >= 0 ? true : false);
        }

    }

    @Test
    @DisplayName("[State] : Random Double Number")
    void testNextDouble() {
        // 난수 발생기를 생성
        // 샘플 10000개 돌려봄
        RNG rng = new SimpleRNG(42L);
        Pair<Double, RNG> pair = null;
        for (int i=0; i<10000; i++) {
            if (pair == null) {
                pair = rng.nextDouble();
            } else {
                pair = pair.getValue1().nextDouble();
            }
            Pair<Double, RNG> finalPair = pair;
            assertTrue(() -> {
                return (finalPair.getValue0() instanceof Double);
            });
            LOG.debug("{} ---> {} : {}", pair.getValue0(), pair.getValue1().getSeed(), (pair.getValue0()).intValue() >= 0 ? true : false);
        }
    }

    @Test
    @DisplayName("[State] : IntDouble")
    void testNextIntDouble() {
        RNG rng = new SimpleRNG(42L);
        Triplet<BigInteger, Double, RNG> n = rng.nextIntDouble();
        LOG.debug("{} ---> {} : {}", n.getValue0(), n.getValue1(), n.getValue2().getSeed());
    }

    @Test
    @DisplayName("[State] : DoubleInt")
    void testNextDoubleInt() {
        RNG rng = new SimpleRNG(42L);
        Triplet<Double, BigInteger, RNG> n = rng.nextDoubleInt();
        LOG.debug("{} ---> {} : {}", n.getValue0(), n.getValue1(), n.getValue2().getSeed());
    }

    @Test
    @DisplayName("[State] : Double3")
    void testNextDouble3() {
        RNG rng = new SimpleRNG(42L);
        Quartet<Double, Double, Double, RNG> n = rng.nextDouble3();
        LOG.debug("{} ---> {} --->  {} : {}", n.getValue0(), n.getValue1(), n.getValue2(), n.getValue3().getSeed());
    }

    @Test
    @DisplayName("[State] : Random Integer List")
    void testNextIntList() {
        int count = 5000;
        RNG rng = new SimpleRNG(42L);
        List<Pair<BigInteger, RNG>> result = rng.nextIntList(count);
        result.stream().forEach(objects -> LOG.debug("{}{} ---> {}", System.lineSeparator(), objects.getValue0(), objects.getValue1().getSeed()));
        assertEquals(count, result.size());
    }

}
