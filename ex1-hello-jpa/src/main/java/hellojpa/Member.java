package hellojpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
//@Table(name = "USER") // 이런식으로 실제 어떤 테이블에 매핑할지도 설정 가능
public class Member {

    // JPA에 PK가 뭔지 알려준다.
    @Id
    private Long id;
//    @Column(name = "username") // 실제 어떤 컬럼에 매핑할지도 설정 가능
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
