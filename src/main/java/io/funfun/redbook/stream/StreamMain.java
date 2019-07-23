package io.funfun.redbook.stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamMain {

    private static final Logger LOG = LoggerFactory.getLogger(StreamMain.class);

    public static void main(String[] args) {

        Stream<String> before = Stream.of("AAA", "BBB", "CCC")
                .filter(Nil.getNil(), s -> !s.equals("CCC"))
                .filter(Nil.getNil(), s -> !s.equals("BBB"));
        LOG.debug("{}{}", System.lineSeparator(), before.toString());

    }
}
