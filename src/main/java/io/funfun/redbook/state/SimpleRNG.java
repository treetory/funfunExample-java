package io.funfun.redbook.state;

import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class SimpleRNG extends RNG {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleRNG.class);

    public SimpleRNG(Long seed) {
        this.seed = seed;
    }
    //private static final long multiplier = 0x5DEECE66DL;
    //private static final long addend = 0xBL;
    //private static final long mask = (1L << 48) - 1;

    // TODO 현재 integer overflow 가 발생한다. 수정해야 한다. 근데... 같은 코드를 스칼라로 실행하면 괜찮다... 이거 뭐냐...
    @Override
    public Pair<Integer, RNG> nextInt() {
        //Long newSeed = (seed * multiplier + addend) & mask;
        Long newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL;
        RNG nextRNG = new SimpleRNG(newSeed);
        Integer n = Math.toIntExact(newSeed >>> 16);
        //LOG.debug("{} seed [{}] ---> newSeed [{}] / mask [{}] : [{}]", System.lineSeparator(), seed, newSeed, mask, 0xFFFFFFFFFFFFL);
        return Pair.with(n, nextRNG);
    }

    @Override
    public Pair<Integer, RNG> nonNegativeInt() {
        Pair<Integer, RNG> n = this.nextInt();
        //LOG.debug("{}{} ---> {}", System.lineSeparator(), n.getValue0(), n.getValue1().getSeed());
        Integer rn = n.getValue0();
        if (rn > 0) {
            // 양수이며 MaxValue 이하일 때,
            return n;
        } else if (rn == Integer.MIN_VALUE) {
            // MinValue 일 때
            return n.setAt0(-(rn+1));
        } else {
            // 음수일 때,
            return n.setAt0(-(rn));
        }
    }

}
