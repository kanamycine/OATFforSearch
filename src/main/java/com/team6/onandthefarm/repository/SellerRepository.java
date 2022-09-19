package com.team6.onandthefarm.repository;

import com.team6.onandthefarm.entity.SellerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends CrudRepository<SellerEntity,Long> {

    SellerEntity findByEmail(String email);
}
