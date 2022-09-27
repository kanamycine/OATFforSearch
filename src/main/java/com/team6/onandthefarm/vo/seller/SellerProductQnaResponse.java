package com.team6.onandthefarm.vo.seller;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerProductQnaResponse {

    private String productQnaContent;

    private String productQnaCreatedAt;

    private String productQnaModifiedAt;

    private Boolean productQnaStatus;

    private String productQnaCategory;
}
