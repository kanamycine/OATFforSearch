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
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;

    private String productName;
    private Integer productPrice;
    private Integer productTotalStock;
    private String productMainImgSrc;
    private String productDetail;
    private String productDetailShort;
    private String productSource;
    private String productDeliveryCompany;
    private String productRegisterDate;
    private String productUpdateDate;
    private String productStatus;
    private Integer productWishCount;
}
