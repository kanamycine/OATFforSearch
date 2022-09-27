package com.team6.onandthefarm.repository.product;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.team6.onandthefarm.entity.product.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {
	// List<Product> findAllByCategory(Long categoryId);

	@Query("select p from Product p join fetch p.category order by p.productPrice DESC")
	List<Product> findProductListByHighPrice();

	@Query("select p from Product p join fetch p.category order by p.productPrice ASC ")
	List<Product> findProductListByLowPrice();

	@Query("select p from Product p join fetch p.category order by p.productSoldCount DESC")
	List<Product> findProductBySoldCount();

	@Query("select p from Product p join fetch p.category where p.category.categoryId =:categoryId order by p.productRegisterDate DESC")
	List<Product> findProductsByCategoryNewest(@Param("categoryId") Long categoryId);





}
