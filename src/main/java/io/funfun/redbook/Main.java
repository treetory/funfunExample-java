package io.funfun.redbook;

import java.util.function.Predicate;

public class Main {

    public static void main(String[] args) {

        System.out.println("Hello World!");

        //System.out.println(FPList.cons(1, FPList.cons(2, FPList.none())));

        ConsList consList = new Cons(1, new Cons(2, new Cons(3, new Cons(4, Nil.getNil()))));

        //System.out.println(consList.toString());

        System.out.println(consList.map(Nil.getNil(), o -> (int)o + 1).toString());

        System.out.println(consList.filter(Nil.getNil(), o -> {
            if ((int)o != 1) {
                return true;
            } else {
                return false;
            }
        }).toString());

        System.out.println(consList.flatMap(Nil.getNil(), Main::apply).toString());

    }

    private static Object apply(Object consList1) {
        
    }
}
