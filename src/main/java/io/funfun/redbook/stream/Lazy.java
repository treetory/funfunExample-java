package io.funfun.redbook.stream;

import java.util.Objects;
import java.util.function.Supplier;

// Stream 의 tail 부분이 아직 평가되지 않은 상태로 만들기 위해 감쌀 객체가 필요했다...
// Lazy 를 구현하기 위해 https://www.vavr.io/ 와 https://speedment.com/ 를 참고했다...
// 아주 친절하게 코드도 오픈해주므로... -> vavr 을 자주 보게 될 것 같다.
public final class Lazy<T> implements Supplier<T>{

    private transient volatile Supplier<T> supplier;
    private T value;

    // Constructor 의 모양이 이렇게 되는 이유는... 내부 인자가 아직 평가되지 않기 위해, supplier 만 받아두는 것 같다.
    private Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    // 2019-07-30 리뷰 결과 : head 도 Lazy 가 적용되어 있어야 한다고 한다. 그래서 head 에 Lazy 를 적용해 줄 수 있도록, T 타입의 인자를 받았을 때, 해당 객체에 Lazy 를 적용해줄 수 있는 생성자를 두었다.
    private Lazy(T value) {
        this.value = value;
    }

    public static <T> Lazy<T> of(T element) {
        Objects.requireNonNull(element);
        return new Lazy<>(element);
    }

    public static <T> Lazy<T> of(Supplier<T> supplier) {
        Objects.requireNonNull(supplier);
        if (supplier instanceof Lazy) {
            return (Lazy<T>) supplier;
        } else {
            return new Lazy<>(supplier);
        }
    }

    // 이 때, 비로소 평가가 이뤄진다. -> value 를 주거나, value 를 평가하여 그 결과를 준다.
    @Override
    public T get() {
        return this.supplier == null ? value : computeValue();
    }

    // 이것을 통해서 비로소 value 를 평가한다. -> synchronized 를 걸어준 건... 혹시나 꼬일까봐...?
    private synchronized T computeValue() {
        final Supplier<T> s = supplier;
        if (s != null) {
            value = s.get();
            supplier = null;
        }
        return value;
    }
}