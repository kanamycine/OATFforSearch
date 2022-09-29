package com.team6.onandthefarm.controller.review;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team6.onandthefarm.dto.review.ReviewDeleteDto;
import com.team6.onandthefarm.dto.review.ReviewFormDto;
import com.team6.onandthefarm.dto.review.ReviewLikeCancelFormDto;
import com.team6.onandthefarm.dto.review.ReviewLikeFormDto;
import com.team6.onandthefarm.dto.review.ReviewUpdateFormDto;
import com.team6.onandthefarm.entity.review.Review;
import com.team6.onandthefarm.entity.review.ReviewLike;
import com.team6.onandthefarm.service.review.ReviewService;
import com.team6.onandthefarm.util.BaseResponse;
import com.team6.onandthefarm.vo.review.ReviewDeleteRequest;
import com.team6.onandthefarm.vo.review.ReviewFormRequest;
import com.team6.onandthefarm.vo.review.ReviewLikeCancelFormRequest;
import com.team6.onandthefarm.vo.review.ReviewLikeFormRequest;
import com.team6.onandthefarm.vo.review.ReviewSelectionResponse;
import com.team6.onandthefarm.vo.review.ReviewUpdateFormRequest;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user/review")
@RequiredArgsConstructor
public class ReviewController {
	private final ReviewService reviewService;

	@PostMapping("/new")
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

	@PutMapping(value="/update")
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

	@PutMapping(value="/delete")
	public ResponseEntity<BaseResponse<Review>> productDelete(@RequestBody ReviewDeleteRequest reviewDeleteRequest) throws Exception{
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		ReviewDeleteDto reviewDeleteDto = modelMapper.map(reviewDeleteRequest, ReviewDeleteDto.class);

		Long reviewId = reviewService.deleteReview(reviewDeleteDto);

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("review delete completed")
				.data(reviewId)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}
	// review like
	@PostMapping(value="/like/up")
	@ApiOperation("리뷰 좋아요 +1")
	public ResponseEntity<BaseResponse<ReviewLike>> upReviewLikeCount(@RequestBody ReviewLikeFormRequest reviewLikeFormRequest) throws Exception{
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		ReviewLikeFormDto reviewLikeFormDto = modelMapper.map(reviewLikeFormRequest, ReviewLikeFormDto.class);
		Long reviewLikeId = reviewService.upLikeCountReview(reviewLikeFormDto);

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.CREATED)
				.message("up reviewLike Counts completed")
				.data(reviewLikeId)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}

	@DeleteMapping(value="/like/cancel")
	@ApiOperation("리뷰 좋아요 취소 -1")
	public ResponseEntity<BaseResponse<ReviewLike>> cancelReviewLikeCount(@RequestBody ReviewLikeCancelFormRequest reviewLikeCancelFormRequest) throws Exception{
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		ReviewLikeCancelFormDto reviewLikeCancelFormDto = modelMapper.map(reviewLikeCancelFormRequest, ReviewLikeCancelFormDto.class);
		Long reviewLikeId = reviewService.cancelReviewLikeCount(reviewLikeCancelFormDto);

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("Cancel reviewLike counts completed")
				.data(reviewLikeId)
				.build();
		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}

	//review like cancle
	@GetMapping("/list/orderby/likecount/{productId}/{page-no}")
	public ResponseEntity<BaseResponse<List<ReviewSelectionResponse>>> getReviewListByLikeCount(@PathVariable("productId") Long productId, @PathVariable("page-no") String pageNumber){

		List<ReviewSelectionResponse> reviews = reviewService.getReviewListByLikeCount(productId, Integer.valueOf(pageNumber));

		//필요 부분만 보내기 위해 (셀러, 프로덕트 짜르기)
		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("get reviews order by likecount completed")
				.data(reviews)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}


	@GetMapping("/list/orderby/newest/{productId}/{page-no}")
	public ResponseEntity<BaseResponse<List<ReviewSelectionResponse>>> getReviewOrderByNewest(@PathVariable("productId") Long productId, @PathVariable("page-no") String pageNumber){

		List<ReviewSelectionResponse> reviews = reviewService.getReviewListOrderByNewest(productId, Integer.valueOf(pageNumber));

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("get reviews order by registerdate completed")
				.data(reviews)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}
}
