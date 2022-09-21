package com.team6.onandthefarm.entity.coupon;

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
public class UserCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userCouponId;
    private Long couponId;
    private Long userId;
    private String userCouponPublishedDate;
    private Boolean userCouponStatus;
}
