package com.team6.onandthefarm.dto.product;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class
ProductWishCancelDto {
	private List<Long> wishId;
	private Long userId;
}
