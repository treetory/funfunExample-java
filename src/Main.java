import io.funfun.redbook.FPList;
import io.funfun.redbook.Option;

import java.util.Optional;

public class Main {

    public static void main(String[] args) {

        System.out.println("Hello World!");

        System.out.println(FPList.cons(1, FPList.cons(2, FPList.none())));

    }

}
