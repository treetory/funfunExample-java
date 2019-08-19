package io.funfun.redbook.state;

import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.javatuples.Triplet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class SimpleRNG<T> extends RNG<T> {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleRNG.class);

    public SimpleRNG(Long seed) {
        this.seed = seed;
    }

    private static final long multiplier = 0x5DEECE66DL;
    private static final long addend = 0xBL;
    private static final long mask = (1L << 48) - 1;

    @Override
    public Pair<T, SimpleRNG> next() {
        Long newSeed = (seed * multiplier + addend) & mask;
        return (Pair<T, SimpleRNG>) Pair.with(BigInteger.valueOf(newSeed >>> 16), new SimpleRNG(newSeed));
    }

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
    public Pair<BigInteger, RNG> nonNegativeInt() {
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
     *  Triplet 의 결과를 반환하는 것이 목표
     *
     * @return  Triplet<BigInteger, Double, RNG> (난수는 2가지 타입의 난수, 발생기는 하나)
     */
    @Override
    public Triplet<BigInteger, Double, RNG> nextIntDouble() {
        Pair<BigInteger, RNG> first = this.nextInt();
        Pair<Double, RNG> second = first.getValue1().nextDouble();

        LOG.debug("{}{} ---> {} : {}", System.lineSeparator(), first.getValue1().getSeed(), second.getValue1().getSeed(), Math.subtractExact(first.getValue1().getSeed(), second.getValue1().getSeed()));

        return Triplet.with(first.getValue0(), second.getValue0(), second.getValue1());
    }

    /**
     *  Triplet 의 결과를 반환하는 것이 목표
     *
     * @return  Triplet<Double, BigInteger, RNG> (난수는 2가지 타입의 난수, 발생기는 하나)
     */
    @Override
    public Triplet<Double, BigInteger, RNG> nextDoubleInt() {
        Pair<Double, RNG> first = this.nextDouble();
        Pair<BigInteger, RNG> second = first.getValue1().nextInt();

        LOG.debug("{}{} ---> {} : {}", System.lineSeparator(), first.getValue1().getSeed(), second.getValue1().getSeed(), Math.subtractExact(first.getValue1().getSeed(), second.getValue1().getSeed()));

        return Triplet.with(first.getValue0(), second.getValue0(), second.getValue1());
    }

    /**
     *  Quartet 의 결과를 반환하는 것이 목표
     *
     * @return  Quartet<Double, Double, Double, RNG> (난수는 3가지 타입의 난수, 발생기는 하나)
     */
    @Override
    public Quartet<Double, Double, Double, RNG> nextDouble3() {
        Pair<Double, RNG> first = this.nextDouble();
        Pair<Double, RNG> second = first.getValue1().nextDouble();
        Pair<Double, RNG> third = second.getValue1().nextDouble();
        return Quartet.with(first.getValue0(), second.getValue0(), third.getValue0(), third.getValue1());
    }

    /**
     * 직전에 생성했던 난수로부터 반환된 발생기를 이용하여 난수를 계속 생성
     *
     * @param   count (난수 생성할 개수)
     * @return  List<Pair<BigInteger, RNG>> (난수를 발생시킨 결과의 목록, 목록의 개수는 count 와 같다)
     */
    @Override
    public List<Pair<BigInteger, RNG>> nextIntList(int count) {
        List<Pair<BigInteger, RNG>> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            if (i == 0) {
                list.add(this.nextInt());
            } else {
                list.add(list.get(i-1).getValue1().nextInt());
            }
        }
        return list;
    }

}
