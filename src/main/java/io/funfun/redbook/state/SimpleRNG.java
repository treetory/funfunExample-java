package io.funfun.redbook.state;

import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;

public class SimpleRNG extends RNG {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleRNG.class);

    public SimpleRNG(Long seed) {
        this.seed = seed;
    }

    private static final long multiplier = 0x5DEECE66DL;
    private static final long addend = 0xBL;
    private static final long mask = (1L << 48) - 1;

    /**
     * integer overflow 때문에 rng 에서 생성하는 난수를 BigInteger 자료형으로 변경하였다.
     * 일반 Integer 자료형을 사용하면, seed 값을 long 으로 쓰기 때문에 난수 생성하다가 integer overflow 가 발생하는 문제가 있다.
     *
     * @return  Pair<BigInteger, RNG> (튜플을 반환한다. 튜플은 난수와 난수생성기를 가지고 있다.)
     */
    @Override
    public Pair<BigInteger, RNG> nextInt() {
        Long newSeed = (seed * multiplier + addend) & mask;
        RNG nextRNG = new SimpleRNG(newSeed);
        BigInteger n = BigInteger.valueOf(newSeed >>> 16);
        //LOG.debug("{} seed [{}] ---> newSeed [{}] / mask [{}] : [{}]", System.lineSeparator(), seed, newSeed, mask, 0xFFFFFFFFFFFFL);
        return Pair.with(n, nextRNG);
    }

    @Override
    public Pair<BigInteger , RNG> nonNegativeInt() {
        Pair<BigInteger, RNG> n = this.nextInt();
        //LOG.debug("{}{} ---> {}", System.lineSeparator(), n.getValue0(), n.getValue1().getSeed());
        BigInteger rn = n.getValue0();
        if (rn.intValue() > 0) {
            // 양수이며 MaxValue 이하일 때,
            return n;
        } else if (rn.intValue() == Integer.MIN_VALUE) {
            // MinValue 일 때
            return n.setAt0((rn.add(BigInteger.ONE)).abs());
        } else {
            // 음수일 때,
            return n.setAt0(rn.abs());
        }
    }

}
