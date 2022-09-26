package com.team6.onandthefarm.service.review;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team6.onandthefarm.dto.review.ReviewDeleteDto;
import com.team6.onandthefarm.dto.review.ReviewFormDto;
import com.team6.onandthefarm.dto.review.ReviewUpdateFormDto;
import com.team6.onandthefarm.entity.product.Product;
import com.team6.onandthefarm.entity.review.Review;
import com.team6.onandthefarm.repository.product.ProductRepository;
import com.team6.onandthefarm.repository.review.ReviewRepository;
import com.team6.onandthefarm.repository.seller.SellerRepository;
import com.team6.onandthefarm.util.DateUtils;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService{

	private ReviewRepository reviewRepository;
	private SellerRepository sellerRepository;
	private ProductRepository productRepository;
	private DateUtils dateUtils;
	private Environment env;

	@Autowired ReviewServiceImpl(ReviewRepository reviewRepository, SellerRepository sellerRepository,
			ProductRepository productRepository, DateUtils dateUtils, Environment env){
		this.reviewRepository = reviewRepository;
		this.sellerRepository = sellerRepository;
		this.productRepository = productRepository;
		this.dateUtils = dateUtils;
		this.env = env;
	}

	public Long saveReview(ReviewFormDto reviewFormDto){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		Review review = modelMapper.map(reviewFormDto, Review.class);

		Long productId = reviewFormDto.getProductId();
		Optional<Product> product = productRepository.findById(productId);

		// 유저 해야함
		review.setSeller(product.get().getSeller());
		review.setProduct(product.get());
		review.setReviewCreatedAt((dateUtils.transDate(env.getProperty("dateutils.format"))));
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

	public List<Review> getReviewListByLikeCount(Long productId){
		List<Review> reviews = reviewRepository.findReviewListByLikeCount(productId);
		return reviews;
	}
}
