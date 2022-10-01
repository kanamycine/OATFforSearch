package com.team6.onandthefarm.vo.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductReviewResponse {

    private String productName;
    private String productMainImgSrc;
    private String productOriginPlace;
    private String sellerShopName;

}
