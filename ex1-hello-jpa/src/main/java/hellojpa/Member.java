package hellojpa;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Member {

    @Id
    private Long id;

    // 객체는 username이라 쓰고싶은데 DB 컬럼 명은 name이라고 쓰고 싶은 경우
    @Column(name = "name")
    private String username;

    private Integer age;

    // 객체에서 enum type을 쓰고 싶은데 DB에는 enum 타입이 없다.
    // 이 경우에 @Enumerated 어노테이션을 사용
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    // 날짜 타입을 쓰고싶은 경우 @Temporal을 쓰고
    // DATE, TIME, TIMESTAMP 중 사용
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    // VARCHAR를 넘어서는 큰 컨텐츠를 넣고 싶다면 @Lob 어노테이션 사용
    // Lob이라 하고 문자 타입이면 DB 타입은 clob으로 된다.
    @Lob
    private String description;


}
