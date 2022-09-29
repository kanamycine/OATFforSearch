package com.team6.onandthefarm.repository.cart;

import com.team6.onandthefarm.entity.cart.Cart;
import com.team6.onandthefarm.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("select c from Cart c join fetch c.product p where c.user.userId =:userId and c.cartStatus is true")
    List<Cart> findNotDeletedCartByUserId(@Param("userId") Long userId);

    List<Cart> findByUser(User user);
}
