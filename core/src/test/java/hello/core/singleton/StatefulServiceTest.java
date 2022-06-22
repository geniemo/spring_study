package hello.core.singleton;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.assertThat;

class StatefulServiceTest {

    @Test
    void statefulServiceSingleton() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);
        
        // 쓰레드 A 사용자가 10000원 주문
        statefulService1.order("userA", 10000);
        // 쓰레드 B 사용자가 20000원 주문
        statefulService2.order("userB", 10000);

        // 쓰레드 A가 주문 금액을 조회
        int price =statefulService1.getPrice();
        System.out.println("price = " + price);

        // 쓰레드 A를 사용하는 사용자는 10000원짜리를 주문했는데 가격이 20000원으로 찍힐 수 있다.
        assertThat(statefulService1.getPrice()).isEqualTo(20000);
    }

    static class TestConfig {

        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }
    }
}