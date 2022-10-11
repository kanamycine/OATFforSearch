
package com.team6.onandthefarm.repository.product;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.team6.onandthefarm.entity.product.Product;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface ProductPagingRepository extends PagingAndSortingRepository<Product, Long> {

    @Query("select p from Product p join fetch p.category join fetch p.seller")
    List<Product> findAllProductOrderByNewest(PageRequest pageRequest);

    @Query("select p from Product p join fetch p.category join fetch p.seller")
    List<Product> findProductListByHighPrice(PageRequest pageRequest);

    @Query("select p from Product p join fetch p.category join fetch p.seller")
    List<Product> findProductListByLowPrice(PageRequest pageRequest);

    @Query("select p from Product p join fetch p.category join fetch p.seller")
    List<Product> findProductBySoldCount(PageRequest pageRequest);

    @Query("select p from Product p join fetch p.category join fetch p.seller where p.seller.sellerId =:sellerId")
    List<Product> findProductBySellerNewest(PageRequest pageRequest, @Param("sellerId") Long sellerId);

    @Query("select p from Product p join fetch p.category join fetch p.seller where p.category.categoryName =:category")
    List<Product> findProductsByCategoryNewest(PageRequest pageRequest,@Param("category") String category);

    @Query("select p from Product p join fetch p.category join fetch p.seller where p.category.categoryName =:category")
    List<Product> findProductByCategoryHighest(PageRequest pageRequest, @Param("category") String category);

    @Query("select p from Product p join fetch p.category join fetch p.seller where p.category.categoryName =:category")
    List<Product> findProductByCategoryLowest(PageRequest pageRequest, @Param("category") String category);

    @Query("select p from Product p join fetch p.category join fetch p.seller where p.category.categoryName =:category")
    List<Product> findProductByCategorySoldCount(PageRequest pageRequest, @Param("category") String category);
}
