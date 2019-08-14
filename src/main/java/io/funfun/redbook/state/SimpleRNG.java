package io.funfun.redbook.state;

import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.javatuples.Triplet;
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

    /**
     * nextInt() 를 사용하여 구현하는 것이 문제의 요구사항
     * 양수는 생성된 난수+생성기 를 반환, 생성된 난수가 MIN_VALUE 와 같으면 난수에 1을 더한 후, 절대값을 씌움
     * 음수일 때는 절대값을 씌운다.
     *
     * -> 구현하느라 linear congruential pseudorandom number generator 관련해서 찾아보게 된다...
     * Donald E. Knuth 교수의 <i>The Art of Computer Programming,</i> Volume 3:
     * <i>Seminumerical Algorithms</i>, section 3.2.1 에 이 내용이 있다고 한다...
     *
     * @return  Pair<BigInteger, RNG> (튜플을 반환한다. 튜플은 난수(양의 정수)와 난수생성기를 가지고 있다.)
     */
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

    /**
     *  nextInt() 를 사용하되, Double 자료형의 난수를 반환하는 것이 목표
     *
     * @return  Pair<BigInteger, RNG> (튜플을 반환한다. 튜플은 난수와 난수생성기를 가지고 있다.)
     */
    @Override
    public Pair<Double, RNG> nextDouble() {
        Pair<BigInteger, RNG> n = this.nextInt();
        return Pair.with(n.getValue0().doubleValue(), n.getValue1());
    }

    /**
     *
     *
     * @return
     */
    @Override
    public Triplet<BigInteger, Double, RNG> nextIntDouble() {
        Pair<BigInteger, RNG> _int = this.nextInt();
        Pair<Double, RNG> _double = this.nextDouble();

        LOG.debug("{}{} ---> {} : {}", System.lineSeparator(), _int.getValue1().getSeed(), _double.getValue1().getSeed(), Math.subtractExact(_int.getValue1().getSeed(), _double.getValue1().getSeed()));

        return Triplet.with(_int.getValue0(), _double.getValue0(), _int.getValue1());
    }

    @Override
    public Triplet<Double, BigInteger, RNG> nextDoubleInt() {
        Pair<BigInteger, RNG> _int = this.nextInt();
        Pair<Double, RNG> _double = this.nextDouble();

        LOG.debug("{}{} ---> {} : {}", System.lineSeparator(), _int.getValue1().getSeed(), _double.getValue1().getSeed(), Math.subtractExact(_int.getValue1().getSeed(), _double.getValue1().getSeed()));

        return Triplet.with(_double.getValue0(), _int.getValue0(), _int.getValue1());
    }

    @Override
    public Quartet<Double, Double, Double, RNG> nextDouble3() {
        Pair<Double, RNG> _double = this.nextDouble();
        return Quartet.with(_double.getValue0(), _double.getValue0(), _double.getValue0(), _double.getValue1());
    }


}
