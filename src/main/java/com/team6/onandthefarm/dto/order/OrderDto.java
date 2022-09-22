package com.team6.onandthefarm.dto.order;

import com.team6.onandthefarm.vo.order.OrderFindOneResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long sellerId;
    private String orderRecipientName;
    private String orderAddress;
    private String orderPhone;
    private String orderRequest;
    private List<OrderFindOneResponse> productList;
}
