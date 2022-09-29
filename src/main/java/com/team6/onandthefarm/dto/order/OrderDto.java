package com.team6.onandthefarm.dto.order;

import com.team6.onandthefarm.vo.order.OrderFindOneResponse;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long sellerId;
    private Long userId;
    private String orderRecipientName;
    private String orderAddress;
    private String orderPhone;
    private String orderRequest;
    private List<OrderProductDto> productList;
    private Map<Long,Long> prodSeller;
}
