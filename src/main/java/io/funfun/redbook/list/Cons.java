package io.funfun.redbook.list;

public class Cons<T> extends ConsList<T> {

    public Cons(T head, ConsList<T> tail) {
        this.head = head;
        this.tail = tail;
    }

    public static <T> Cons<T> cons(T head, ConsList<T> tail) {
        return new Cons<>(head, tail);
    }

    public boolean isEmpty() {
        return false;
    }

    @Override
    public T get(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException((Integer.toString(index)));
        }

        ConsList<T> cur = this;
        for (int curIdx = index; curIdx > 0; --curIdx) {
            if (cur.tail().isEmpty()) {
                throw new IndexOutOfBoundsException((Integer.toString(index)));
            }
            cur = cur.tail();
        }

        return cur.head();
    }

    @Override
    public int size() {
        return 1 + this.tail.size();
    }
}
