package hellojpa;

import org.hibernate.Hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;

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
            // jpql
            System.out.println("============== jpql =============="     );
            String jpql = "select m from Member m where m.username like '%kim%'";
            List<Member> result = em.createQuery(jpql, Member.class).getResultList();
            for (Member member : result) {
                System.out.println("member = " + member);
            }

            // criteria
            System.out.println("================= criteria =================");
            CriteriaBuilder cb = em.getCriteriaBuilder();
            // 멤버 관련된 쿼리를 할거야
            CriteriaQuery<Member> query = cb.createQuery(Member.class);

            // 멤버 테이블 대상으로 from 절
            Root<Member> m = query.from(Member.class);

            CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("username"), "kim"));
            List<Member> resultList = em.createQuery(cq).getResultList();

            System.out.println("=================== Native SQL ====================");
            String sql = "select MEMBER_ID, city, street, zipcode, USERNAME FROM MEMBER";
            List<Member> resultList2 = em.createNativeQuery(sql, Member.class).getResultList();

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
