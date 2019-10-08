package io.funfun.redbook.state;

import io.funfun.redbook.functions.Fun1;
import org.javatuples.Pair;

import java.util.function.Function;

public class State<S, A> {

    private final Fun1<? super S, ? extends Pair<A, S>> stateFun;

    public State(Fun1<? super S, ? extends Pair<A, S>> stateFun) {
        this.stateFun = stateFun;
    }

    public Pair<A, S> run(S s) {
        return stateFun.apply(s);
    }

    public static <A> State<A, A> get() {
        return new State<>(a -> Pair.with(a, a));
    }

    public static <S> State<S, Unit> set(S s) {
        return new State<>(s1 -> Pair.with(Unit.UNIT, s));
    }

    public static <S> State<S, Unit> modify(Function<S, S> f) {
        return new State<>(s -> Pair.with(Unit.UNIT, f.apply(s)));
    }

    // 연습문제 6.10
    public <S, T> State<S, T> unit(T t) {
        return new State<>(s -> Pair.with(t, s));
    }
/*
    public <S, T, R> State<S, R> flatMap(State<S, T> state, Function<T, R> f) {
        State<S, A> temp = new State<S, A>(s -> (Pair<A, S>) state.run(s));
        return temp.
        //return new State<>(s -> State.set(s) -> f.apply())
    }
*/
}
