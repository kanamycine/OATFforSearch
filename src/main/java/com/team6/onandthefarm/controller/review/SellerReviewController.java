package com.team6.onandthefarm.controller.review;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team6.onandthefarm.service.review.ReviewService;
import com.team6.onandthefarm.util.BaseResponse;
import com.team6.onandthefarm.vo.review.ReviewSelectionResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/seller/review")
@RequiredArgsConstructor
public class SellerReviewController {
	private final ReviewService reviewService;

	@GetMapping("/list/by-seller/{sellerId}/{page-no}")
	public ResponseEntity<BaseResponse<List<ReviewSelectionResponse>>> getReviewBySellerNewest(@PathVariable("sellerId") Long sellerId, @PathVariable("page-no") String pageNumber){
		List<ReviewSelectionResponse> reviews = reviewService.getReviewBySellerNewest(sellerId, Integer.valueOf(pageNumber));

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("get reviews by seller completed")
				.data(reviews)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}
}
