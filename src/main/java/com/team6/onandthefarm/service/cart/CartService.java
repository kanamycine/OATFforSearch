package com.team6.onandthefarm.service.cart;

import com.team6.onandthefarm.dto.cart.CartDeleteDto;
import com.team6.onandthefarm.dto.cart.CartDto;
import com.team6.onandthefarm.dto.cart.CartIsActivatedDto;
import com.team6.onandthefarm.vo.cart.CartResponse;
import com.team6.onandthefarm.vo.cart.CartResult;

import java.util.List;

public interface CartService {

    List<Long> addCart(CartDto cartDto, Long userId);
    Long updateCartIsActivated(CartIsActivatedDto cartIsActivatedDto);
    List<Long> deleteCart(CartDeleteDto cartDeleteDto);
    CartResult selectCart(Long userId, Integer pageNumber);
    Long setCart(CartDto cartDto, Long userId);
}
