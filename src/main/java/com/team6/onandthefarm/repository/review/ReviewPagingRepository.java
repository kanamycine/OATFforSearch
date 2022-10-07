package com.team6.onandthefarm.repository.review;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.team6.onandthefarm.entity.review.Review;

public interface ReviewPagingRepository extends PagingAndSortingRepository<Review, Long> {
	@Query("select r from Review r join fetch r.product p join fetch p.category join fetch p.seller where r.product.productId =:productId")
	List<Review> findReviewListByLikeCount(PageRequest pageRequest, @Param("productId") Long productId);

	@Query("select r from Review r join fetch r.product p join fetch p.category join fetch p.seller where r.product.productId =:productId")
	List<Review> findReviewListByNewest(PageRequest pageRequest, @Param("productId") Long productId);

	//@Query("select r from Review r join fetch r.product p join fetch p.category c join fetch p.seller s where r.seller.sellerId =:sellerId")
	@Query("select r from Review r join fetch r.seller s where r.seller.sellerId =:sellerId")
	List<Review> findReviewListBySeller(PageRequest pageRequest, @Param("sellerId") Long sellerId);
}
