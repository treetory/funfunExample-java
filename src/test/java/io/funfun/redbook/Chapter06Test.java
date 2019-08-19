package io.funfun.redbook;

import io.funfun.redbook.state.RNG;
import io.funfun.redbook.state.Rand;
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

    @Test
    @DisplayName("[State] : Rand & Unit")
    void testUnit() {
        Rand<Integer, RNG> rand1 = new Rand<>();
        rand1.unit(15);
        //LOG.debug("{}{} --->  {}", System.lineSeparator(), rand1.getNumber(), rand1.getRng().getSeed());
        Rand<Integer, RNG> rand2 = new Rand<>();
        rand2.unit(35);
        //LOG.debug("{}{} --->  {}", System.lineSeparator(), rand2.getNumber(), rand2.getRng().getSeed());

        // unit 은 인자로 넘겨준 숫자와 난수발생기만 반환해야 한다.
        // 15를 넘기면 Rand 에 15가 입력되어야 한다.
        assertEquals(rand1.getNumber(), 15);
        // 35를 넘기면 Rand 에 35가 입력되어야 한다.
        assertEquals(rand2.getNumber(), 35);
        // rand1 과 rand2 모두 순수함수적인 RNG 를 이용하므로, 같은 seed 값을 가지고 있어야 한다.
        assertEquals(rand1.getRng().getSeed(), rand2.getRng().getSeed());
    }

    @Test
    @DisplayName("[State] : Rand & Map")
    void testMap() {
        Rand<BigInteger, RNG> rand = new Rand<BigInteger, RNG>();
        //rand.unit(15);
        LOG.debug("{}{} --->  {}", System.lineSeparator(), rand.getNumber(), rand.getRng().getSeed());
        Rand<BigInteger, RNG> _rand1 = rand.map(bigInteger -> BigInteger.valueOf(bigInteger.intValueExact() *2));
        LOG.debug("{}{} --->  {}", System.lineSeparator(), _rand1.getNumber(), _rand1.getRng().getSeed());
        Rand<BigInteger, RNG> _rand2 = _rand1.map(bigInteger -> BigInteger.valueOf(bigInteger.intValueExact() - 1));
        LOG.debug("{}{} --->  {}", System.lineSeparator(), _rand2.getNumber(), _rand2.getRng().getSeed());
    }

    @Test
    @DisplayName("[State] : Rand & Map with None Negative Integer")
    void testNonNegativeEven() {
        Rand<BigInteger, RNG> rand = new Rand<>();
        //rand.unit(BigInteger.valueOf(16L));

        Rand<BigInteger, RNG> rand1 = rand.nonNegativeEven();
        LOG.debug("{}{} --->  {}", System.lineSeparator(), rand1.getNumber(), rand1.getRng().getSeed());
        assertTrue((rand1.getNumber().divideAndRemainder(BigInteger.valueOf(2L))[1]).intValueExact() == 0);

        Rand<BigInteger, RNG> rand2 = rand1.nonNegativeEven();
        LOG.debug("{}{} --->  {}", System.lineSeparator(), rand2.getNumber(), rand2.getRng().getSeed());
        assertTrue((rand2.getNumber().divideAndRemainder(BigInteger.valueOf(2L))[1]).intValueExact() == 0);

        Rand<BigInteger, RNG> rand3 = rand2.nonNegativeEven();
        LOG.debug("{}{} --->  {}", System.lineSeparator(), rand3.getNumber(), rand3.getRng().getSeed());
        assertTrue((rand3.getNumber().divideAndRemainder(BigInteger.valueOf(2L))[1]).intValueExact() == 0);

        Rand<BigInteger, RNG> rand4 = rand3.nonNegativeEven();
        LOG.debug("{}{} --->  {}", System.lineSeparator(), rand4.getNumber(), rand4.getRng().getSeed());
        assertTrue((rand4.getNumber().divideAndRemainder(BigInteger.valueOf(2L))[1]).intValueExact() == 0);

        Rand<BigInteger, RNG> rand5 = rand4.nonNegativeEven();
        LOG.debug("{}{} --->  {}", System.lineSeparator(), rand5.getNumber(), rand5.getRng().getSeed());
        assertTrue((rand5.getNumber().divideAndRemainder(BigInteger.valueOf(2L))[1]).intValueExact() == 0);

        Rand<BigInteger, RNG> rand6 = rand5.nonNegativeEven();
        LOG.debug("{}{} --->  {}", System.lineSeparator(), rand6.getNumber(), rand6.getRng().getSeed());
        assertTrue((rand6.getNumber().divideAndRemainder(BigInteger.valueOf(2L))[1]).intValueExact() == 0);

    }

    @Test
    @DisplayName("[State] : Rand & Map to get double")
    void testRandNextDouble() {
        Rand<Double, RNG> rand = new Rand<>();
        Rand<Double, RNG> _rand = rand.nextDouble();
        LOG.debug("{}{} --->  {}", System.lineSeparator(), _rand.getNumber(), _rand.getRng().getSeed());
        assertTrue(_rand.getNumber() instanceof Double);
    }

    @Test
    @DisplayName("[State] : Rand & Map2")
    void testMap2() {
        Rand<?, RNG> randA = new Rand<>();
        Rand<?, RNG> randB = new Rand<>(Double.valueOf("19293829"));
        Rand<Float, RNG> randC = (Rand<Float, RNG>) randA.map2(randB, (i, i2) -> {
            return ((BigInteger) i).multiply((BigInteger) i2);
        });
        LOG.debug("{}{} ---> {}", System.lineSeparator(), randC.getNumber(), randC.getRng().getSeed());
    }
}
