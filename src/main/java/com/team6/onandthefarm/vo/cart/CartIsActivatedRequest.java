package com.team6.onandthefarm.vo.cart;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartIsActivatedRequest {

    private Long cartId;
    private Boolean cartIsActivated;
}
