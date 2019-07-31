package io.funfun.redbook.stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

// TODO 2019-07-30 Iterable 을 implement 하는 것을 빼는 것이 좋겠다는 의견을 받음
// TODO 2019-07-30 Nil 을 항상 acc 의 인자로 넘기지 않도록, 편의함수를 제공하는 것이 좋겠다는 의견을 받음
public abstract class Stream<T> implements Iterable<T> {

    private static final Logger LOG = LoggerFactory.getLogger(Stream.class);

    // head 도 lazy 가 적용되어야 한다 -> Lazy 가 적용되지 않으면, eager valuation 이 발생한다.
    Lazy<T> head;
    Lazy<Stream<T>> tail;

    // 그러므로 head() 함수도 tail() 처럼 평가된 값을 return 하도록 get() 을 실행하여 반환하도록 한다.
    public abstract T head();
    public abstract Stream<T> tail();

    // 단일 element 를 받아서 Stream<T> 객체를 생성
    public static <T> Stream<T> of(T element) {
        return new Cons<>(element, Nil::getNil);
    }

    // 여러 elements 를 받아서 Stream<T> 객체를 생성
    @SafeVarargs
    public static <T> Stream<T> of (T... elements) {
        Stream<T> stream = Nil.getNil();
        for (T element : elements) {
            final Stream<T> temp = stream;
            stream = new Cons<>(element, () -> temp);
        }
        return stream.reverse(Nil.getNil());
    }

    // predicate 평가식을 받아서 그 평가식의 결과에 해당하는 것들로 이뤄진 Stream<T> 반환
    public Stream<T> filter(Stream<T> acc, Predicate<T> predicate) {

        if (this instanceof Nil) {
            LOG.debug("<<<  : {}", acc.toString());
            return acc.reverse(Nil.getNil());
        } else {
            //LOG.debug(">>> head : {}", this.head);
            //LOG.debug(">>> tail : {}", this.tail());
            Stream<T> temp = acc;
            if (predicate.test(this.head())) {
                acc = new Cons<>(this.head(), () -> this.tail().filter(temp, predicate));
                //LOG.debug(">>> true : {}", acc.head);
            }
            return this.tail().filter(acc, predicate);
        }

    }

    // Stream 의 head 를 끄집어 내가면서 숫자를 세서 그 카운트를 acc 에 누적시켰다가, head 가 Nil 일 때 acc 를 반환
    public int length(int acc) {
        if (this instanceof Nil) {
            return acc;
        } else {
            int temp = acc;
            ++temp;
            return this.tail().length(temp);
        }
    }

    // 하나씩 head 를 drop 시키면서 개수를 차감한 뒤, drop 시킬 개수가 0이 되면 그 때의 Stream<T> 를 반환
    public Stream<T> drop(int count) {
        if (count <= 0) {
            return this;
        } else {
            int temp = count;
            --temp;
            return this.tail().drop(temp);
        }
    }

    // head 를 하나씩 꺼내서 acc 에 옮겨 담고, 더 이상 옮겨 담을 게 없을 때 acc 반환
    public Stream<T> reverse(Stream<T> acc) {
        if (this instanceof Nil) {
            return acc;
        } else {
            Stream<T> temp = acc;
            //LOG.debug("{} >>> {}", System.lineSeparator(), this.head());
            acc = new Cons<>(this.head(), () -> temp);
            return this.tail().reverse(acc);
        }
    }

    // head 에 붙임
    public Stream<T> addHead(T element) {
        return new Cons<>(element, () -> this);
    }

    // head 를 접고 남은 tail 을 return 하도록 처리
    public Stream<T> foldLeft(Stream<T> acc) {
        //LOG.debug("{}>>> acc : {}", acc);
        if (this instanceof Nil) {
            return acc.reverse(Nil.getNil());
        } else {
            acc = this.tail();
            return acc;
        }
    }

