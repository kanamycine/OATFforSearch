package com.team6.onandthefarm.repository.review;

import com.team6.onandthefarm.entity.user.User;
import org.springframework.data.repository.CrudRepository;

import com.team6.onandthefarm.entity.review.ReviewLike;

import java.util.Optional;

public interface ReviewLikeRepository extends CrudRepository<ReviewLike, Long> {

    Optional<ReviewLike> findReviewLikeByUser(User user);

}
