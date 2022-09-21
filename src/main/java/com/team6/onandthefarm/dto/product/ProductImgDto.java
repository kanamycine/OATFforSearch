package com.team6.onandthefarm.dto.product;

import com.team6.onandthefarm.entity.product.ProductImg;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ProductImgDto {
	private Long productImgId;
	private Long productId;
	private String productImgSrc;

	@Builder
	public ProductImgDto(Long product_id, String product_img_src){
		this.productId = productId;
		this.productImgSrc = productImgSrc;
	}

	public ProductImg toEntity(ProductImgDto dto){
		ProductImg entity = ProductImg.builder()
				.productImgId(dto.productId)
				.productImgSrc(dto.productImgSrc)
				.build();
		return entity;
	}
}