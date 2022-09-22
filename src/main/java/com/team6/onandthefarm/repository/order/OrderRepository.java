package com.team6.onandthefarm.repository.order;

import com.team6.onandthefarm.entity.order.Orders;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Orders,Long> {
    List<Orders> findByOrdersSellerIdAndOrdersDateBetween(Long sellerId,String startDate,String endDate);

    Orders findByOrdersSerial(String orderSerial);
}
