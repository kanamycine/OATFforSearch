package com.team6.onandthefarm.dto.product;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class
ProductWishCancelDto {
	private Long wishId;
	private Long productId;
	private Long userId;
}
