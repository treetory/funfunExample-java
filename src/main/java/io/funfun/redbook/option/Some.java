package io.funfun.redbook.option;

public class Some<T> extends Option<T> {

    public Some(T value) {
        this.value = value;
    }

    @Override
    public Option<T> get() {
        return new Some<>(this.value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
