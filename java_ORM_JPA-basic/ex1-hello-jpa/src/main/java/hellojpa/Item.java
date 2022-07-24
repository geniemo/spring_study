package hellojpa;

import javax.persistence.*;

@Entity
// 아무것도 지정하지 않으면 싱글 테이블
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// DTYPE이라는 컬럼을 만들어서 각 엔티티의 DiscriminateValue를 넣는다. 기본 값은 엔티티 클래스 명
@DiscriminatorColumn
public abstract class Item {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private int price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
