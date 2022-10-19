package com.team6.onandthefarm.entity.order;

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
@SequenceGenerator(
        name="PAYMENT_SEQ_GENERATOR",
        sequenceName = "PAYMENT_SEQ",
        initialValue = 100000, allocationSize = 1
)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "PAYMENT_SEQ_GENERATOR")
    private Long paymentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordersId")
    private Orders orders;

    private String paymentDate;

    private String paymentMethod;

    private Boolean paymentStatus;

    private Integer paymentDepositAmount;

    private String paymentDepositName;

    private String paymentDepositBank;

    private String paymentRefundAccount;

    private String paymentRefundAccountName;
}
