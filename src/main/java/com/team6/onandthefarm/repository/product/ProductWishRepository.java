package com.team6.onandthefarm.repository.product;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.team6.onandthefarm.entity.product.Wish;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductWishRepository extends CrudRepository<Wish, Long> {

    @Query("select w from Wish w join fetch w.product where w.user.userId=:userId")
    List<Wish> findWishListByUserId(@Param("userId") Long userId);

}
