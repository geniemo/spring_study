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
            Member member1 = new Member();
            member1.setUsername("member1");
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("member2");
            em.persist(member2);

            em.flush();
            em.clear();

//            Member findMember = em.find(Member.class, member.getId());
//            System.out.println("findMember.getId() = " + findMember.getId());
//            System.out.println("findMember.getUsername() = " + findMember.getUsername());

            // 이 줄은 실행해도 select 쿼리가 나가지 않는다.
//            Member findMember = em.getReference(Member.class, member1.getId());
//
//            // 그런데 또 이를 이용해 값을 실제로 사용하려 하면 쿼리가 나가는 것을 알 수 있다.
//            System.out.println("findMember.getId() = " + findMember.getId());
//            System.out.println("before findMember = " + findMember.getClass());
//            System.out.println("findMember.getUsername() = " + findMember.getUsername());
//            System.out.println("after findMember = " + findMember.getClass()); // 프록시가 진짜 엔티티로 바뀌지 않았다. 그냥 이 객체를 통해 실제 엔티티에 접근하는 것

            Member m1 = em.find(Member.class, member1.getId());
            Member m2 = em.getReference(Member.class, member2.getId());

            // false가 나온다. m1은 진짜 객체, m2는 프록시 객체이기 때문
            System.out.println("(m1.getClass() == m2.getClass()) = " + (m1.getClass() == m2.getClass()));
            // 둘 다 true가 나온다.
            System.out.println("(m1 instanceof Member) = " + (m1 instanceof Member));
            System.out.println("(m2 instanceof Member) = " + (m2 instanceof Member));

            // 이미 영속성 컨텍스트에 member1이 있었기 때문에 getReference를 했음에도 프록시가 아니라 진짜 엔티티 객체가 반환되었다.
            Member reference = em.getReference(Member.class, member1.getId());
            System.out.println("reference.getClass() = " + reference.getClass());

            // m2를 준영속 엔티티로 만든다.
//            em.detach(m2);
//            em.close(); // 이렇게 해도 마찬가지
//            em.clear(); // 이렇게 해도 마찬가지
            // 준영속으로 만든 후 프록시를 초기화하려고 시도 -> org.hibernate.LazyInitializationException: could not initialize proxy
//            m2.getUsername();

            System.out.println("emf.getPersistenceUnitUtil().isLoaded(reference) = " + emf.getPersistenceUnitUtil().isLoaded(m2));
//            m2.getUsername(); // 강제 초기화
            Hibernate.initialize(m2);
            System.out.println("emf.getPersistenceUnitUtil().isLoaded(reference) = " + emf.getPersistenceUnitUtil().isLoaded(m2));

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
