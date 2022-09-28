package com.team6.onandthefarm.dto.cart;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {

    private Long productId;
    private Integer cartQty;

}
