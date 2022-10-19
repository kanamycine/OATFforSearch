package com.team6.onandthefarm.vo.product;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductQnAInfoResponse {

    private List<ProductQnAResponse> productQnAResponseList;
    private Integer qnACount;

}
