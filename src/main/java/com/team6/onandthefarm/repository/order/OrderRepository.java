package com.team6.onandthefarm.repository.order;

import com.team6.onandthefarm.entity.order.Orders;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Orders,Long> {
    List<Orders> findByOrdersStatusAndOrdersDateBetween(String orderStatus,String startDate,String endDate);

    Orders findByOrdersSerial(String orderSerial);

    List<Orders> findByOrdersSellerIdAndOrdersStatusAndOrdersDateBetween(Long sellerId,String ordersStatus,String startDate,String endDate);
}
