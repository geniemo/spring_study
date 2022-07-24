package jpql;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
            Team team = new Team();
            team.setName("teamA");

            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            member.setType(MemberType.ADMIN);
            member.setTeam(team);

            em.persist(member);

            Member member2 = new Member();
            member2.setUsername("teamA");
            member2.setAge(60);
            member2.setType(MemberType.USER);
            member2.setTeam(team);

            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername(null);
            member3.setAge(60);
            member3.setType(MemberType.USER);
            member3.setTeam(team);

            em.persist(member3);

            Member member4 = new Member();
            member4.setUsername("관리자");
            member4.setAge(60);
            member4.setType(MemberType.USER);
            member4.setTeam(team);

            em.persist(member4);

            em.flush();
            em.clear();

            String query = "SELECT FUNCTION('group_concat', m.username) FROM Member m";
            List<String> resultList = em.createQuery(query, String.class).getResultList();
            for (String s : resultList) {
                System.out.println("s = " + s);
            }

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
