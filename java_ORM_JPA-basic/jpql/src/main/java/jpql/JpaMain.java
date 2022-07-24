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
            member.setTeam(team);

            em.persist(member);

            Member member2 = new Member();
            member2.setUsername("teamA");
            member2.setAge(10);
            member2.setTeam(team);

            em.persist(member2);

            em.flush();
            em.clear();

            // left, right, outer, inner, 세타 조인 다 사용 가능하다.
            String query1 = "select m from Member m inner join m.team t";
            List<Member> resultList1 = em.createQuery(query1, Member.class).getResultList();

//            String query2 = "select m from Member m inner join m.team t where t.name = :teamName";
//            List<Member> resultList2 = em.createQuery(query2, Member.class).getResultList();

            String query3 = "select m from Member m left outer join m.team t";
            List<Member> resultList3 = em.createQuery(query3, Member.class).getResultList();

            String query4 = "select m from Member m, Team t where m.username = t.name";
            List<Member> resultList4 = em.createQuery(query4, Member.class).getResultList();

            String query5 = "select m from Member m left join m.team t on t.name = 'teamA'";
            List<Member> resultList5 = em.createQuery(query5, Member.class).getResultList();

            String query6 = "select m from Member m left join Team t on m.username = t.name";
            List<Member> resultList6 = em.createQuery(query6, Member.class).getResultList();

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
