package com.team6.onandthefarm.vo.seller;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerPopularProductResponse {
    private String productMainImgSrc;

    private String productName;

    private Integer productWishCount;

    private Long productId;
}
