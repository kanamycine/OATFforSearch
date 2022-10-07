package com.team6.onandthefarm.vo.cart;

import lombok.Data;

@Data
public class CartInfoRequest {

    private Long productId;
    private Integer cartQty;

}
