package io.funfun.redbook.functions;

@FunctionalInterface
public interface Fun1<A, B> {

    default B apply(A a) {
        return checkedApply(a);
    }

    B checkedApply(A a);

}
