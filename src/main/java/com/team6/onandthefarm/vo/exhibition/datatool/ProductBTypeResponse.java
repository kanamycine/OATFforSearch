package com.team6.onandthefarm.vo.exhibition.datatool;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductBTypeResponse {
	private Long productId;
	private String ImgSrc;
	private String productName;
	private Integer productPrice;
	private Integer soldCount;
}
