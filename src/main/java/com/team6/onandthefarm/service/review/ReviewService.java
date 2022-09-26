package com.team6.onandthefarm.service.review;

import java.util.List;

import com.team6.onandthefarm.dto.review.ReviewDeleteDto;
import com.team6.onandthefarm.dto.review.ReviewFormDto;
import com.team6.onandthefarm.dto.review.ReviewUpdateFormDto;
import com.team6.onandthefarm.entity.review.Review;

public interface ReviewService {
	Long saveReview(ReviewFormDto reviewFormDto);
	Long updateReview(ReviewUpdateFormDto reviewUpdateFormDto);
	Long deleteReview(ReviewDeleteDto reviewDeleteDto);
	List<Review> getReviewListByLikeCount(Long productId);
}
