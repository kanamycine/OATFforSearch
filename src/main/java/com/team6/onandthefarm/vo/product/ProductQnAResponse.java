package com.team6.onandthefarm.vo.product;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductQnAResponse {
    private Long productQnaId;

    private String productQnaContent;

    private String productQnaCreatedAt;

    private String productQnaModifiedAt;

    private String productQnaStatus;

    private String productQnaCategory;

    private String productSellerAnswer;

    private String userName;

    private String userProfileImg;

}
