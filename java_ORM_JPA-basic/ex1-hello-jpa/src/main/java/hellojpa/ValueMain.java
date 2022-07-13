package hellojpa;

public class ValueMain {

    public static void main(String[] args) {

        Integer a = 10;
        Integer b = a;

        System.out.println("a = " + a);
        System.out.println("b = " + b);

//        b.setValue()와 같은 것이 애초에 불가능하게 되어있다.

        Address address1 = new Address("city", "street", "10000");
        Address address2 = new Address("city", "street", "10000");
        // 같은 내용임에도 인스턴스가 다르므로 false
        System.out.println("(address1 == address2) = " + (address1 == address2));
        // 재정의하지 않으면 == 비교가 기본 동작이기 때문에 false로 나온다.
        // 재정의 한 후에는 true가 나온다.
        System.out.println("address1.equals(address2) = " + address1.equals(address2));

    }
}
