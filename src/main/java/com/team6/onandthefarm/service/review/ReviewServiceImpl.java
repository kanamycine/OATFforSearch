package com.team6.onandthefarm.service.review;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.team6.onandthefarm.entity.order.OrderProduct;
import com.team6.onandthefarm.entity.user.User;
import com.team6.onandthefarm.repository.order.OrderProductRepository;
import com.team6.onandthefarm.repository.user.UserRepository;
import com.team6.onandthefarm.vo.PageVo;
import com.team6.onandthefarm.vo.review.ReviewInfoResponse;
import com.team6.onandthefarm.vo.review.ReviewSelectionResponseResult;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team6.onandthefarm.dto.review.ReviewDeleteDto;
import com.team6.onandthefarm.dto.review.ReviewFormDto;
import com.team6.onandthefarm.dto.review.ReviewLikeCancelFormDto;
import com.team6.onandthefarm.dto.review.ReviewLikeFormDto;
import com.team6.onandthefarm.dto.review.ReviewUpdateFormDto;
import com.team6.onandthefarm.entity.product.Product;
import com.team6.onandthefarm.entity.review.Review;
import com.team6.onandthefarm.entity.review.ReviewLike;
import com.team6.onandthefarm.repository.product.ProductRepository;
import com.team6.onandthefarm.repository.review.ReviewLikeRepository;
import com.team6.onandthefarm.repository.review.ReviewPagingRepository;
import com.team6.onandthefarm.repository.review.ReviewRepository;
import com.team6.onandthefarm.util.DateUtils;
import com.team6.onandthefarm.vo.review.ReviewSelectionResponse;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService{

	private final ReviewRepository reviewRepository;
	private final ReviewPagingRepository reviewPagingRepository;
	private final ReviewLikeRepository reviewLikeRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final OrderProductRepository orderProductRepository;

	private final DateUtils dateUtils;
	private final Environment env;

	@Autowired ReviewServiceImpl(ReviewRepository reviewRepository, ReviewPagingRepository reviewPagingRepository,
								 ReviewLikeRepository reviewLikeRepository, UserRepository userRepository,
								 ProductRepository productRepository, OrderProductRepository orderProductRepository,
								 DateUtils dateUtils, Environment env){
		this.reviewRepository = reviewRepository;
		this.reviewPagingRepository = reviewPagingRepository;
		this.reviewLikeRepository = reviewLikeRepository;
		this.userRepository = userRepository;
		this.productRepository = productRepository;
		this.orderProductRepository = orderProductRepository;
		this.dateUtils = dateUtils;
		this.env = env;
	}

	public Long saveReview(ReviewFormDto reviewFormDto){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		Review review = modelMapper.map(reviewFormDto, Review.class);

		Optional<OrderProduct> orderProduct = orderProductRepository.findById(reviewFormDto.getOrderProductId());
		Long productId = orderProduct.get().getProductId();
		Optional<Product> product = productRepository.findById(productId);
		Long userId = reviewFormDto.getUserId();
		Optional<User> user = userRepository.findById(userId);

		review.setSeller(product.get().getSeller());
		review.setProduct(product.get());
		review.setUser(user.get());
		review.setReviewCreatedAt((dateUtils.transDate(env.getProperty("dateutils.format"))));
		review.setReviewLikeCount(0);
		review.setReviewStatus("created");

		return reviewRepository.save(review).getReviewId();
	}

	public Long updateReview(ReviewUpdateFormDto reviewUpdateFormDto){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		Optional<Review> review = reviewRepository.findById(reviewUpdateFormDto.getReviewId());
		long reviewId = review.get().updateReview(reviewUpdateFormDto.getReviewContent(), reviewUpdateFormDto.getReviewRate());

		review.get().setReviewModifiedAt(dateUtils.transDate(env.getProperty("dateutils.format")));

		return reviewId;
	}

	public Long deleteReview(ReviewDeleteDto reviewDeleteDto){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		Optional<Review> review = reviewRepository.findById(reviewDeleteDto.getReviewId());
		//product.get().setProductStatus("deleted");
		review.get().setReviewStatus("deleted");
		review.get().setReviewModifiedAt(dateUtils.transDate(env.getProperty("dateutils.format")));

		return review.get().getReviewId();
	}


	public Long upLikeCountReview(ReviewLikeFormDto reviewLikeFormDto){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		Optional<User> user = userRepository.findById(reviewLikeFormDto.getUserId());
		Optional<ReviewLike> savedReviewLike = reviewLikeRepository.findReviewLikeByUser(reviewLikeFormDto.getUserId(), reviewLikeFormDto.getReviewId());

		Long reviewLikeId = null;
		if(savedReviewLike.isPresent()){
			reviewLikeId = savedReviewLike.get().getReviewLikeId();
		}
		else {
			ReviewLike reviewLike = modelMapper.map(reviewLikeFormDto, ReviewLike.class);

			Optional<Review> review = reviewRepository.findById(reviewLikeFormDto.getReviewId());
			review.get().setReviewLikeCount(review.get().getReviewLikeCount() + 1);
			reviewLike.setReview(review.get());
			reviewLike.setUser(user.get());

			reviewLikeId = reviewLikeRepository.save(reviewLike).getReviewLikeId();
		}

		return reviewLikeId;
	}

	public Long cancelReviewLikeCount(ReviewLikeCancelFormDto reviewLikeCancelFormDto){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);


		Long reviewId = reviewLikeCancelFormDto.getReviewId();
		Long userId = reviewLikeCancelFormDto.getUserId();
		ReviewLike reviewLike = reviewLikeRepository.findReviewLikeByUser(userId, reviewId).get();
		Long reviewLikeId = reviewLike.getReviewLikeId();

		reviewLikeRepository.delete(reviewLike);
		Optional<Review> review = reviewRepository.findById(reviewLikeCancelFormDto.getReviewId());
		review.get().setReviewLikeCount(review.get().getReviewLikeCount() - 1);

		return reviewLikeId;
	}

	@Override
	public ReviewInfoResponse getReviewInfo(Long productId) {

		Optional<Product> product = productRepository.findById(productId);

		int totalCountOfReview = 0;
		int sumOfReview = 0;
		Integer[] reviewRates = new Integer[6];
		for(int i=1; i<=5; i++) {
			List<Review> reviewListByReviewRate = reviewRepository.findReviewByProductAndReviewRate(product.get(), i);
			reviewRates[i] = reviewListByReviewRate.size();
			totalCountOfReview += reviewListByReviewRate.size();

			sumOfReview += (reviewListByReviewRate.size() * i);
		}

		Double avgOfReview = 0.0;
		if(totalCountOfReview > 0){
			avgOfReview = ((double)sumOfReview/totalCountOfReview);
		}
		ReviewInfoResponse reviewInfoResponse = ReviewInfoResponse.builder()
				.reviewCount(totalCountOfReview)
				.reviewRate(avgOfReview)
				.reviewFiveCount(reviewRates[5])
				.reviewFourCount(reviewRates[4])
				.reviewThreeCount(reviewRates[3])
				.reviewTwoCount(reviewRates[2])
				.reviewOneCount(reviewRates[1])
				.build();

		return reviewInfoResponse;
	}


	public ReviewSelectionResponseResult getReviewListByLikeCount(Long userId, Long productId, Integer pageNumber){

		ReviewSelectionResponseResult reviewSelectionResponseResult = new ReviewSelectionResponseResult();

		PageRequest pageRequest = PageRequest.of(pageNumber, 8, Sort.by("reviewLikeCount").descending());
		Page<Review> reviews = reviewPagingRepository.findReviewListOrderBy(pageRequest, productId);

		int totalPage = reviews.getTotalPages();
		Long totalElements = reviews.getTotalElements();
		PageVo pageVo = PageVo.builder()
				.totalPage(totalPage)
				.nowPage(pageNumber)
				.totalElement(totalElements)
				.build();
		reviewSelectionResponseResult.setPageVo(pageVo);

		List<ReviewSelectionResponse> reviewResponses = new ArrayList<>();
		for (Review review : reviews) {
			ReviewSelectionResponse reviewSelectionResponse = ReviewSelectionResponse
					.builder()
					.reviewId(review.getReviewId())
					.reviewContent(review.getReviewContent())
					.reviewCreatedAt(review.getReviewCreatedAt())
					.reviewModifiedAt(review.getReviewModifiedAt())
					.reviewLikeCount(review.getReviewLikeCount())
					.reviewRate(review.getReviewRate())
					.userName(review.getUser().getUserName())
					.userProfileImg(review.getUser().getUserProfileImg())
					.isMyReview(false)
					.isAvailableUp(true)
					.build();

			if(review.getUser().getUserId() == userId){
				reviewSelectionResponse.setIsMyReview(true);
			}
			Optional<ReviewLike> reviewLike = reviewLikeRepository.findReviewLikeByUser(userId, review.getReviewId());
			if(reviewLike.isPresent()){
				reviewSelectionResponse.setIsAvailableUp(false);
			}
			reviewResponses.add(reviewSelectionResponse);
		}
		reviewSelectionResponseResult.setReviewSelectionResponses(reviewResponses);

		return reviewSelectionResponseResult;
	}

	public ReviewSelectionResponseResult getReviewListOrderByNewest(Long userId, Long productId, Integer pageNumber) {

		ReviewSelectionResponseResult reviewSelectionResponseResult = new ReviewSelectionResponseResult();

		PageRequest pageRequest = PageRequest.of(pageNumber, 8, Sort.by("reviewCreatedAt").descending());
		Page<Review> reviews = reviewPagingRepository.findReviewListOrderBy(pageRequest, productId);

		int totalPage = reviews.getTotalPages();
		Long totalElements = reviews.getTotalElements();
		PageVo pageVo = PageVo.builder()
				.totalPage(totalPage)
				.nowPage(pageNumber)
				.totalElement(totalElements)
				.build();
		reviewSelectionResponseResult.setPageVo(pageVo);

		List<ReviewSelectionResponse> reviewResponse = new ArrayList<>();
		for (Review review : reviews) {
			ReviewSelectionResponse reviewSelectionResponse = ReviewSelectionResponse
					.builder()
					.reviewId(review.getReviewId())
					.reviewContent(review.getReviewContent())
					.reviewCreatedAt(review.getReviewCreatedAt())
					.reviewModifiedAt(review.getReviewModifiedAt())
					.reviewLikeCount(review.getReviewLikeCount())
					.reviewRate(review.getReviewRate())
					.userName(review.getUser().getUserName())
					.userProfileImg(review.getUser().getUserProfileImg())
					.isMyReview(false)
					.isAvailableUp(true)
					.build();

			if(review.getUser().getUserId() == userId){
				reviewSelectionResponse.setIsMyReview(true);
			}
			Optional<ReviewLike> reviewLike = reviewLikeRepository.findReviewLikeByUser(userId, review.getReviewId());
			if(reviewLike.isPresent()){
				reviewSelectionResponse.setIsAvailableUp(false);
			}
			reviewResponse.add(reviewSelectionResponse);
		}
		reviewSelectionResponseResult.setReviewSelectionResponses(reviewResponse);

		return reviewSelectionResponseResult;
	}

	public ReviewSelectionResponseResult getReviewBySellerNewest(Long sellerId, Integer pageNumber) {

		ReviewSelectionResponseResult reviewSelectionResponseResult = new ReviewSelectionResponseResult();

		PageRequest pageRequest = PageRequest.of(pageNumber, 8, Sort.by("reviewCreatedAt").descending());
		Page<Review> reviews = reviewPagingRepository.findReviewListBySeller(pageRequest, sellerId);

		int totalPage = reviews.getTotalPages();
		Long totalElements = reviews.getTotalElements();
		PageVo pageVo = PageVo.builder()
				.totalPage(totalPage)
				.nowPage(pageNumber)
				.totalElement(totalElements)
				.build();
		reviewSelectionResponseResult.setPageVo(pageVo);

		List<ReviewSelectionResponse> reviewResponse = new ArrayList<>();
		for (Review review : reviews) {
			ReviewSelectionResponse reviewSelectionResponse = ReviewSelectionResponse.builder()
					.reviewId(review.getReviewId())
					.reviewContent(review.getReviewContent())
					.reviewCreatedAt(review.getReviewCreatedAt())
					.reviewModifiedAt(review.getReviewModifiedAt())
					.reviewLikeCount(review.getReviewLikeCount())
					.reviewRate(review.getReviewRate())
					.userProfileImg(review.getUser().getUserProfileImg())
					.userEmail(review.getUser().getUserEmail())
					.productMainImgSrc(review.getProduct().getProductMainImgSrc())
					.productName(review.getProduct().getProductName())
					.userName(review.getUser().getUserName())
					.userProfileImg(review.getUser().getUserProfileImg())
					.build();
			reviewResponse.add(reviewSelectionResponse);
		}
		reviewSelectionResponseResult.setReviewSelectionResponses(reviewResponse);

		return reviewSelectionResponseResult;
	}

	public ReviewSelectionResponseResult getMyReview(Long userId, Integer pageNumber) {

		ReviewSelectionResponseResult reviewSelectionResponseResult = new ReviewSelectionResponseResult();

		PageRequest pageRequest = PageRequest.of(pageNumber, 8, Sort.by("reviewCreatedAt").descending());
		Page<Review> reviews = reviewPagingRepository.findReviewListByUser(pageRequest, userId);

		int totalPage = reviews.getTotalPages();
		Long totalElements = reviews.getTotalElements();
		PageVo pageVo = PageVo.builder()
				.totalPage(totalPage)
				.nowPage(pageNumber)
				.totalElement(totalElements)
				.build();
		reviewSelectionResponseResult.setPageVo(pageVo);

		List<ReviewSelectionResponse> reviewResponse = new ArrayList<>();
		for (Review review : reviews) {
			ReviewSelectionResponse reviewSelectionResponse = ReviewSelectionResponse.builder()
					.reviewId(review.getReviewId())
					.reviewContent(review.getReviewContent())
					.reviewCreatedAt(review.getReviewCreatedAt())
					.reviewModifiedAt(review.getReviewModifiedAt())
					.reviewLikeCount(review.getReviewLikeCount())
					.reviewRate(review.getReviewRate())
					.userProfileImg(review.getUser().getUserProfileImg())
					.userEmail(review.getUser().getUserEmail())
					.productMainImgSrc(review.getProduct().getProductMainImgSrc())
					.productName(review.getProduct().getProductName())
					.userName(review.getUser().getUserName())
					.userProfileImg(review.getUser().getUserProfileImg())
					.isAvailableUp(true)
					.build();
			Optional<ReviewLike> reviewLike = reviewLikeRepository.findReviewLikeByUser(userId, review.getReviewId());
			if(reviewLike.isPresent()){
				reviewSelectionResponse.setIsAvailableUp(false);
			}
			reviewResponse.add(reviewSelectionResponse);
		}
		reviewSelectionResponseResult.setReviewSelectionResponses(reviewResponse);

		return reviewSelectionResponseResult;
	}
}
