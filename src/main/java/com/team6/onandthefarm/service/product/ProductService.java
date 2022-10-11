package com.team6.onandthefarm.service.product;

import java.util.List;
import java.util.Map;

import com.team6.onandthefarm.dto.product.*;
import com.team6.onandthefarm.entity.product.ProductQna;
import com.team6.onandthefarm.entity.product.ProductQnaAnswer;
import com.team6.onandthefarm.vo.product.*;
import org.springframework.web.multipart.MultipartFile;
import com.team6.onandthefarm.entity.product.Product;

public interface ProductService {
	Long saveProduct(ProductFormDto productFormDto);
	
  Long updateProduct(ProductUpdateFormDto productUpdateFormDto);
	
  Long deleteProduct(ProductDeleteDto productDeleteDto);
	
  ProductWishResultDto addProductToWishList(ProductWishFormDto productWishFormDto);
	
  List<Long> cancelProductFromWishList(ProductWishCancelDto productWishCancelDto);
	
  List<ProductWishResponse> getWishList(Long userId);
	
  List<ProductSelectionResponse> getAllProductListOrderByNewest(Integer pageNumber);
	
  List<ProductSelectionResponse> getProductsListByHighPrice(Integer pageNumber);
	
  List<ProductSelectionResponse> getProductsListByLowPrice(Integer pageNumber);
	
  List<ProductSelectionResponse> getProductsBySoldCount(Integer pageNumber);
	
  List<ProductSelectionResponse> getProductListByCategoryNewest(Long CategoryId, Integer pageNumber);
	
  List<ProductSelectionResponse> getProductListBySellerNewest(Long SellerId, Integer pageNumber);
	
  List<ProductQnAResponse> findProductQnAList(Long productId);

	List<ProductReviewResponse> getProductsWithoutReview(Long userId);
	
  ProductDetailResponse findProductDetail(Long productId, Long userId);
}