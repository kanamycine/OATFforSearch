package com.team6.onandthefarm.service.product;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.team6.onandthefarm.dto.product.*;
import com.team6.onandthefarm.entity.product.ProductQna;
import com.team6.onandthefarm.entity.product.ProductQnaAnswer;
import com.team6.onandthefarm.vo.product.*;
import org.springframework.web.multipart.MultipartFile;
import com.team6.onandthefarm.entity.product.Product;

public interface ProductService {
	Long saveProduct(ProductFormDto productFormDto) throws IOException;
	
    Long updateProduct(ProductUpdateFormDto productUpdateFormDto) throws IOException;

    Long deleteProduct(ProductDeleteDto productDeleteDto);

    ProductWishResultDto addProductToWishList(ProductWishFormDto productWishFormDto);

    List<Long> cancelProductFromWishList(ProductWishCancelDto productWishCancelDto);

    ProductWishResult getWishList(Long userId, Integer pageNumber);

    ProductSelectionResponseResult getAllProductListOrderByNewest(Long userId, Integer pageNumber);

    ProductSelectionResponseResult getProductsListByHighPrice(Long userId, Integer pageNumber);

    ProductSelectionResponseResult getProductsListByLowPrice(Long userId, Integer pageNumber);

    ProductSelectionResponseResult getMainProductsBySoldCount(Long userId);

    ProductSelectionResponseResult getProductsBySoldCount(Long userId, Integer pageNumber);

    ProductSelectionResponseResult getProductListByCategoryNewest(Long userId, String Category, Integer pageNumber);

    ProductSelectionResponseResult getProductListByCategoryHighest(Long userId, String Category, Integer pageNumber);

    ProductSelectionResponseResult getProductListByCategoryLowest(Long userId, String category, Integer pageNumber);

    ProductSelectionResponseResult getProductsByCategorySoldCount(Long userId, String category, Integer pageNumber);

    ProductSelectionResponseResult getProductListBySellerNewest(Long userId, Long SellerId, Integer pageNumber);

    ProductSelectionResponseResult getSellingProductListBySellerNewest(Long userId, Long sellerId, Integer pageNumber);

    ProductSelectionResponseResult getPauseProductListBySellerNewest(Long userId, Long sellerId, Integer pageNumber);

    ProductQnAInfoResponse findProductQnAList(Long productId, Integer pageNumber);

    ProductReviewResult getProductsWithoutReview(Long userId, Integer pageNumber);
	
    ProductDetailResponse findProductDetail(Long productId, Long userId);
}