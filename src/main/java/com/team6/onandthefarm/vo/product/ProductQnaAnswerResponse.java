package com.team6.onandthefarm.vo.product;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductQnaAnswerResponse {
    private String productQnaAnswerContent;

    private String productQnaAnswerCreatedAt;

    private String productQnaAnswerModifiedAt;
}
