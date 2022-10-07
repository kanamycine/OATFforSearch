package com.team6.onandthefarm.vo.product;

import com.team6.onandthefarm.dto.product.ProductImgDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductWishResponse {

    private Long wistId;
    private Long productId;
    private String productName;
    private Integer productPrice;
    private String productMainImgSrc;
    private String productDetail;
    private String productOriginPlace;
    private String productDetailShort;

}
