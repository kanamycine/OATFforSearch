package com.team6.onandthefarm.vo.order;

import lombok.*;

import java.util.List;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSellerResponseList {
    List<OrderSellerResponse> orderSellerResponses;
}
