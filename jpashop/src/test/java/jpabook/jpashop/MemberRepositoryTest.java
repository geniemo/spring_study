package jpabook.jpashop;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;


    @Test
    @Transactional // test에 있으면 테스트 후 알아서 롤백을 해준다.
    @Rollback(false) // 롤백을 안하고 커밋하도록 해줌
    public void testMember() throws Exception {
        // given
        Member member = new Member();
        member.setUsername("memberA");

        // when
        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.find(saveId);

        // then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        // 한 Transactional 안, 같은 영속성 컨텍스트 내에 관리되고 있는 똑같은 엔티티가 있기 때문에
        // 식별자가 같으면 기존에 관리되던 게 나온 것, 1차 캐시에 있는 것을 꺼내온다.
        assertThat(findMember).isEqualTo(member);
    }
}