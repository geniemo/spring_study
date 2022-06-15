package hello.hellospring;

import hello.hellospring.repository.MemberRepository;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    // application.properties에 저장해놓은 내용으로 스프링이 알아서 가져와준다
//    private DataSource dataSource;
//
//    @Autowired
//    public SpringConfig(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }

//    private EntityManager em;
//
//    @Autowired
//    public SpringConfig(EntityManager em) {
//        this.em = em;
//    }

    private final MemberRepository memberRepository;

    @Autowired
    public SpringConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 이렇게 하면 스프링일 뜰 때 @Configuration을 읽고
    // @Bean을 읽어서 멤버 서비스를 스프링 빈에 등록해준다.
    // 아래와 같이 작성하면 멤버 서비스와 멤버 리포지토리를 스프링 빈에 등록하고
    // 빈에 등록되어 있는 멤버 리포지토리를 멤버 서비스에 넣어준다.
    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository);
    }

//    @Bean
//    public MemberRepository memberRepository() {
//        // 다형성을 이용해 아래 줄만 바꾸면 DB를 바꾸는데도 정상적으로 동작한다.
//        return new JpaMemberRepository(em);
//    }
}
