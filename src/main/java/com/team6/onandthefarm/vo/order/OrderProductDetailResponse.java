package com.team6.onandthefarm.vo.order;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductDetailResponse {
    private String orderProductName;
    private Integer orderProductPrice;
    private String orderProductMainImg;
    private Integer orderProductQty;
}
