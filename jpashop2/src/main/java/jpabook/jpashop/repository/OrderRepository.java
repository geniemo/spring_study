package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAll(OrderSearch orderSearch) {
        return em.createQuery("select o from Order o join o.member m" +
                        " where o.status = :status " +
                        " and m.name like :name", Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName())
                .setMaxResults(1000)
                .getResultList();
    }

    /**
     * 손수 다 작성
     * 너무너무 귀찮은 방법
     */
    public List<Order> findAllByString(OrderSearch orderSearch) {
        //language=JPAQL
        String jpql = "select o From Order o join o.member m";
        boolean isFirstCondition = true;
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }
        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000); //최대 1000건
        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
    }

    /**
     * JPA Criteria
     * <p>
     * 동적 코드를 작성할 때 위 방식에 비해 아주 메리트가 있다.
     * <p>
     * 얘도 결코 좋은 방법은 아니다. 유지보수성이 0에 가깝다.
     * 코드를 보고서 어떤 쿼리문이 만들어질지 눈에 보이지가 않는다.
     * 표준 스펙에 있지만 실무에서는 잘 쓰지 않는다. 쓰는 사람도 읽는 사람도 멘붕
     */
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        // 주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }
        // 회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name = cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
        return query.getResultList();
    }

    // Querydsl로 쓰면 아주 간단해진다. 나중에 바꿔보고 일단은 이걸로 진행해보자.

    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                "SELECT o FROM Order o" +
                        " JOIN FETCH o.member m" +
                        " JOIN FETCH o.delivery d", Order.class
        ).getResultList();
    }

    public List<Order> findAllWithItem() {
        // 여기서 distinct 는 sql과는 다르게
        // DB에 distinct 키워드를 날려줄 뿐만 아니라
        // 엔티티가 중복일 경우 중복을 걸러주는 두 가지 기능이 있다.
        // 일대다 페치 조인을 할 때는 페이징을 할 수 없다는 단점이 있다.
        // 컬렉션 페치 조인은 1개만 사용할 수 있다. 컬렉션 둘 이상에 페치 조인을 사용하면 안된다. 데이터가 부정합하게 조회될 수 있다.
        return em.createQuery(
                        "SELECT DISTINCT o FROM Order o" +
                                " JOIN FETCH o.member m" +
                                " JOIN FETCH o.delivery d" +
                                " JOIN FETCH o.orderItems oi" +
                                " JOIN FETCH oi.item i", Order.class)
                .getResultList();

    }

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        // default_batch_fetch_size: 100 를 설정해주면
        // SELECT o FROM Order o 로만 조회해도 member랑 delivery를 조인할 때 최적화를 해준다.
        // 그렇지만 ToOne은 그냥 fetch join 하는 게 가장 성능이 잘 나온다.
        return em.createQuery(
                        "SELECT o FROM Order o" +
                                " JOIN FETCH o.member m" +
                                " JOIN FETCH o.delivery d", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    // 아래 new에서 엔티티를 OrderQueryDto(o)로 넘겨주면 jpa에서는 엔티티 식별자만 넘겨주기 때문에 안에 값을 이용할 수 없다.
    // 따라서 아래와 같이 값을 꺼내서 넣어주었다.
    // 그런데 리포지토리는 엔티티를 조회하는 데 쓰는 게 맞다.
    // v1, v2, v3까지는 그 역할을 제대로 수행한다 하지만 v4는 좀 선을 넘은 감이 있다. 그래서 별도의 리포지토리를 만들어서 쓰면 이런 문제를 해결 가능하다.
//    public List<OrderQueryDto> findOrderDtos() {
//        return em.createQuery(
//                "SELECT new jpabook.jpashop.repository.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
//                        " FROM Order o" +
//                        " JOIN o.member m" +
//                        " JOIN o.delivery d", OrderQueryDto.class)
//                .getResultList();
//
//    }

}
