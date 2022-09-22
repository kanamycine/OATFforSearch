package com.team6.onandthefarm.vo.order;

import io.swagger.annotations.ApiResponses;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * 셀러가 주문내역 조회를 하기 위해 사용하는 응답 객체
 */
public class OrderSellerResponse {
    private String ordersId;
    private String ordersSerial;
    private String ordersDate;
    private String ordersStatus;
}
