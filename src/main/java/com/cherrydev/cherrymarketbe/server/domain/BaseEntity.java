package com.cherrydev.cherrymarketbe.server.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.sql.Timestamp;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

  @CreatedDate
  @Column(name = "CREAT_DE", nullable = false, updatable = false)
  private Timestamp createdAt;

  @LastModifiedDate
  @Column(name = "UPDT_DE", nullable = false)
  private Timestamp updatedAt;
}
