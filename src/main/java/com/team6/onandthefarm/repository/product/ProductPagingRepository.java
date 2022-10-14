
package com.team6.onandthefarm.repository.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.team6.onandthefarm.entity.product.Product;

import org.springframework.data.repository.query.Param;

public interface ProductPagingRepository extends PagingAndSortingRepository<Product, Long> {

	@Query(value = "select p from Product p join fetch p.category join fetch p.seller",
			countQuery = "select count(p) from Product")
	Page<Product> findAllProductOrderByNewest(PageRequest pageRequest);

	@Query(value = "select p from Product p join fetch p.category join fetch p.seller",
			countQuery = "select count(p) from Product")
	Page<Product> findProductListByHighPrice(PageRequest pageRequest);

	@Query(value = "select p from Product p join fetch p.category join fetch p.seller",
			countQuery = "select count(p) from Product")
	Page<Product> findProductListByLowPrice(PageRequest pageRequest);

	@Query(value = "select p from Product p join fetch p.category join fetch p.seller",
			countQuery = "select count(p) from Product")
	Page<Product> findProductBySoldCount(PageRequest pageRequest);

	@Query(value = "select p from Product p join fetch p.category join fetch p.seller where p.seller.sellerId =:sellerId",
			countQuery = "select count(p) from Product")
	Page<Product> findProductBySellerNewest(PageRequest pageRequest, @Param("sellerId") Long sellerId);

	@Query(value = "select p from Product p join fetch p.category join fetch p.seller where p.category.categoryName =:category",
			countQuery = "select count(p) from Product")
	Page<Product> findProductsByCategoryNewest(PageRequest pageRequest, @Param("category") String category);

	@Query(value = "select p from Product p join fetch p.category join fetch p.seller where p.category.categoryName =:category",
			countQuery = "select count(p) from Product")
	Page<Product> findProductByCategoryHighest(PageRequest pageRequest, @Param("category") String category);

	@Query(value = "select p from Product p join fetch p.category join fetch p.seller where p.category.categoryName =:category",
			countQuery = "select count(p) from Product")
	Page<Product> findProductByCategoryLowest(PageRequest pageRequest, @Param("category") String category);

	@Query(value = "select p from Product p join fetch p.category join fetch p.seller where p.category.categoryName =:category",
			countQuery = "select count(p) from Product")
	Page<Product> findProductByCategorySoldCount(PageRequest pageRequest, @Param("category") String category);
}
