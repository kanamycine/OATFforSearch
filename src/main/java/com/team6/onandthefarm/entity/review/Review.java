package com.team6.onandthefarm.entity.review;

import com.team6.onandthefarm.entity.product.Product;
import com.team6.onandthefarm.entity.seller.Seller;
import com.team6.onandthefarm.entity.user.User;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sellerId")
    private Seller seller;

    private String reviewContent;

    private String reviewCreatedAt;

    private String reviewModifiedAt;

    private Integer reviewLikeCount;

    private Integer reviewRate;

    private String reviewStatus;


    public Long updateReview(String reviewContent, Integer reviewRate){
        this.reviewContent = reviewContent;
        this.reviewRate = reviewRate;

        return reviewId;
    }
}
