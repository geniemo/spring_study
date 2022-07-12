package hellojpa;

import org.hibernate.Hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

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
            Child child1 = new Child();
            Child child2 = new Child();

            Parent parent = new Parent();
            parent.addChild(child1);
            parent.addChild(child2);

            // 원래는 이렇게 해야 했겠지만 코드를 짤 때 parent 위주로 할거야
            // parent를 persist 하면 child도 같이 persist 되면 좋겠어 -> CASCADE
//            em.persist(parent);
//            em.persist(child1);
//            em.persist(child2);

            // CASCADE를 Parent에 적용한 후에는 parent만 persist 해주면 child 둘 다 persist된다.
            em.persist(parent);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();
    }

    // 회원 정보만 수정
    // 이것만 사용할 때는 굳이 팀까지 다 가져올 필요가 없다.
    private static void printMember(Member member) {
        System.out.println("member.getUsername() = " + member.getUsername());
    }

    // 회원과 팀 정보를 함께 출력
    private static void printMemberAndTeam(Member member) {
        String username = member.getUsername();
        System.out.println("username = " + username);

        Team team = member.getTeam();
        System.out.println("team.getName() = " + team.getName());
    }
}
