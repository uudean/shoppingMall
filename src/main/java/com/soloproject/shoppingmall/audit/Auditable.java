package com.soloproject.shoppingmall.audit;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Setter
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss",timezone = "Asia/Seoul")
    @CreatedDate
    @Column(nullable = false)
    public LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss",timezone = "Asia/Seoul")
    @LastModifiedDate
    @Column(nullable = false)
    public LocalDateTime modifiedAt;

}
