package hello.hellospring.domain;

import javax.persistence.*;

// jpa가 관리하는 Entity
@Entity
public class Member {

    // DB가 알아서 생성해주는 값은 @GeneratedValue로 다음과 같이 설정
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Column 이름이 변수 이름과 똑같다면 상관없고,
    // 다르다면 @Column(name = "컬럼명") 어노테이션을 붙여주면 된다.
    private String name;

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
}
