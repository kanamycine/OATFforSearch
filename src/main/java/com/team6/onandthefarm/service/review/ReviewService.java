package com.team6.onandthefarm.service.review;

import java.util.List;

import com.team6.onandthefarm.dto.review.ReviewDeleteDto;
import com.team6.onandthefarm.dto.review.ReviewFormDto;
import com.team6.onandthefarm.dto.review.ReviewLikeCancelFormDto;
import com.team6.onandthefarm.dto.review.ReviewLikeFormDto;
import com.team6.onandthefarm.dto.review.ReviewUpdateFormDto;
import com.team6.onandthefarm.vo.review.ReviewSelectionResponse;

public interface ReviewService {
	Long saveReview(ReviewFormDto reviewFormDto);
	Long updateReview(ReviewUpdateFormDto reviewUpdateFormDto);
	Long deleteReview(ReviewDeleteDto reviewDeleteDto);
	Long upLikeCountReview(ReviewLikeFormDto reviewLikeFormDto);
	Long cancelReviewLikeCount(ReviewLikeCancelFormDto reviewLikeCancelFormDto);
	List<ReviewSelectionResponse> getReviewListByLikeCount(Long productId, Integer pageNumber);
	List<ReviewSelectionResponse> getReviewListOrderByNewest(Long productId, Integer pageNumber);
}
