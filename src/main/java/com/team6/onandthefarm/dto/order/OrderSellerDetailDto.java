package com.team6.onandthefarm.dto.order;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSellerDetailDto {
    private String orderSerial;

    private String sellerId;
}
