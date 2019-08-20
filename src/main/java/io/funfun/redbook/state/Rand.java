package io.funfun.redbook.state;

import io.funfun.redbook.list.ConsList;
import io.funfun.redbook.list.Nil;
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

    public Rand(T t, RNG rng) {
        this.number = t;
        this.rng = (SimpleRNG) rng;
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

    public Rand<T, RNG> next() {
        Pair<T, SimpleRNG> _next = this.getRng().next();
        this.number = _next.getValue0();
        this.rng = _next.getValue1();
        return this;
    }

    public Rand<BigInteger, RNG> nextInt() {
        this.next();
        return (Rand<BigInteger, RNG>) this;
    }

    // 연습문제 6.5
    /**
     * map 을 이용하여 double 타입의 난수를 반환하는 함수를 구현
     * TODO 이 또한 위와 마찬가지로... 타입 추론을 이용하면 코드가 더 깨끗해질 것인지...
     *
     * @return  Rand<Double, RNG> (난수는 Double 자료형)
     */
    public Rand<Double, RNG> nextDouble() {
        this.next();
        return (Rand<Double, RNG>) this.map(t -> (T) Double.valueOf(((BigInteger)this.getNumber()).doubleValue()));
    }

    // 연습문제 6.6
    /**
     * 연산을 하려면, 타입을 엄격하게 맞춰야 하는 것 때문에 코드가 지저분해지는 것 같다...
     * RandA(this) 와 RandB(rb) 를 받아서 새로운 타입의 난수를 가진 RandC 를 반환하는 함수를 만드는 데,
     * java 에서 제공하는 FunctionalInterface 가 BiFunction 이 있어서 이것을 활용했다.
     * 만약 더 많은 인자를 받아서 처리해야 하면, FunctionalInterface 를 추가로 만들어서 쓰면된다.
     * -> <see>https://stackoverflow.com/questions/27872387/can-a-java-lambda-have-more-than-1-parameter</see>
     * -> 요약하자면, @FunctionalInterface 어노테이션을 붙여서 인자 개수를 generics 에 넣어서 apply 처리하는 인터페이스를 만들라는 것 같다.
     *
     * @param rb        (결과를 조합할 대상)
     * @param mapper    (조합하는 함수, 연산식)
     * @return Rand<? extends Object, RNG> (조합된 결과)
     */
    public Rand<?, RNG> map2(Rand<? extends Object, RNG> rb, BiFunction<T, T, ?> mapper) {

        LOG.debug("{}{} ---> {}", System.lineSeparator(), this.getNumber(), this.getNumber() instanceof BigInteger);
        LOG.debug("{}{} ---> {}", System.lineSeparator(), rb.getNumber(), rb.getNumber() instanceof BigInteger);
        LOG.debug("{}{} ---> {}", System.lineSeparator(), rb.getNumber(), rb.getNumber() instanceof ConsList);

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

    /**
     * 위에 map2 를 간단히 하면 아래와 같은데...
     * number 를 이용한 연산을 하면 java.lang.ClassCastException: java.lang.Double cannot be cast to java.math.BigInteger 발생
     * 산술 연산은 타입의 영향을 받아서... 어쩔 수 없는 것인가...
     * map3 를 만든 이유는 sequence 를 해결하기 위함이다.
     * TODO map2 와 map3 로 나누지 않고도 동일한 동작이 가능하게 할 수 있는지 확인 필요...
     *
     * @param rb
     * @param mapper
     * @return
     */
    public Rand<?, RNG> map3(Rand<? extends Object, RNG> rb, BiFunction<T, T, ?> mapper) {
        return new Rand<>(mapper.apply((T) this.getNumber(), (T) rb.getNumber()));
    }

    /**
     * 두 개의 Rand 를 조합하여, A 와 B 의 쌍을 반환하는 함수
     *
     * @param   rb  (조합할 대상)
     * @return  Rand<Pair<?, ?>, RNG> (RandA 와 RandB 쌍이 담긴 결과)
     */
    public Rand<Pair<T, T>, RNG> both(Rand<T, RNG> rb) {
        Rand<Pair<T, T>, RNG> _both = (Rand<Pair<T, T>, RNG>) this.map2(rb, (t, t2) -> {
            Pair<T, T> _temp = Pair.with(this.getNumber(), rb.getNumber());
            return (T) _temp;
        });
        return _both;
    }

    /**
     * random number 로 BigInteger 와 Double 값을 쌍으로 담아 반환하는 함수 (BigInteger, Double 순서)
     *
     * @return  Rand<Pair<BigInteger, Double>, RNG> (number 부분에 튜플이 들어가서 반환됨)
     */
    public Rand<Pair<BigInteger, Double>, RNG> randIntDouble() {
        Rand<T, RNG> rb = (Rand<T, RNG>)((new Rand<Double, RNG>()).nextDouble());
        Rand<Pair<T, T>, RNG> _both = this.both(rb);
        return new Rand<>((Pair<BigInteger, Double>)_both.getNumber());
    }

    /**
     * random number 로 Double 과 BigInteger 값을 쌍으로 담아 반환하는 함수 (Double, BigInteger 순서)
     *
     * @return  Rand<Pair<Double, BigInteger>, RNG> (number 부분에 튜플이 들어가서 반환됨)
     */
    public Rand<Pair<Double, BigInteger>, RNG> randDoubleInt() {
        Rand<T,RNG> rb = (Rand<T, RNG>)((new Rand<BigInteger, RNG>()).nextInt());
        Rand<Pair<T, T>, RNG> _both = this.both(rb);
        return new Rand<>((Pair<Double, BigInteger>)_both.getNumber());
    }

    //연습문제 6.7
    public Rand<ConsList<T>, RNG> sequence(ConsList<T> numbers) {
        // 하나씩 우측에서 접어서 acc 에 옮겨 담고,
        ConsList<T> acc = numbers.foldRight(Nil.getNil());
        // 옮겨 담아진 것에 random number 를 붙이고
        Rand<ConsList<T>, RNG> randB = new Rand<>(acc);
        // 붙여서 완성된 ConsList 와 RNG 를 반환한다.
        return (Rand<ConsList<T>, RNG>) this.map3(randB, (t, t2) -> ((ConsList)t2).append(ConsList.asList(t)));
    }

    /**
     * 아래 3가지는 map, map2 로는 잘 작성할 수 없는 함수의 예제이다
     *
     * @param   n (0이상 n미만의 정수 난수를 발생하기 위해 생성된 난수를 나누는 수)
     * @return  Rand<T, RNG> (0이상 n미만의 난수와 생성기)
     */
    public Rand<T, RNG> nonNegativeLessThan1(int n) {
        return this.map(t -> {
            Integer _t = ((BigInteger) t)./*intValueExact()*/intValue();
            Integer mod = _t % n;
            return (T) BigInteger.valueOf(Long.parseLong(mod.toString()));
        });
    }

    public Rand<T, RNG> nonNegativeLessThan2(int n) {
        return this.map(t -> {
            Integer _t = ((BigInteger) t)./*intValueExact()*/intValue();
            Integer mod = _t % n;
            return _t + (n-1) - mod >= 0 ? (T) BigInteger.valueOf(Long.parseLong(mod.toString())) : (T) this.getRng().nextInt().getValue0();
        });
    }

    public Rand<T, RNG> nonNegativeLessThan3(int n) {
        Pair<BigInteger, RNG> t = this.getRng().nonNegativeInt();
        Integer _t = (t.getValue0())./*intValueExact()*/intValue();
        Integer mod = _t % n;
        Rand<T, RNG> result = new Rand<>((T) BigInteger.valueOf(Long.parseLong(mod.toString())), t.getValue1());
        if (_t + (n-1) - mod >= 0) {
            return result;
        } else {
            return result.nonNegativeLessThan3(n);
        }
    }

    // 연습문제 6.8
    /**
     * flatMap 구현
     *
     * @param   mapper (연산)
     * @return  Rand<T, RNG> (연산이 적용된 결과)
     */
    public Rand<T, RNG> flatMap(Function<T, T> mapper) {
        return new Rand<>(mapper.apply(this.getNumber()), (RNG)this.getRng());
    }

    /**
     * flatMap 을 이용하여 0이상 n 미만의 정수 반환하는 함수
     *
     * @param   n (나누는 수)
     * @return  Rand<T, RNG> (0이상 n 미만의 정수와 난수 생성기)
     */
    public Rand<T, RNG> nonNegativeLessThanUsingFlatMap(int n) {
        return this.flatMap(t -> (T)((BigInteger) t).divideAndRemainder(BigInteger.valueOf(Long.parseLong(Integer.valueOf(n).toString())))[1]);
    }

    // 연습문제 6.9
    /**
     * flatMap 을 이용하여 map 을 구현
     *
     * @param mapper
     * @return
     */
    public Rand<T, RNG> map_usingFlatMap(Function<T, T> mapper) {
        return this.flatMap(mapper);
    }

    /**
     * flatMap 을 이용하여 map2 를 구현
     *
     * @param rb
     * @param mapper
     * @return
     */
    public Rand<T, RNG> map2_usingFlatMap(Rand<T, RNG> rb, BiFunction<T, T, T> mapper) {
        return this.flatMap(t -> mapper.apply(t, rb.getNumber()));
    }

    /**
     * 하나 모자라는 오류가 남아있는 주사위 굴리기 함수 (0~5가 반환됨)
     *
     * @return
     */
    public Rand<T, RNG> rollDice() {
        return this.nonNegativeLessThan3(6);
    }

    /**
     * 위 오류를 교정한 주사위 굴리기 함수 (1~6이 반환됨)
     *
     * @return
     */
    public Rand<T, RNG> rollDice2() {
        return this.nonNegativeLessThan3(6).map(t -> (T)((BigInteger) t).add(BigInteger.ONE));
    }

}
