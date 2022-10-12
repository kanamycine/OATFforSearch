package com.team6.onandthefarm.vo.sns.profile;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WishProductListResponse {
	private Long productId;

	private String productName;

	private Integer productPrice;

	private String productMainImgSrc;

	private String productOriginPlace;

	private String productStatus;

	private Integer productWishCount;

	//필요 여부 논의...
	private Long sellerName;
	private Integer productReviewCount;
}
