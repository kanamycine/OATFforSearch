package com.team6.onandthefarm.vo.order;

import com.team6.onandthefarm.entity.order.Orders;
import lombok.*;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductGroupByProduct {
    private Integer orderProductQty;

    private String orderProductName;

    private Long productId;

    private Integer orderProductPrice;

    private String orderProductMainImg;

    private Long sellerId;

    private String orderProductStatus;

    private String orderProductDate;
}
