package com.team6.onandthefarm.repository.product;

import com.team6.onandthefarm.entity.product.Product;
import com.team6.onandthefarm.entity.product.ProductQna;
import com.team6.onandthefarm.entity.seller.Seller;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductQnaRepository extends CrudRepository<ProductQna,Long> {
    List<ProductQna> findBySeller(Seller seller);

    List<ProductQna> findByProduct(Product product);

    List<ProductQna> findByUser_UserId(Long userId);
}
