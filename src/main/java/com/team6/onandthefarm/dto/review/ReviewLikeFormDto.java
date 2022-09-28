package com.team6.onandthefarm.dto.review;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewLikeFormDto {
	private Long reviewId;
	private Long userId;
}
