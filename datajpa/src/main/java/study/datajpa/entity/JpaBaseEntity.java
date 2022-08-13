package study.datajpa.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

// 진짜 상속 관계가 아니라 속성들을 내려줘서 테이블에서 같이 쓸 수 있게 데이터만 공유
@MappedSuperclass
@Getter
public class JpaBaseEntity {

    @Column(updatable = false)
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @PrePersist // persist 전 발생
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        updatedDate = now;
    }

    @PreUpdate // 업데이트 전 발생
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }
}
