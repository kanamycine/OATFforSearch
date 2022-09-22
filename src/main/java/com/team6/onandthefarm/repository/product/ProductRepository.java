package com.team6.onandthefarm.repository.product;

import org.springframework.data.repository.CrudRepository;

import com.team6.onandthefarm.entity.product.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {
}

