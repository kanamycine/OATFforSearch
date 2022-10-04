package com.team6.onandthefarm.vo.review;

import com.team6.onandthefarm.entity.product.Product;
import com.team6.onandthefarm.entity.seller.Seller;
import com.team6.onandthefarm.entity.user.User;

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
public class ReviewFormRequest {
	private Long orderProductId;
	private String reviewContent;
	private String reviewCreatedAt;
	private String reviewModifiedAt;
	private Integer reviewRate;
}
