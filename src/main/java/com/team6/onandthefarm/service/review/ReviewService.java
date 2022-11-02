package com.team6.onandthefarm.service.review;

import java.util.List;

import com.team6.onandthefarm.dto.review.ReviewDeleteDto;
import com.team6.onandthefarm.dto.review.ReviewFormDto;
import com.team6.onandthefarm.dto.review.ReviewLikeCancelFormDto;
import com.team6.onandthefarm.dto.review.ReviewLikeFormDto;
import com.team6.onandthefarm.dto.review.ReviewUpdateFormDto;
import com.team6.onandthefarm.vo.review.ReviewInfoResponse;
import com.team6.onandthefarm.vo.review.ReviewSelectionResponse;
import com.team6.onandthefarm.vo.review.ReviewSelectionResponseResult;

public interface ReviewService {
	Long saveReview(ReviewFormDto reviewFormDto);
	Long updateReview(ReviewUpdateFormDto reviewUpdateFormDto);
	Long deleteReview(ReviewDeleteDto reviewDeleteDto);
	Long upLikeCountReview(ReviewLikeFormDto reviewLikeFormDto);
	Long cancelReviewLikeCount(ReviewLikeCancelFormDto reviewLikeCancelFormDto);
	ReviewInfoResponse getReviewInfo(Long productId);
	ReviewSelectionResponseResult getReviewListByLikeCount(Long userId, Long productId, Integer pageNumber);
	ReviewSelectionResponseResult getReviewListOrderByNewest(Long userId, Long productId, Integer pageNumber);
	ReviewSelectionResponseResult getReviewBySellerNewest(Long sellerId, Integer pageNumber);
	ReviewSelectionResponseResult getMyReview(Long userId, Integer pageNumber);
}
