package com.team6.onandthefarm.service.order;

import com.team6.onandthefarm.dto.order.*;
import com.team6.onandthefarm.vo.order.*;

import java.util.List;

public interface OrderService {
    OrderSheetResponse findOneByProductId(OrderSheetDto orderSheetDto);
    List<OrderFindOneResponse> findCartByUserId(Long userId);
    Boolean createOrder(OrderDto orderDto);
    OrderSellerResponseListResponse findSellerOrders(OrderSellerFindDto orderSellerFindDto);
    OrderUserResponseListResponse findUserOrders(OrderUserFindDto orderUserFindDto);
    OrderUserDetailResponse findUserOrderDetail(String orderSerial);
    OrderUserDetailResponse findSellerOrderDetail(OrderSellerDetailDto orderSellerDetailDto);
    void createPayment(String orderSerial);
    OrderSellerResultResponse findSellerClaims(OrderSellerRequest orderSellerRequest);
    OrderRefundResultResponse findUserClaims(OrderUserFindDto orderUserFindDto);
    Boolean createCancel(RefundDto refundDto);
    Boolean createRefund(RefundDto refundDto);
    RefundDetailResponse findRefundDetail(Long orderProductId);
    Boolean conformRefund(Long orderProductId);
    Boolean deliveryStart(OrderDeliveryDto orderDeliveryDto);
    Boolean deliveryConform(String orderSerial);
    OrdersConditionResponse findOrdersCondition(Long sellerId);
}
