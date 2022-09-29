package com.team6.onandthefarm.vo.cart;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartRequest {

    private Long productId;
    private Integer cartQty;

}