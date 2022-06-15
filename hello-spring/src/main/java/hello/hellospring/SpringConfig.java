package hello.hellospring;

import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import hello.hellospring.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    // 이렇게 하면 스프링일 뜰 때 @Configuration을 읽고
    // @Bean을 읽어서 멤버 서비스를 스프링 빈에 등록해준다.
    // 아래와 같이 작성하면 멤버 서비스와 멤버 리포지토리를 스프링 빈에 등록하고
    // 빈에 등록되어 있는 멤버 리포지토리를 멤버 서비스에 넣어준다.
    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }
}
