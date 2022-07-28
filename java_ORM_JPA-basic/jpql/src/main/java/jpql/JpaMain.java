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

            em.flush();
            em.clear();

            String query1 = "SELECT m FROM Member m";
            List<Member> result1 = em.createQuery(query1, Member.class).getResultList();
            // 지연로딩이므로 teamA와 teamB가 영속성 컨텍스트에 없다.
            // 따라서 회원1에서 팀을 가져올 때는 쿼리를 날려서 가져오고, 팀A
            // 회원 2에서 가져올 때는 1차캐시에서 가져오고, 팀A
            // 회원 3에서 가져올 때 팀 B는 영속성 컨텍스트에 없으므로 쿼리를 날려서 가져온다. 팀B
            for (Member member : result1) {
                System.out.println("member = " + member.getUsername() + ", " + member.getTeam().getName());
            }

            String query2 = "SELECT m FROM Member m JOIN FETCH m.team";
            List<Member> result2 = em.createQuery(query2, Member.class).getResultList();
            // 지연로딩이지만 한 번에 모두 다 가져온다.
            for (Member member : result2) {
                System.out.println("member = " + member.getUsername() + ", " + member.getTeam().getName());
            }

            String query3 = "SELECT t FROM Team t JOIN FETCH t.members";
            List<Team> result3 = em.createQuery(query3, Team.class).getResultList();
            for (Team team : result3) {
                System.out.println("team = " + team.getName() + " | " + team.getMembers().size());
                for (Member member : team.getMembers()) {
                    System.out.println("-> member = " + member);
                }
            }

            // 쿼리만 생각하면 조인 결과에서 멤버 정보가 달라서 결과가 그대로인 게 맞는데
            // 같은 식별자를 가진 팀 엔티티를 알아서 제거해준다.
            String query4 = "SELECT DISTINCT t FROM Team t JOIN FETCH t.members";
            List<Team> result4 = em.createQuery(query4, Team.class).getResultList();
            for (Team team : result4) {
                System.out.println("team = " + team.getName() + " | " + team.getMembers().size());
                for (Member member : team.getMembers()) {
                    System.out.println("-> member = " + member);
                }
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
