package com.team6.onandthefarm.repository.product;

import com.team6.onandthefarm.entity.product.Product;
import com.team6.onandthefarm.entity.product.ProductQna;
import com.team6.onandthefarm.entity.seller.Seller;
import com.team6.onandthefarm.entity.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductQnaRepository extends CrudRepository<ProductQna,Long> {
    List<ProductQna> findBySeller(Seller seller);

    List<ProductQna> findByProduct(Product product);

    List<ProductQna> findByUser(User user);

    @Query("select q from ProductQna q where q.seller.sellerId=:sellerId and q.productQnaStatus='waiting'")
    List<ProductQna> findBeforeAnswerProductQna(@Param("sellerId") Long sellerId);
}
