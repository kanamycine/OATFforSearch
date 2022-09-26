package com.team6.onandthefarm.vo.order;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSellerDetailRequest {
    private String ordersId;
    private String ordersSerial;
    private String ordersDate;
    private String ordersStatus;
    private Integer ordersTotalPrice;
    private List<OrderProductDetailResponse> ordersProductList;
}
