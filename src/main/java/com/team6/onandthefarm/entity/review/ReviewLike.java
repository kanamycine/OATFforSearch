package com.team6.onandthefarm.entity.review;

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
@SequenceGenerator(
        name="REVIEW_LIKE_SEQ_GENERATOR",
        sequenceName = "REVIEW_LIKE_SEQ",
        initialValue = 100000, allocationSize = 1
)
public class ReviewLike {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "REVIEW_LIKE_SEQ_GENERATOR")
    private Long reviewLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewId")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

}
