package com.team6.onandthefarm.repository.review;

import org.springframework.data.repository.CrudRepository;

import com.team6.onandthefarm.entity.review.Review;

public interface ReviewRepository extends CrudRepository<Review, Long> {
}
