package com.team6.onandthefarm.repository.order;

import com.team6.onandthefarm.entity.order.Refund;
import org.springframework.data.repository.CrudRepository;

public interface RefundRepository extends CrudRepository<Refund,Long> {
    Refund findByOrderProductId(Long orderProductId);
}
