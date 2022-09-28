package com.team6.onandthefarm.service.cart;

import com.team6.onandthefarm.dto.cart.CartDto;
import com.team6.onandthefarm.dto.cart.CartIsActivatedDto;

public interface CartService {

    Long addCart(CartDto cartDto);
    Long updateCartIsActivated(CartIsActivatedDto cartIsActivatedDto);
}
