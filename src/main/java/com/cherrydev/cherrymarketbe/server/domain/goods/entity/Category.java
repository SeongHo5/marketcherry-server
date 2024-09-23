package com.cherrydev.cherrymarketbe.server.domain.goods.entity;

import com.cherrydev.cherrymarketbe.server.domain.BaseEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Builder
@Comment("카테고리")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "CTGRY")
public class Category extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "CTGRY_ID", nullable = false)
  private Long id;

  @Column(name = "CTGRY_NM", nullable = false, length = 20)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "CTGRY_UPPER")
  private Category categoryUpper;

  @OneToMany(mappedBy = "category")
  private List<Goods> goods = new ArrayList<>();
}
