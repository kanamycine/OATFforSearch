package com.team6.onandthefarm.entity.order;

import com.team6.onandthefarm.entity.user.User;
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
    private Long ordersId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    private String ordersDate;

    private String ordersStatus;

    private Integer ordersTotalPrice;

    private String ordersRecipientName;

    private String ordersAddress;

    private String ordersPhone;

    private String ordersRequest;

    private Long ordersSellerId;

    private String ordersDeliveryStatus;

    private String ordersDeliveryWaybillNumber;

    private String ordersDeliveryCompany;

    private String ordersDeliveryDate;

    private String ordersSerial;
}
