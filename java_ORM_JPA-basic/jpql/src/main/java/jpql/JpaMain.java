package jpql;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        // persistence.xml에 있는 <persistence-unit name="hello"> 얘를 불러온다.
        // EntityManagerFactory 애플리케이션 로딩 시점에 딱 하나만 만들어야 한다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // 실제 DB에 저장하거나 하는 트랜잭션 단위를 할 때마다 EntityManager를 만든다.
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 엔티티 매니저를 꺼낸 후 여기서 실제 코드를 작성
        try {
            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setAge(10);
            member1.setType(MemberType.ADMIN);
            member1.setTeam(teamA);

            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setAge(60);
            member2.setType(MemberType.USER);
            member2.setTeam(teamA);

            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setAge(60);
            member3.setType(MemberType.USER);
            member3.setTeam(teamB);

            em.persist(member3);

            // FLUSH가 되긴 함
            int resultCount = em.createQuery("UPDATE Member m SET m.age = 20").executeUpdate();
            System.out.println("resultCount = " + resultCount);

            System.out.println("======= CLEAR 전 =======");
            Member findMember1 = em.find(Member.class, member1.getId());
            System.out.println("findMember1 = " + findMember1);

            em.clear();
            System.out.println("======= CLEAR 후 =======");
            Member findMember2 = em.find(Member.class, member1.getId());
            System.out.println("findMember2 = " + findMember2);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();
    }
}
