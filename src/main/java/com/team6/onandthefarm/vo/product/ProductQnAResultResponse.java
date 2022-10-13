package com.team6.onandthefarm.vo.product;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductQnAResultResponse {
    List<ProductQnAResponse> responses;

    private Integer currentPageNum;

    private Integer totalPageNum;
}
