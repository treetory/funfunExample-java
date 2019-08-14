package io.funfun.redbook.state;

import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RNGMain {

    private static final Logger LOG = LoggerFactory.getLogger(RNGMain.class);

    public static void main(String[] args) {

        RNG rng = new SimpleRNG(42L);
        Pair pair1 = rng.nextInt();
        Pair pair2 = rng.nextInt();
        Pair pair3 = rng.nextInt();

        LOG.debug("{}{}", System.lineSeparator(), pair1.toArray());
        LOG.debug("{}{}", System.lineSeparator(), pair2.toArray());
        LOG.debug("{}{}", System.lineSeparator(), pair3.toArray());

    }
}
