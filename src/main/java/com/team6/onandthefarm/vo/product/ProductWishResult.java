package com.team6.onandthefarm.vo.product;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class ProductWishResult {

    private List<ProductWishResponse> productWishResponseList;

    private Integer currentPageNum;

    private Integer totalPageNum;

    private Integer totalElementNum;
}
