package com.team6.onandthefarm.vo.order;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSheetRequest {
    private Long productId;

    private Long userId;

    private Integer productQty;
}
