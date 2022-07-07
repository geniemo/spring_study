package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemUpdateTest {

    @Autowired
    EntityManager em;

    @Test
    public void updateTest() throws Exception {
        Book book = em.find(Book.class, 1L);

        // 트랜잭션 안에서는 이름 바꾼 다음 트랜잭션이 커밋 되어버리면 변경 부분에 대해 JPA가 찾아서 반영을 한다.
        // 이를 dirty checking, 변경 감지라 한다.
        // 이 메커니즘으로 JPA의 엔티티를 바꿀 수 있다.
        book.setName("asdfasdf");

    }
}
