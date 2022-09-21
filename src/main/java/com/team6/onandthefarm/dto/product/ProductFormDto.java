package com.team6.onandthefarm.dto.product;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.team6.onandthefarm.entity.product.Product;

@Data
@NoArgsConstructor


public class ProductFormDto {
	private Long productId;

	@NotBlank(message = "상품명은 필수 입력 값입니다.")
	private String productName;

	@NotNull(message = "가격은 필수 입력 값입니다.")
	private Integer productPrice;

	@NotBlank(message = "상세내용은 필수 입력 값입니다.")
	private String productDetail;

	@NotNull(message = "재고는 필수 입력 값입니다.")
	private Integer productTotalStock;

	@NotBlank(message = "원산지는 필수 입력 값입니다.")
	private String productOriginPlace;


	@NotBlank(message = "택배사는 필수 입력 값입니다.")
	private String productDeliveryCompany;

	private String productStatus;

	private String productDetailShort;

	private String productRegisterDate;

	private String productUpdateDate;

	private Integer productWishCount;

	private List<ProductImgDto> productImgDtoList = new ArrayList<>();

	private List<Long> productImgIds = new ArrayList<>();

	@Builder
	public ProductFormDto(String productName, Integer productPrice, String productDetail,
			Integer productStockNumber, String productOriginPlace, String productDeliveryCompany,
			String productStatus, String productDetailShort, String productUpdateDate,
			Integer productWishCount){
	}

	public Product toEntity(ProductFormDto dto){
		Product entity = Product.builder()
				.productName(dto.productName)
				.productPrice(dto.productPrice)
				.productDetail(dto.productDetail)
				.productTotalStock(dto.productTotalStock)
				.productOriginPlace(dto.productOriginPlace)
				.productDeliveryCompany(dto.productDeliveryCompany)
				.productStatus(dto.productStatus)
				.productDetailShort(dto.productDetailShort)
				.productRegisterDate(dto.productRegisterDate)
				.productUpdateDate(dto.productUpdateDate)
				.productWishCount(dto.productWishCount)
				.build();

		return entity;
	}
}