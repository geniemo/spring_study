package hellojpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {

    @Id
    @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;

    private String name;

    // DB 관점으로 몇대 몇 연관관계인지 알려준다.
    // mappedBy로 뭐랑 연결되어있는지 알려준다. Member 클래스의 Team 멤버의 변수 이름
    // mappedBy로 되어있는 멤버에서는 값을 바꿔봐야 아무 일도 일어나지 않는다.
    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

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

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
}
