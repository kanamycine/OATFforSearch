package com.team6.onandthefarm.service.order;

import com.team6.onandthefarm.dto.order.*;
import com.team6.onandthefarm.vo.order.*;

import java.util.List;

public interface OrderService {
    public OrderSheetResponse findOneByProductId(OrderSheetDto orderSheetDto);
    public List<OrderFindOneResponse> findCartByUserId(Long userId);
    public Boolean createOrder(OrderDto orderDto);
    public List<OrderSellerResponseList> findSellerOrders(OrderSellerFindDto orderSellerFindDto);
    public OrderSellerDetailResponse findSellerOrderDetail(String orderSerial);
    public void createPayment(String orderSerial);
    public List<OrderSellerResponse> findSellerClaims(OrderSellerRequest orderSellerRequest);
    public boolean createCancel(RefundDto refundDto);
    public Boolean createRefund(RefundDto refundDto);
    public RefundDetailResponse findRefundDetail(Long orderProductId);
    public Boolean conformRefund(Long orderProductId);
    public Boolean deliveryStart(OrderDeliveryDto orderDeliveryDto);
    public Boolean deliveryConform(String orderSerial);
}
