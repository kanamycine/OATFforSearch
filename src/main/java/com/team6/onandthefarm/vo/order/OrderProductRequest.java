package com.team6.onandthefarm.vo.order;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductRequest {
    private Long productId;
    private Integer productQty;
}
