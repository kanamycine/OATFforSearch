package com.team6.onandthefarm.controller.review;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team6.onandthefarm.dto.review.ReviewFormDto;
import com.team6.onandthefarm.dto.review.ReviewUpdateFormDto;
import com.team6.onandthefarm.entity.review.Review;
import com.team6.onandthefarm.service.review.ReviewService;
import com.team6.onandthefarm.util.BaseResponse;
import com.team6.onandthefarm.vo.review.ReviewFormRequest;
import com.team6.onandthefarm.vo.review.ReviewUpdateFormRequest;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/review/")
@RequiredArgsConstructor
public class ReviewController {
	private final ReviewService reviewService;

	@PostMapping("new")
	@ApiOperation("리뷰 신규 등록")
	public ResponseEntity<BaseResponse<Review>> reviewForm(@RequestBody ReviewFormRequest reviewFormRequest) throws Exception{

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		ReviewFormDto reviewFormDto = modelMapper.map(reviewFormRequest, ReviewFormDto.class);

		Long reviewId = reviewService.saveReview(reviewFormDto);

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.CREATED)
				.message("review register completed")
				.data(reviewId)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.CREATED);
	}

	@PutMapping(value="update")
	@ApiOperation("리뷰 수정")
	public ResponseEntity<BaseResponse<Review>> reviewUpdateForm(@RequestBody ReviewUpdateFormRequest reviewUpdateFormRequest) throws Exception{
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		ReviewUpdateFormDto reviewUpdateFormDto = modelMapper.map(reviewUpdateFormRequest, ReviewUpdateFormDto.class);

		Long reviewId = reviewService.updateReview(reviewUpdateFormDto);

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("review update completed")
				.data(reviewId)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}
}
