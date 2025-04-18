package com.userorder.persistence.model.base;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class PersistenceModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private Long id;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    protected LocalDateTime createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    protected LocalDateTime modifiedDate;

    @CreatedBy
    @Column(updatable = false)
    protected String createdBy;

    @LastModifiedBy
    protected String modifiedBy;
}
