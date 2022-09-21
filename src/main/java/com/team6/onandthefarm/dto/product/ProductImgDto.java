package com.team6.onandthefarm.dto.product;

import com.team6.onandthefarm.entity.product.ProductImg;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ProductImgDto {
	private Long product_img_id;
	private Long product_id;
	private String product_img_src;

	@Builder
	public ProductImgDto(Long product_id, String product_img_src){
		this.product_id = product_id;
		this.product_img_src = product_img_src;
	}

	public ProductImg toEntity(ProductImgDto dto){
		ProductImg entity = ProductImg.builder()
				.productImgId(dto.product_img_id)
				.productImgSrc(dto.product_img_src)
				.build();
		return entity;
	}

	public ProductImgDto of(ProductImg entity){
		ProductImgDto dto = ProductImgDto.builder()
				.product_id(entity.getProductImgId())
				.product_img_src(entity.getProductImgSrc())
				.build();

		return dto;
	}
}