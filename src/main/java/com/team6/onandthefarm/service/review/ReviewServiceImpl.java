package com.team6.onandthefarm.service.review;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.team6.onandthefarm.entity.order.OrderProduct;
import com.team6.onandthefarm.entity.user.User;
import com.team6.onandthefarm.repository.order.OrderProductRepository;
import com.team6.onandthefarm.repository.user.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
import com.team6.onandthefarm.repository.seller.SellerRepository;
import com.team6.onandthefarm.util.DateUtils;
import com.team6.onandthefarm.vo.review.ReviewSelectionResponse;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService{

	private ReviewRepository reviewRepository;
	private ReviewPagingRepository reviewPagingRepository;
	private ReviewLikeRepository reviewLikeRepository;
	private SellerRepository sellerRepository;
	private UserRepository userRepository;
	private ProductRepository productRepository;
	private OrderProductRepository orderProductRepository;

	private DateUtils dateUtils;
	private Environment env;

	@Autowired ReviewServiceImpl(ReviewRepository reviewRepository, ReviewPagingRepository reviewPagingRepository,
								 ReviewLikeRepository reviewLikeRepository, SellerRepository sellerRepository, UserRepository userRepository,
								 ProductRepository productRepository, OrderProductRepository orderProductRepository,
								 DateUtils dateUtils, Environment env){
		this.reviewRepository = reviewRepository;
		this.reviewPagingRepository = reviewPagingRepository;
		this.reviewLikeRepository = reviewLikeRepository;
		this.sellerRepository = sellerRepository;
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
		Optional<ReviewLike> savedReviewLike = reviewLikeRepository.findReviewLikeByUser(user.get());

		Long reviewLikeId = null;
		if(savedReviewLike.isPresent()){
			reviewLikeId = savedReviewLike.get().getReviewLikeId();
		}
		else {
			ReviewLike reviewLike = modelMapper.map(reviewLikeFormDto, ReviewLike.class);

			Optional<Review> review = reviewRepository.findById(reviewLikeFormDto.getReviewId());
			reviewLike.setReview(review.get());
			reviewLike.setUser(user.get());

			reviewLikeId = reviewLikeRepository.save(reviewLike).getReviewLikeId();
		}

		return reviewLikeId;
	}

	public Long cancelReviewLikeCount(ReviewLikeCancelFormDto reviewLikeCancelFormDto){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);


		Long reviewLikeId = reviewLikeCancelFormDto.getReviewId();
		ReviewLike reviewLike = reviewLikeRepository.findById(reviewLikeId).get();

		reviewLikeRepository.delete(reviewLike);
		return reviewLikeId;
	}


	public List<ReviewSelectionResponse> getReviewListByLikeCount(Long productId, Integer pageNumber){
		// msa 고려하여 다시 설계할 것
		// Product product = productRepository.findById(productId).get();
		// List<Review> reviews = reviewRepository.findReviewsByProductOrderByReviewLikeCountDesc(product);
		List<ReviewSelectionResponse> reviewResponses = new ArrayList<>();
		PageRequest pageRequest = PageRequest.of(pageNumber, 8, Sort.by("reviewLikeCount").descending());
		List<Review> reviews = reviewPagingRepository.findReviewListByLikeCount(pageRequest, productId);
		for (Review review : reviews) {
			ReviewSelectionResponse reviewSelectionResponse = ReviewSelectionResponse
					.builder()
					.reviewId(review.getReviewId())
					.reviewContent(review.getReviewContent())
					.reviewCreatedAt(review.getReviewCreatedAt())
					.reviewModifiedAt(review.getReviewModifiedAt())
					.reviewLikeCount(review.getReviewLikeCount())
					.reviewRate(review.getReviewRate())
					.build();
			reviewResponses.add(reviewSelectionResponse);
		}
		return reviewResponses;
	}

	public List<ReviewSelectionResponse> getReviewListOrderByNewest(Long productId, Integer pageNumber) {
		List<ReviewSelectionResponse> reviewResponse = new ArrayList<>();
		PageRequest pageRequest = PageRequest.of(pageNumber, 8, Sort.by("reviewCreatedAt").descending());
		List<Review> reviews = reviewPagingRepository.findReviewListByNewest(pageRequest, productId);
		for (Review review : reviews) {
			ReviewSelectionResponse reviewSelectionResponse = ReviewSelectionResponse
					.builder()
					.reviewId(review.getReviewId())
					.reviewContent(review.getReviewContent())
					.reviewCreatedAt(review.getReviewCreatedAt())
					.reviewModifiedAt(review.getReviewModifiedAt())
					.reviewLikeCount(review.getReviewLikeCount())
					.reviewRate(review.getReviewRate())
					.build();
			reviewResponse.add(reviewSelectionResponse);
		}
		return reviewResponse;
	}

	public List<ReviewSelectionResponse> getReviewBySellerNewest(Long sellerId, Integer pageNummber) {

		PageRequest pageRequest = PageRequest.of(pageNummber, 8, Sort.by("reviewCreatedAt").descending());
		List<Review> reviews = reviewPagingRepository.findReviewListBySeller(pageRequest, sellerId);

		List<ReviewSelectionResponse> reviewResponse = new ArrayList<>();
		for (Review review : reviews) {
		ReviewSelectionResponse reviewSelectionResponse = ReviewSelectionResponse.builder()
				.reviewId(review.getReviewId())
				.reviewContent(review.getReviewContent())
				.reviewCreatedAt(review.getReviewCreatedAt())
				.reviewModifiedAt(review.getReviewModifiedAt())
				.reviewLikeCount(review.getReviewLikeCount())
				.reviewRate(review.getReviewRate())
				.build();
			reviewResponse.add(reviewSelectionResponse);
		}
		return reviewResponse;
	}
}
