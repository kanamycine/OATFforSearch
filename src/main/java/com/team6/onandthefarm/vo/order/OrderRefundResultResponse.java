package com.team6.onandthefarm.vo.order;

import lombok.*;

import java.util.List;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRefundResultResponse {
    List<OrderRefundResponse> responses;

    private Integer currentPageNum;

    private Integer totalPageNum;
}
