package com.team6.onandthefarm.entity.order;

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
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long paymentId;
    private Long orderId;
    private String paymentDate;
    private String paymentMethod;
    private Boolean paymentStatus;
    private Integer paymentDepositAmount;
    private String paymentDepositName;
    private String paymentDepositBank;
    private String paymentRefundAccount;
    private String paymentRefundAccountName;
}
