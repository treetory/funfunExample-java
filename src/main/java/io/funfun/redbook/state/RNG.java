package io.funfun.redbook.state;

import org.javatuples.Pair;

public abstract class RNG {

    Long seed;
    public abstract Pair<Integer, RNG> nextInt();

}
