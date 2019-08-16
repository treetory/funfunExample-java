package io.funfun.redbook.state;

import javafx.util.converter.BigIntegerStringConverter;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.function.Function;

public class Rand<T, RNG> {

    public static final Logger LOG = LoggerFactory.getLogger(Rand.class);

    private T number;
    private RNG rng;

    private final Long init_seed = 19283920L;

    public T getNumber() {
        return number;
    }

    public RNG getRng() {
        return rng;
    }

    public Rand() {
        this.rng = (RNG) new SimpleRNG(init_seed);
    }

    /**
     * 주어진 RNG 를 사용하지 않고 그대로 전달하는, RNG 상태 전이인 unit 함수
     *
     * @param   number
     * @return  Rand<T, RNG> (T 는 인자로 넘겨받은 숫자)
     */
    public Rand<T, RNG> unit(T number) {
        Rand<T, RNG> rand = new Rand<T, RNG>();
        this.number = number;
        //LOG.debug("{}RAND T : {} ---> {}", System.lineSeparator(), number, this.number);
        return rand;
    }

    /**
     * 상태 변화가 없이 (즉, RNG 의 seed 는 동일해야 한다), mapper 가 적용된 number 값을 담아서 반환
     *
     * @param   mapper (number 에 적용할 lambda 식)
     * @return  Rand<T, RNG> (T 는 mapper 가 적용된 것, RNG 는 원본 그대로)
     */
    public Rand<T, RNG> map(Function<T, T> mapper) {
        T number = mapper.apply(this.number);
        this.number = number;
        return this;
    }

    /**
     * BigInteger 를 쓰면서 연산식 사용하는 것이 복잡해짐...
     * 일단 위에 적용한 map 함수를 이용한다.
     * 다만, map 함수에 전달하는 mapper 가 even 값을 구하는 연산식인데,
     * BigInteger 의 특성상, primitive type 에서 사용하는 산술연산자를 사용할 수 없다.
     * 그래서 실제 식이 복잡해 보이나, i - (i % 2) 가 아래처럼 표현된 것이다.
     * TODO 타입 추론을 이용하면 코드가 더 깨끗해질 것인가... 요 부분은 아직 내가 잘...
     *
     * @return  Rand<T, RNG> (T 는 짝수)
     */
    public Rand<T, RNG> nonNegativeEven() {
        Pair<BigInteger, io.funfun.redbook.state.RNG> nonNegativeInt = ((SimpleRNG)this.getRng()).nonNegativeInt();
        this.number = (T) nonNegativeInt.getValue0();
        this.rng = (RNG) nonNegativeInt.getValue1();
        return this.map(t -> (T) ((BigInteger)t).subtract((nonNegativeInt.getValue0()).divideAndRemainder(BigInteger.valueOf(2L))[1]));
    }

}
