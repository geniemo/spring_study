package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

// MemberRepository 가 MemberRepositoryCustom 을 상속하고,
// 이 클래스를 구현체 클래스로 쓴다. 클래스 이름은 MemberRepositoryImpl 로 맞춰줘야 한다.
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("SELECT m FROM Member m").getResultList();
    }
}
