package com.team6.onandthefarm.repository.product;

import com.team6.onandthefarm.entity.product.ProductQna;
import com.team6.onandthefarm.entity.product.ProductQnaAnswer;
import org.springframework.data.repository.CrudRepository;

public interface ProductQnaAnswerRepository extends CrudRepository<ProductQnaAnswer,Long> {
    ProductQnaAnswer findByProductQna(ProductQna productQna);
}
