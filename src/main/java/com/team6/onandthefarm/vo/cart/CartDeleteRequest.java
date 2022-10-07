package com.team6.onandthefarm.vo.cart;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDeleteRequest {

    private List<Long> cartList;

}
