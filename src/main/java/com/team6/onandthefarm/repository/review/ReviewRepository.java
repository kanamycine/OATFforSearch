package com.team6.onandthefarm.repository.review;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.team6.onandthefarm.entity.review.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	@Query("select r from Review r join fetch r.product p join fetch p.category join fetch p.seller where r.product.productId =:productId order by r.reviewLikeCount DESC")
	List<Review> findReviewListByLikeCount(@Param("productId") Long productId);
	//List<Review> findReviewsByProductOrderByReviewLikeCountDesc(Product product);

}
