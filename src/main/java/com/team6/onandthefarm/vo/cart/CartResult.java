package com.team6.onandthefarm.vo.cart;

import com.team6.onandthefarm.vo.product.ProductWishResponse;
import lombok.Data;

import java.util.List;

@Data
public class CartResult {

    private List<CartResponse> cartResponseList;

    private Integer currentPageNum;

    private Integer totalPageNum;

    private Integer totalElementNum;

}
