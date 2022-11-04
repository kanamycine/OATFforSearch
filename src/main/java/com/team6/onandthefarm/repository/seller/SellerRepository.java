package com.team6.onandthefarm.repository.seller;

import com.team6.onandthefarm.dto.seller.SellerDto;
import com.team6.onandthefarm.entity.seller.Seller;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepository extends CrudRepository<Seller,Long> {

    Optional<Seller> findBySellerEmail(String email);
    Optional<Seller> findBySellerEmailAndAndSellerPhone(String sellerEmail, String sellerPhone);
}
