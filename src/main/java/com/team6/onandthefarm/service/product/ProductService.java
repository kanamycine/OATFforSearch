package com.team6.onandthefarm.service.product;

import java.util.List;

import com.team6.onandthefarm.dto.product.ProductDeleteDto;
import com.team6.onandthefarm.dto.product.ProductFormDto;
import com.team6.onandthefarm.dto.product.ProductUpdateFormDto;
import com.team6.onandthefarm.entity.product.Product;

public interface ProductService {
	Long saveProduct(ProductFormDto productFormDto);
	Long updateProduct(ProductUpdateFormDto productUpdateFormDto);
	Long deleteProduct(ProductDeleteDto productDeleteDto);
	List<Product> getProductsListByHighPrice();
	List<Product> getProductsListByLowPrice();
	List<Product> getProductsBySoldCount();
	List<Product> getProductListByCategoryNewest(Long CategoryId);
}