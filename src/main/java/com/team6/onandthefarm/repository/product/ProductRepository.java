package com.team6.onandthefarm.repository.product;

import java.util.List;

import com.team6.onandthefarm.entity.order.Orders;
import com.team6.onandthefarm.entity.seller.Seller;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.team6.onandthefarm.entity.product.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {
	// List<Product> findAllByCategory(Long categoryId);

	@Query("select p from Product p join fetch p.category join fetch p.seller order by p.productPrice DESC")
	List<Product> findProductListByHighPrice();

	@Query("select p from Product p join fetch p.category join fetch p.seller order by p.productPrice ASC ")
	List<Product> findProductListByLowPrice();

	@Query("select p from Product p join fetch p.category join fetch p.seller order by p.productSoldCount DESC")
	List<Product> findProductBySoldCount();

	@Query("select p from Product p join fetch p.category join fetch p.seller where p.category.categoryId =:categoryId order by p.productRegisterDate DESC")
	List<Product> findProductsByCategoryNewest(@Param("categoryId") Long categoryId);

	List<Product> findBySeller(Seller seller);

	List<Product> findBySellerOrderByProductWishCountDesc(Seller seller);

	@Query("select p from Product p where p.seller.sellerId=:sellerId and p.productStatus<>'selling'")
	List<Product> findNotSellingProduct(@Param("sellerId") Long sellerId);

	@Query("select p from Product p where p.seller.sellerId=:sellerId and p.productStatus='selling'")
	List<Product> findSellingProduct(@Param("sellerId") Long sellerId);
}

