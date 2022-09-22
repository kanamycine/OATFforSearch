package com.team6.onandthefarm.dto.product;

import com.team6.onandthefarm.entity.product.ProductImg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImgDto {
	private Long productId;
	private String productImgSrc;
}