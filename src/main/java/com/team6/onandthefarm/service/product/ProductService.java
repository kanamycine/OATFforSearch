package com.team6.onandthefarm.service.product;

import java.util.List;
import java.util.Map;

import com.team6.onandthefarm.dto.product.ProductWishCancelDto;
import com.team6.onandthefarm.dto.product.ProductWishFormDto;
import com.team6.onandthefarm.entity.product.ProductQna;
import com.team6.onandthefarm.entity.product.ProductQnaAnswer;
import org.springframework.web.multipart.MultipartFile;
import com.team6.onandthefarm.dto.product.ProductDeleteDto;
import com.team6.onandthefarm.dto.product.ProductFormDto;
import com.team6.onandthefarm.dto.product.ProductUpdateFormDto;
import com.team6.onandthefarm.entity.product.Product;

public interface ProductService {
	Long saveProduct(ProductFormDto productFormDto);
	Long updateProduct(ProductUpdateFormDto productUpdateFormDto);
	Long deleteProduct(ProductDeleteDto productDeleteDto);
	Long addProductToWishList(ProductWishFormDto productWishFormDto);
	Long cancelProductFromWishList(ProductWishCancelDto productWishCancelDto);
	List<Product> getAllProductListOrderByNewest(Integer pageNumber);
	List<Product> getProductsListByHighPrice(Integer pageNumber);
	List<Product> getProductsListByLowPrice(Integer pageNumber);
	List<Product> getProductsBySoldCount(Integer pageNumber);
	List<Product> getProductListByCategoryNewest(Long CategoryId, Integer pageNumber);
	List<Product> getProductListBySellerNewest(Long SellerId, Integer pageNumber);
	Map<ProductQna, ProductQnaAnswer> findProductQnAList(Long productId);
}