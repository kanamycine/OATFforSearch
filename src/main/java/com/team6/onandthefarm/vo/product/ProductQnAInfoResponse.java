package com.team6.onandthefarm.vo.product;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class ProductQnAInfoResponse {

    private List<ProductQnAResponse> productQnAResponseList;
    private Integer currentPageNum;
    private Integer totalPageNum;
    private Integer totalElementNum;

}
