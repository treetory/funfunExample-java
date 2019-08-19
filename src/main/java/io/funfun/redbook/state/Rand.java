package io.funfun.redbook.state;

import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Rand<T extends Object, RNG> {

    public static final Logger LOG = LoggerFactory.getLogger(Rand.class);

    private T number;
    private SimpleRNG rng;

    private final BigInteger init_number = BigInteger.valueOf(1920203920L);
    private final Long init_seed = 19283920L;

    public T getNumber() {
        return number;
    }

    public SimpleRNG getRng() {
        return rng;
    }

    public Rand() {
        this.number = (T) init_number;
        this.rng = new SimpleRNG(init_seed);
    }

    public Rand(T t) {
        this.number = t;
        this.rng = new SimpleRNG(init_seed);
    }

    public Rand(T t, SimpleRNG rng) {
        this.number = t;
        this.rng = rng;
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
        this.rng = (SimpleRNG) nonNegativeInt.getValue1();
        return this.map(t -> (T) ((BigInteger)t).subtract((nonNegativeInt.getValue0()).divideAndRemainder(BigInteger.valueOf(2L))[1]));
    }

    // 연습문제 6.5
    /**
     * map 을 이용하여 double 타입의 난수를 반환하는 함수를 구현
     * TODO 이 또한 위와 마찬가지로... 타입 추론을 이용하면 코드가 더 깨끗해질 것인지...
     *
     * @return  Rand<Double, RNG> (난수는 Double 자료형)
     */
    public Rand<Double, RNG> nextDouble() {
        Pair<T, SimpleRNG> _next = this.getRng().next();
        this.number = _next.getValue0();
        this.rng = _next.getValue1();
        return (Rand<Double, RNG>) this.map(t -> (T) Double.valueOf(((BigInteger)this.getNumber()).doubleValue()));
    }

    // 연습문제 6.6

    /**
     * 연산을 하려면, 타입을 엄격하게 맞춰야 하는 것 때문에 코드가 지저분해지는 것 같다...
     * RandA 와 RandB 를 받아서 새로운 타입의 난수를 가진 RandC 를 반환하는 함수를 만드는 데,
     * java 에서 제공하는 FunctionalInterface 가 BiFunction 이 있어서 이것을 활용했다.
     * 만약 더 많은 인자를 받아서 처리해야 하면, FunctionalInterface 를 추가로 만들어서 쓰면된다.
     * -> <see>https://stackoverflow.com/questions/27872387/can-a-java-lambda-have-more-than-1-parameter</see>
     * -> 요약하자면, @FunctionalInterface 어노테이션을 붙여서 인자 개수를 generics 에 넣어서 apply 처리하는 인터페이스를 만들라는 것 같다.
     *
     * @param rb        (결과를 조합할 대상)
     * @param mapper    (조합하는 함수, 연산식)
     * @return Rand<? extends Object, RNG> (조합된 결과)
     */
    public Rand<?, RNG> map2(Rand<?, RNG> rb, BiFunction<T, T, ?> mapper) {

        LOG.debug("{}{} ---> {}", System.lineSeparator(), this.getNumber(), this.getNumber() instanceof BigInteger);
        LOG.debug("{}{} ---> {}", System.lineSeparator(), rb.getNumber(), rb.getNumber() instanceof BigInteger);

        try {

            BigInteger ra_value;
            if (this.getNumber() instanceof Integer) {
                ra_value = BigInteger.valueOf(Long.parseLong(Integer.valueOf((Integer)this.getNumber()).toString()));
            } else if (this.getNumber() instanceof Double) {
                ra_value = BigInteger.valueOf(Math.round((Double)this.getNumber()));
            } else if (this.getNumber() instanceof Long) {
                ra_value = BigInteger.valueOf((Long)this.getNumber());
            } else if (this.getNumber() instanceof Float) {
                ra_value = BigInteger.valueOf(Math.round((Float)this.getNumber()));
            } else if (this.getNumber() instanceof BigInteger) {
                ra_value = (BigInteger) this.getNumber();
            } else if (this.getNumber() instanceof String) {
                ra_value = BigInteger.valueOf(Long.parseLong((String) this.getNumber()));
            } else {
                throw new ClassCastException("Can't cast to Number formatted object.");
            }

            BigInteger rb_value;
            if (rb.getNumber() instanceof Integer) {
                rb_value = BigInteger.valueOf(Long.parseLong(Integer.valueOf((Integer)rb.getNumber()).toString()));
            } else if (rb.getNumber() instanceof Double) {
                rb_value = BigInteger.valueOf(Math.round((Double)rb.getNumber()));
            } else if (rb.getNumber() instanceof Long) {
                rb_value = BigInteger.valueOf((Long)rb.getNumber());
            } else if (rb.getNumber() instanceof Float) {
                rb_value = BigInteger.valueOf(Math.round((Float)rb.getNumber()));
            } else if (rb.getNumber() instanceof BigInteger) {
                rb_value = (BigInteger) rb.getNumber();
            } else if (rb.getNumber() instanceof String) {
                rb_value = BigInteger.valueOf(Long.parseLong((String) rb.getNumber()));
            } else {
                throw new ClassCastException("Can't cast to Number formatted object.");
            }

            return new Rand<>(mapper.apply((T)ra_value, (T)rb_value));

        } catch (Exception e) {
            LOG.error(e.getMessage());
            return new Rand<>(BigInteger.valueOf(-1L));
        }

    }
}
