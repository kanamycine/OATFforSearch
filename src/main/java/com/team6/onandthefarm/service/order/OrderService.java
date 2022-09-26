package com.team6.onandthefarm.service.order;

import com.team6.onandthefarm.dto.order.OrderDeliveryDto;
import com.team6.onandthefarm.dto.order.OrderDto;
import com.team6.onandthefarm.dto.order.OrderSellerFindDto;
import com.team6.onandthefarm.dto.order.RefundDto;
import com.team6.onandthefarm.vo.order.*;

import java.util.List;

public interface OrderService {
    public OrderFindOneResponse findOneByProductId(Long productId);
    public List<OrderFindOneResponse> findCartByUserId(Long userId);
    public void createOrder(OrderDto orderDto);
    public List<OrderSellerResponseList> findSellerOrders(OrderSellerFindDto orderSellerFindDto);
    public OrderSellerDetailResponse findSellerOrderDetail(String orderSerial);
    public void createPayment(String orderSerial);
    public List<OrderSellerResponse> findSellerClaims(OrderSellerRequest orderSellerRequest);
    public boolean createCancel(RefundDto refundDto);
    public boolean createRefund(RefundDto refundDto);
    public RefundDetailResponse findRefundDetail(Long orderProductId);
    public Boolean conformRefund(Long orderProductId);
    public Boolean deliveryStart(OrderDeliveryDto orderDeliveryDto);
    public Boolean deliveryConform(String orderSerial);
}
