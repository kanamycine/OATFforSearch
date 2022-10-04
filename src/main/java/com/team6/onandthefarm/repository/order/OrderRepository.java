package com.team6.onandthefarm.repository.order;

import com.team6.onandthefarm.entity.order.Orders;
import com.team6.onandthefarm.entity.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Orders,Long> {
    List<Orders> findByOrdersStatusAndOrdersDateBetween(String orderStatus,String startDate,String endDate);

    List<Orders> findByOrdersSellerId(Long sellerId);

    Orders findByOrdersSerial(String orderSerial);

    List<Orders> findByOrdersSellerIdAndOrdersStatusAndOrdersDateBetween(Long sellerId,String ordersStatus,String startDate,String endDate);

    List<Orders> findByUser(User user);

    @Query("select o from Orders o join fetch o.user u where o.user.userId=:userId and o.ordersDeliveryStatus = 'd2' and o.ordersStatus <> 'o2'")
    List<Orders> findWithOrderAndDeliveryStatus(@Param("userId") Long userId);
}
