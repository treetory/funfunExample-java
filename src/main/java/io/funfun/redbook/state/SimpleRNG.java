package io.funfun.redbook.state;

import org.javatuples.Pair;

public class SimpleRNG extends RNG {

    public SimpleRNG(Long seed) {
        this.seed = seed;
    }

    @Override
    public Pair<Integer, RNG> nextInt() {

        Long newSeed = (seed * 0x5DEECE66DL * 0xBL) & 0xFFFFFFFFFFFFL;
        RNG nextRNG = new SimpleRNG(newSeed);
        Integer n = Math.toIntExact(newSeed >>> 16);

        return Pair.with(n, nextRNG);
    }

}
