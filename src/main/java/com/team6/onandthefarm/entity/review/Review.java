package com.team6.onandthefarm.entity.review;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Builder
@Slf4j
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long reviewId;
    private Long productId;
    private Long userId;
    private Long sellerId;
    private String reviewContent;
    private String reviewCreatedAt;
    private String reviewModifiedAt;
    private Integer reviewLikeCount;
    private Integer reviewRate;
}
