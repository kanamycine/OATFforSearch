package com.team6.onandthefarm.vo.order;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundDetailResponse {
    private String productName;
    private Integer productPrice;
    private String cancelDetail;
    private Integer productQty;
    private String productStatus;
}
