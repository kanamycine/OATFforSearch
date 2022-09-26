package com.team6.onandthefarm.service.review;

import com.team6.onandthefarm.dto.review.ReviewFormDto;
import com.team6.onandthefarm.dto.review.ReviewUpdateFormDto;

public interface ReviewService {
	Long saveReview(ReviewFormDto reviewFormDto);
	Long updateReview(ReviewUpdateFormDto reviewUpdateFormDto);
}
