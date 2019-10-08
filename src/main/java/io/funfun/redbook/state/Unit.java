package io.funfun.redbook.state;

public final class Unit {

    public static final Unit UNIT = new Unit();

    private Unit() {}

    @Override
    public String toString() {
        return "Unit{}";
    }

}
