package hello.core;

import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/*
@Configuration: 설정 정보를 담당하는 클래스임을 나타냄
@ComponentScan: 스프링 빈을 다 긁어서 자동으로 스프링 빈으로 끌어올려야 함. @Component 어노테이션이 붙은 클래스들을 스캔해서 빈으로 등록함
 */
@Configuration
@ComponentScan(
        // 컴포넌트 스캔 시작 위치를 지정
        basePackages = "hello.core",
        // @Configuration도 @Component가 붙어있어서 스캔의 대상이 되는데
        // 여기서는 AppConfig, TestConfig등이 빈에 등록되면 안되기 때문에 제외 설정
        // 보통 설정 정보를 컴포넌트 스캔 대상에서 제외하진 않지만 기존 예제 코드를 최대한 남기고 유지하기 위해 이 방법을 선택
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoAppConfig {

//    @Bean(name = "memoryMemberRepository")
//    MemberRepository memberRepository() {
//        return new MemoryMemberRepository();
//    }
}
