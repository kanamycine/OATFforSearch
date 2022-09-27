package com.team6.onandthefarm.repository.product;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.team6.onandthefarm.entity.product.Product;

public interface ProductPagingRepository extends PagingAndSortingRepository<Product, Long> {

}
