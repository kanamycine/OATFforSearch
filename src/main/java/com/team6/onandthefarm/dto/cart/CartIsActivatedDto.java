package com.team6.onandthefarm.dto.cart;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartIsActivatedDto {

    private Long cartId;
    private Boolean cartIsActivated;

}
