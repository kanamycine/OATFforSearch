package com.team6.onandthefarm.repository.seller;

import com.team6.onandthefarm.dto.seller.SellerDto;
import com.team6.onandthefarm.entity.seller.Seller;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends CrudRepository<Seller,Long> {

    Seller findBySellerEmail(String email);
    Seller findBySellerEmailAndSellerPassword(String sellerEmail, String sellerPassword);

    Seller findBySellerEmailAndAndSellerPhone(String sellerEmail, String sellerPhone);
}
