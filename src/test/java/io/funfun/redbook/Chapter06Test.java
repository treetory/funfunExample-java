package io.funfun.redbook;

import io.funfun.redbook.state.RNG;
import io.funfun.redbook.state.SimpleRNG;
import org.javatuples.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    }

    @Test
    @DisplayName("[State] : Simple Random Number Generator")
    void testNonNegativeInt() {
        // 난수 발생기를 생성
        // 샘플 1000개 돌려봄
        RNG rng = new SimpleRNG(42L);
        Pair<Integer, RNG> pair = null;
        for (int i=0; i<1000; i++) {
            if (pair == null) {
                pair = rng.nonNegativeInt();
            } else {
                pair = pair.getValue1().nonNegativeInt();
            }
            LOG.debug("{} ---> {}", pair.getValue0(), pair.getValue1().getSeed());
            //assertTrue(pair.getValue0() >= 0 && pair.getValue0() <= Integer.MAX_VALUE );
        }

    }

}
