package com.team6.onandthefarm.entity.order;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Builder
@Slf4j
@Entity
@Table(name = "order_product")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderProductId;
    private Long orderId;
    private Integer orderProductQty;
    private String orderProductName;
    private Long productId;
    private Integer orderProductPrice;
    private String orderProductMainImg;
    private Long sellerId;

}
