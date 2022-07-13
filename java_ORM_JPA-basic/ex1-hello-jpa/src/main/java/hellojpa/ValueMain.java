package hellojpa;

public class ValueMain {

    public static void main(String[] args) {

        Integer a = 10;
        Integer b = a;

        System.out.println("a = " + a);
        System.out.println("b = " + b);

//        b.setValue()와 같은 것이 애초에 불가능하게 되어있다.
    }
}
