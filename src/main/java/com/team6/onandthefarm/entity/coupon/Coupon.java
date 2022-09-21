package com.team6.onandthefarm.entity.coupon;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Builder
@Slf4j
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long couponId;
    private Long sellerId;
    private String couponName;
    private String couponDetail;
    private String couponExpirationPeriod;
    private String couponExpirationDate;
    private String couponDiscountRate;
    private Integer couponDiscountPrice;
}
