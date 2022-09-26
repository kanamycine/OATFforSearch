package com.team6.onandthefarm.repository.order;

import com.team6.onandthefarm.entity.order.Payment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends CrudRepository<Payment,Long> {
}
