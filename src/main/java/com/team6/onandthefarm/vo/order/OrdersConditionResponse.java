package com.team6.onandthefarm.vo.order;

import com.team6.onandthefarm.entity.order.Orders;
import com.team6.onandthefarm.entity.product.Product;
import com.team6.onandthefarm.entity.product.ProductQna;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrdersConditionResponse {

    private Integer beforeDelivery;
    private Integer requestRefund;
    private Integer cancelOrders;
    private Integer delivering;
    private Integer notSelling;
    private Integer beforeAnswer;

}
