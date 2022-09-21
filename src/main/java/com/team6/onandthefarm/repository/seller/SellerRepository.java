package com.team6.onandthefarm.repository.seller;

import com.team6.onandthefarm.entity.seller.SellerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends CrudRepository<SellerEntity,Long> {

    SellerEntity findByEmail(String email);
}
