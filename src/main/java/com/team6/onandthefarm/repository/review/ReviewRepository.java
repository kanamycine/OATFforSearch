package com.team6.onandthefarm.repository.review;

import java.util.List;
import java.util.Optional;

import com.team6.onandthefarm.entity.product.Product;
import com.team6.onandthefarm.entity.seller.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.team6.onandthefarm.entity.review.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	// @Query("select r from Review r join fetch r.product p join fetch p.category join fetch p.seller where r.product.productId =:productId order by r.reviewLikeCount DESC")
	// List<Review> findReviewListByLikeCount(@Param("productId") Long productId);

	// @Query("select r from Review r join fetch r.product p join fetch p.category join fetch p.seller where r.reviewStatus ='created' order by r.reviewCreatedAt DESC")
	// List<Review> findReviewListByNewest();

	//List<Review> findReviewsByProductOrderByReviewLikeCountDesc(Product product);

	List<Review> findBySellerOrderByReviewCreatedAtDesc(Seller seller);

	Optional<Review> findReviewByOrderProductId(Long orderProductId);

	List<Review> findReviewByProduct(Product product);

	List<Review> findReviewByProductAndReviewRate(Product product, Integer reviewRate);

}
