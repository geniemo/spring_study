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
            Address address = new Address("city", "street", "10000");

            Member member = new Member();
            member.setUsername("member1");
            member.setHomeAddress(address);
            em.persist(member);

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setHomeAddress(address);
            em.persist(member2);

            // 두 엔티티의 도시가 모두 바뀐다. 업데이트 쿼리가 두 개 나감
            // 이런 걸로 나오는 버그는 찾기도 힘들다.
//            member.getHomeAddress().setCity("newCity");

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
