package hello.core.singleton;

public class SingletonService {

    // 자기 자신을 static으로 가지고 있는다.
    private static final SingletonService instance = new SingletonService();

    // 조회할 때는 얘를 사용
    public static SingletonService getInstance() {
        return instance;
    }

    // 외부에서 마음대로 SingletonService 객체를 생성할 수 없도록 private 생성자를 정의
    private SingletonService() {

    }

    public void logic() {
        System.out.println("싱글톤 객체 로직 호출");
    }
}
