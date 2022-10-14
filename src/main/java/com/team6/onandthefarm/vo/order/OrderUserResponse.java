package com.team6.onandthefarm.vo.order;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderUserResponse {
    private Long orderProductId;

    private Integer orderProductQty;

    private String orderProductName;

    private Integer orderProductPrice;

    private String orderProductMainImg;

    private String orderProductStatus;

    private Long productId;

}
