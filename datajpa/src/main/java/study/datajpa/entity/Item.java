package study.datajpa.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Item implements Persistable<String> {

    @Id
    @GeneratedValue
    private String id;

    @CreatedDate
    private LocalDateTime createdDate;

    public Item(String id) {
        this.id = id;
    }

    @Override
    public boolean isNew() {
        // created date는 jpa persist 전에 등록되는 것이므로 이것으로 새로운 엔티티인지 판단할 수도 있다.
        // 구현하고 있는 서비스에 따라 다를 수 있지만 김영한 강사님은 이 방식을 자주 사용하신다고 함
        return createdDate == null;
    }
}
