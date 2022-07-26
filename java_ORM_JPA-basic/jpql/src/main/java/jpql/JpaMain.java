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
            Team team = new Team();
            team.setName("teamA");

            em.persist(team);

            Member member1 = new Member();
            member1.setUsername("관리자1");
            member1.setAge(10);
            member1.setType(MemberType.ADMIN);
            member1.setTeam(team);

            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("유저1");
            member2.setAge(60);
            member2.setType(MemberType.USER);
            member2.setTeam(team);

            em.persist(member2);

            em.flush();
            em.clear();

            String query = "SELECT m.username FROM Member m";
            List<String> resultList = em.createQuery(query, String.class).getResultList();
            for (String s : resultList) {
                System.out.println("s = " + s);
            }

            String query2 = "SELECT m.team FROM Member m";
            List<Team> resultList2 = em.createQuery(query2, Team.class).getResultList();

            // m.team으로 엔티티 가져온 후 추가로 team 내의 name까지 탐색
            String query3 = "SELECT m.team.name FROM Member m";
            List<String> resultList3 = em.createQuery(query3, String.class).getResultList();
            for (String s : resultList3) {
                System.out.println("s = " + s);
            }

            String query4 = "SELECT t.members FROM Team t";
            List<Collection> resultList4 = em.createQuery(query4, Collection.class).getResultList();
            System.out.println("resultList4 = " + resultList4);

            String query5 = "SELECT t.members.size FROM Team t";
            List<Integer> resultList5 = em.createQuery(query5, Integer.class).getResultList();
            for (Integer integer : resultList5) {
                System.out.println("integer = " + integer);
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
