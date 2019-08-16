package io.funfun.redbook.state;

import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.javatuples.Triplet;

import java.math.BigInteger;
import java.util.List;

public abstract class RNG {

    Long seed;

    public Long getSeed() {
        return this.seed;
    }

    // 목록 6.2 순수 함수적 난수 발생기
    public abstract Pair<BigInteger, RNG> nextInt();
    // 연습문제 6.1
    public abstract Pair<BigInteger, RNG> nonNegativeInt();
    // 연습문제 6.2
    public abstract Pair<Double, RNG> nextDouble();
    // 연습문제 6.3
    public abstract Triplet<BigInteger, Double, RNG> nextIntDouble();
    public abstract Triplet<Double, BigInteger, RNG> nextDoubleInt();
    public abstract Quartet<Double, Double, Double, RNG> nextDouble3();
    // 연습문제 6.4
    public abstract List<Pair<BigInteger, RNG>> nextIntList(int count);

}
