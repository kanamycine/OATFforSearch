package com.team6.onandthefarm.repository.product;

import com.team6.onandthefarm.entity.product.Product;
import com.team6.onandthefarm.entity.product.ProductImg;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductImgRepository extends CrudRepository<ProductImg, Long> {

    List<ProductImg> findByProduct(Product product);
}
