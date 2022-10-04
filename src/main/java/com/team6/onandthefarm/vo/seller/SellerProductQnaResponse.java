package com.team6.onandthefarm.vo.seller;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerProductQnaResponse {
    private Long productQnaId;

    private String productQnaContent;

    private String productQnaCreatedAt;

    private String productQnaModifiedAt;

    private String productQnaStatus;

    private String productQnaCategory;
}
