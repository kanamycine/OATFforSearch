package com.team6.onandthefarm.repository.cart;

import com.team6.onandthefarm.entity.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
