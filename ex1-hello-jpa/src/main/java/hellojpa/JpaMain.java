package hellojpa;

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
            /*
            // 삽입
            Member member = new Member();
            member.setId(1L);
            member.setName("HelloA");

            em.persist(member);

            // 조회
            Member findMember = em.find(Member.class, 1L);
            System.out.println("findMember.getId() = " + findMember.getId());
            System.out.println("findMember.getName() = " + findMember.getName());

            // 삭제
            Member findMember = em.find(Member.class, 1L);
            em.remove(findMember);

            // 수정
            Member findMember = em.find(Member.class, 1L);
            findMember.setName("HelloJPA");
            // persist로 저장하지 않아도 더티 체킹을 통해 변화를 감지, 반영
             */

            /*
            // DB에 데이터가 있을 때 아래 두 줄 빼고 주석처리하고 실행해보자.
            // 아래 두 줄 실행 결과를 보면 select문은 한 번만 나갔다.
            // 첫 조회 시에는 select 문을 날려서 받아온거고 두 번째 조회 시에는 1차 캐시에서 가져왔기 때문이다.
            Member findMember1 = em.find(Member.class, 100L);
            Member findMember2 = em.find(Member.class, 100L);
            System.out.println("(findMember1 == findMember2) = " + (findMember1 == findMember2));
             */

            /*
            // 비영속
            Member member = new Member();
            member.setId(100L);
            member.setName("HelloJPA");

            // 영속
            System.out.println("=== BEFORE ===");
            em.persist(member); // BEFORE와 AFTER 사이 출력에는 쿼리문이 없다. 여기서 쿼리문이 날라가지 않았다는 것
            System.out.println("=== AFTER ===");
            
            // 출력을 확인해보면 조회를 했는데 select 쿼리가 안나갔다.
            // 1차 캐시에 저장되어 있던 엔티티를 가져온 것
            Member findMember = em.find(Member.class, 100L);

            System.out.println("findMember.getId() = " + findMember.getId());
            System.out.println("findMember.getName() = " + findMember.getName());
             */

            /*
            Member member1 = new Member(150L, "A");
            Member member2 = new Member(160L, "B");

            em.persist(member1);
            em.persist(member2);
            System.out.println("=================="); // 이 이전에는 쿼리가 나가지 않는다.
             */

            /*
            Member member = em.find(Member.class, 150L);
            member.setName("ZZZZZ");

            // 엔티티 수정 시 persist를 써서 얻을 수 있는 이점이 없다.
            // 찾아 온 다음 데이터를 변경한 후 커밋해서 변경 감지를 해서 변경사항을 반영해준다.
//            em.persist(member);
            System.out.println("==================");
             */

            /*
            Member member = new Member(200L, "member200");
            em.persist(member);
            
            // 원래는 커밋 시에 날라가야 하지만 미리 확인을 해보고 싶다 -> 강제로 호출
            em.flush();
            System.out.println("==================");
             */

            /*
            // 영속
            Member member = em.find(Member.class, 150L);
            member.setName("ZZZZZ");
            
            // 준영속
//            em.detach(member);

//            em.clear(); // 초기화 시에는 다시 조회 시 똑같은 애를 다시 select 문을 날려 조회하게 된다.
//            Member member2 = em.find(Member.class, 150L);

//            em.close(); // 이 경우에는 update가 된다. https://www.inflearn.com/questions/53733 참고하자.

            System.out.println("=================="); // 이후 업데이트 쿼리가 나가지 않는다.
             */

            tx.commit(); // 쿼리는 트랜잭션을 커밋하는 시점에 영속성 컨텍스트에 있는 애가 DB에 날라가게 된다.
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
