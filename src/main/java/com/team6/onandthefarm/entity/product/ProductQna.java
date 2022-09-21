package com.team6.onandthefarm.entity.product;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Builder
@Slf4j
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductQna {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productQnaId;

    private String productQnaContent;
    private String productQnaCreatedAt;
    private String productQnaModifiedAt;
    private Boolean productQnaStatus;
    private String productQnaCategory;
}
