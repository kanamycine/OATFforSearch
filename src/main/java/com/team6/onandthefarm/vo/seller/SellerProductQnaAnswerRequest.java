package com.team6.onandthefarm.vo.seller;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerProductQnaAnswerRequest {
    private String productQnaId;

    private String productQnaAnswerContent;
}
