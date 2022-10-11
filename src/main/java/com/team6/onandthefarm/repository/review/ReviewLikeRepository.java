package com.team6.onandthefarm.repository.review;

import com.team6.onandthefarm.entity.product.Product;
import com.team6.onandthefarm.entity.user.User;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.team6.onandthefarm.entity.review.ReviewLike;

import java.util.List;
import java.util.Optional;

public interface ReviewLikeRepository extends CrudRepository<ReviewLike, Long> {
    @Query("select r from ReviewLike r join fetch r.user where r.review.reviewId =:reviewId and r.user.userId =:userId")
    Optional<ReviewLike> findReviewLikeByUser(@Param("userId")Long userId, @Param("reviewId")Long reviewId);
}
