package io.funfun.redbook.state;

import org.javatuples.Pair;

import java.math.BigInteger;

public abstract class RNG {

    Long seed;

    public Long getSeed() {
        return this.seed;
    }

    // 목록 6.2 순수 함수적 난수 발생기
    public abstract Pair<BigInteger , RNG> nextInt();
    // 연습문제 6.1
    public abstract Pair<BigInteger , RNG> nonNegativeInt();
}
