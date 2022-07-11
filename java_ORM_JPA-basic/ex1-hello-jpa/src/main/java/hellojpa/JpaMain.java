package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
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
            team.setName("TeamA");
            em.persist(team); // 영속 상태로

            Member member = new Member();
            member.setUsername("member1");
            member.changeTeam(team);
            em.persist(member);

//            team.getMembers().add(member); // changeTeam을 만듦으로써 더이상 하지 않아도 된다.

            // team.getMembers().add(member); 를 해주지 않았을 때
            // em.flush, em.clear를 해주면 db에 들어가면서 id 같은 것들이 다 생성되기 때문에
            // 아래에서 find 했을 때 정상적으로 팀과 멤버스 모두 잘 조회된다.
            // 하지만 em.flush, em.clear를 해주지 않았다면 id들이 생성되지 않았기 때문에
            // find를 했을 때 Team이 조회가 되긴 되지만 getMembers()의 결과로 나온 리스트에 아무것도 담겨 있지 않게 된다.
            em.flush();
            em.clear();

            Team findTeam = em.find(Team.class, team.getId());
            List<Member> members = findTeam.getMembers();
            for (Member m : members) {
                System.out.println("m.getUsername() = " + m.getUsername());
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
