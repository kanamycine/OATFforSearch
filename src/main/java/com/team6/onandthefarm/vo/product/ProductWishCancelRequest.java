package com.team6.onandthefarm.vo.product;

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
public class ProductWishCancelRequest {
	private Long wishId;
	private Long productId;
	private Long userId;
}
