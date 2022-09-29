package com.team6.onandthefarm.dto.order;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSheetDto {
    private Long productId;

    private Long userId;

    private Integer productQty;
}