    // tail 이 Nil 이면, head 를 acc 에 담지 않는 방식으로 구현 -> tail 이 Nil 일 때까지 한번 가고, reverse 하면서 한번 더 순회
    public Stream<T> foldRight(Stream<T> acc) {
        final Stream<T> temp = acc;
        if (this.tail() instanceof Nil) {
            return acc.reverse(Nil.getNil());
        } else {
            acc = new Cons<>(this.head(), () -> temp);
            return this.tail().foldRight(acc);
        }
    }

    // 하나씩 옮겨 담다가 Stream 에 원소가 없을 때, element 를 head 에 두고 acc 를 tail 에 둔 Stream 을 만들고, 뒤집어서 반환
    public Stream<T> append(Stream<T> acc, T element) {
        Stream<T> temp = acc;
        if (this instanceof Nil) {
            if (element != null) {
                temp = new Cons<>(element, () -> acc);
            }
            return temp.reverse(Nil.getNil());
        } else {
            temp = new Cons<>(this.head(), () -> acc);
            return this.tail().append(temp, element);
        }
    }

    // head 값엔 mapper 함수를 적용하여 생성된 값을 받아서 넘기고, tail 은 mapper 가 적용되어 값이 평가되지 않은 상태
    public <R> Stream<R> map(Stream<R> acc, Function<T, R> mapper) {
        if (this instanceof Nil) {
            return acc.reverse(Nil.getNil());
        } else {
            return this.tail().map(new Cons<>(mapper.apply(this.head()), () -> acc), mapper);
        }
    }

    // ConsList 의 flatMap2 와 비슷한 방법으로 구현했다. -> 근데 Iterable 을 확장하지 아니하고선 Function 넘겨받을 때, 처리할 방법이 생각이 안난다...
    public <R> Stream<R> flatMap(Stream<R> acc, Function<T, ? extends Iterable<R>> mapper) {
        if (this instanceof Nil) {
            return acc.reverse(Nil.getNil());
        } else {
            // 원래의 것은 불변성 유지하기 위해 옮겨 담는다.
            final Stream<R> temp = acc;
            // 1. mapper 를 적용한 결과(type -> List, 그래서 Stream 을 Iterable interface 를 구현시켰음)를 받고
            // 2. 상기 결과를 Stream 으로 변환 후에
            // 3. acc 에 해당 결과를 append 한다.
            // 4. 해당 결과를 reverse 처리한 것을 acc 에 옮겨 담은 후에, 꼬리 재귀 시킨다.
            Stream<R> result = ((Stream<R>) Stream.of(((List<R>) mapper.apply(this.head())).toArray())).append(temp);
            return this.tail().flatMap(result.reverse(Nil.getNil()), mapper);
        }
    }

    // TODO 2019-07-30 map 을 적용 후, flatten 을 적용하는 방식으로 flatMap 을 구현해보는 것이 좋겠다는 리뷰결과가 있다.
    /*
    public <R> Stream<R> flatMap(Stream<R> acc, Function<T, R> mapper) {
        return Nil.getNil();
    }
    */

    // flatMap 을 위해선 이게 필요했다... -> stream 을 acc 에 append 하는 것만 처리, 위의 append 는 Stream 에 추가하고자 하는 원소 하나를 붙이는 함수...
    private Stream<T> append(Stream<T> acc) {
        if (this instanceof Nil) {
            return acc.reverse(Nil.getNil());
        } else {
            final Stream<T> temp = acc;
            acc = new Cons<>(this.head(), () -> temp);
            //LOG.debug("{}>>> acc.head() : {}", System.lineSeparator(), acc.head());
            //LOG.debug("{}>>> acc.tail().head() : {}", System.lineSeparator(), (acc.tail() instanceof Nil) == false ? acc.tail().head() : "empty");
            return this.tail().append(acc);
        }
    }

    @Override
    public String toString() {
        return "Stream{" +
                "head=" + head +
                ", tail=" + tail +
                '}';
    }
}
