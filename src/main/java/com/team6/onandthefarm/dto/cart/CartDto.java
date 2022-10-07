package com.team6.onandthefarm.dto.cart;

import com.team6.onandthefarm.vo.cart.CartInfoRequest;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {

    private List<CartInfoRequest> cartList;

}
