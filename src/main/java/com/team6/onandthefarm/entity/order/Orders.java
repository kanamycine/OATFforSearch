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
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderId;
    private Long userId;
    private String orderDate;
    private String orderStatus;
    private Integer orderTotalPrice;
    private String orderRecipientName;
    private String orderAddress;
    private String orderPhone;
    private String orderRequest;
    private Long orderSellerId;
    private String orderDeliveryStatus;
    private String orderDeliveryWaybillNumber;
    private String orderDeliveryCompany;
    private String orderDeliveryDate;
    private String orderSerial;
}
