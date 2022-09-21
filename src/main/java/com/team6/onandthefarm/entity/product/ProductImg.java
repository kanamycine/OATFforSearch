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
public class ProductImg {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productImgId;

    private String productImgSrc;
}
