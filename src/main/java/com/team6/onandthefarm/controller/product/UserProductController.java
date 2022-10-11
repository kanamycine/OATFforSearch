package com.team6.onandthefarm.controller.product;

import com.team6.onandthefarm.dto.product.*;
import com.team6.onandthefarm.entity.product.ProductQna;
import com.team6.onandthefarm.entity.product.ProductQnaAnswer;
import com.team6.onandthefarm.service.product.ProductService;
import com.team6.onandthefarm.util.BaseResponse;
import com.team6.onandthefarm.vo.product.*;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user/product")
@RequiredArgsConstructor
public class UserProductController {

	private final ProductService productService;

	@PostMapping(value = "/wish/add")
	@ApiOperation("위시리스트 추가")
	public ResponseEntity<BaseResponse> addProductToWishList(@ApiIgnore Principal principal,
			@RequestBody ProductWishFormRequest productWishFormRequest) throws Exception {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		ProductWishFormDto productWishFormDto = modelMapper.map(productWishFormRequest, ProductWishFormDto.class);
		productWishFormDto.setUserId(Long.parseLong(principal.getName()));
		ProductWishResultDto resultDto = productService.addProductToWishList(productWishFormDto);

		BaseResponse baseResponse = null;
		if(resultDto.getIsCreated()) {
			baseResponse = BaseResponse.builder()
					.httpStatus(HttpStatus.CREATED)
					.message("add Product to wish-list completed")
					.data(resultDto.getWishId())
					.build();
		}
		else {
			baseResponse = BaseResponse.builder()
					.httpStatus(HttpStatus.OK)
					.message("Wish is already added")
					.data(resultDto.getWishId())
					.build();
		}

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}

	@PutMapping(value = "/wish/delete")
	@ApiOperation("위시리스트 삭제")
	public ResponseEntity<BaseResponse> deleteProductToWishList(@ApiIgnore Principal principal,
			@RequestBody ProductWishCancelRequest productWishCancelRequest) throws Exception {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		ProductWishCancelDto productWishCancelDto = modelMapper.map(productWishCancelRequest,
				ProductWishCancelDto.class);
		productWishCancelDto.setUserId(Long.parseLong(principal.getName()));
		List<Long> wishId = productService.cancelProductFromWishList(productWishCancelDto);

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.CREATED)
				.message("delete Product to wish-list completed")
				.data(wishId)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}

	@GetMapping(value = "/{product-id}")
	@ApiOperation(value = "상품 단건 조회")
	public ResponseEntity<ProductInfoResponse> findProductDetail(@ApiIgnore Principal principal, @PathVariable("product-id") Long productId) {
		Long userId = null;
		if (principal != null){
			userId = Long.parseLong(principal.getName());
		}
		ProductDetailResponse product = productService.findProductDetail(productId, userId);

		BaseResponse baseReponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("getting one products by product-id")
				.data(product)
				.build();

		return new ResponseEntity(baseReponse, HttpStatus.OK);
	}

	@GetMapping(value = "/list/all/newest/{page-no}")
	@ApiOperation(value = "모든 상품 최신순 조회")
	public ResponseEntity<BaseResponse<List<ProductSelectionResponse>>> getAllProductListOrderByNewest(
			@ApiIgnore Principal principal,
			@PathVariable("page-no") String pageNumber) {

		Long userId = null;
		if (principal != null){
			userId = Long.parseLong(principal.getName());
		}
		List<ProductSelectionResponse> products = productService.getAllProductListOrderByNewest(userId,
				Integer.valueOf(pageNumber));

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("getting All products by Newest")
				.data(products)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}

	@GetMapping(value = "/list/orderby/highprice/{page-no}")
	@ApiOperation(value = "상품 높은 가격 순 조회")
	public ResponseEntity<BaseResponse<List<ProductSelectionResponse>>> getProductListByHighPrice(
			@ApiIgnore Principal principal,
			@PathVariable("page-no") String pageNumber) {

		Long userId = null;
		if (principal != null){
			userId = Long.parseLong(principal.getName());
		}
		List<ProductSelectionResponse> products = productService.getProductsListByHighPrice(userId,
				Integer.valueOf(pageNumber));

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("getting products by high price completed")
				.data(products)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}

	@GetMapping(value = "/list/orderby/lowprice/{page-no}")
	@ApiOperation(value = "상품 낮은 가격 순 조회")
	public ResponseEntity<BaseResponse<List<ProductSelectionResponse>>> getProductListByLowPrice(
			@ApiIgnore Principal principal,
			@PathVariable("page-no") String pageNumber) {

		Long userId = null;
		if (principal != null){
			userId = Long.parseLong(principal.getName());
		}
		List<ProductSelectionResponse> products = productService.getProductsListByLowPrice(userId, Integer.valueOf(pageNumber));

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("getting products by high price completed")
				.data(products)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}

	@GetMapping(value = "/list/orderby/soldcount/{page-no}")
	@ApiOperation(value = "상품 높은 판매순 조회")
	public ResponseEntity<BaseResponse<List<ProductSelectionResponse>>> getProductsListBySoldCount(
			@ApiIgnore Principal principal,
			@PathVariable("page-no") String pageNumber) {

		Long userId = null;
		if (principal != null){
			userId = Long.parseLong(principal.getName());
		}
		List<ProductSelectionResponse> products = productService.getProductsBySoldCount(userId, Integer.valueOf(pageNumber));

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("getting products by high price completed")
				.data(products)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}

	@GetMapping(value = "/list/orderby/seller/{sellerId}/{page-no}")
	@ApiOperation(value = "상품 농부별 최신순 조회")
	public ResponseEntity<BaseResponse<List<ProductSelectionResponse>>> getProductsListBySellerNewest(
			@ApiIgnore Principal principal,
			@PathVariable("sellerId") Long sellerId, @PathVariable("page-no") String pageNumber) {

		Long userId = null;
		if (principal != null){
			userId = Long.parseLong(principal.getName());
		}
		List<ProductSelectionResponse> products = productService.getProductListBySellerNewest(userId, sellerId,
				Integer.valueOf(pageNumber));

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("getting Newest products by farmer completed")
				.data(products)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}

	@GetMapping(value = "/list/orderby/category/{categoryId}/{page-no}")
	@ApiOperation(value = "상품 카테고리별 최신순 조회")
	public ResponseEntity<BaseResponse<List<ProductSelectionResponse>>> getProductsListByCategoryNewest(
			@ApiIgnore Principal principal,
			@PathVariable("categoryId") Long categoryId, @PathVariable("page-no") String pageNumber) {

		Long userId = null;
		if (principal != null){
			userId = Long.parseLong(principal.getName());
		}
		List<ProductSelectionResponse> products = productService.getProductListByCategoryNewest(userId, categoryId,
				Integer.valueOf(pageNumber));

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("getting Newest products by category  completed")
				.data(products)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}

	@GetMapping("/QnA/{product-no}")
	@ApiOperation(value = "상품에 대한 질의 조회")
	public ResponseEntity<BaseResponse<Map<ProductQna, ProductQnaAnswer>>> findProductQnAList(
			@PathVariable("product-no") Long productId) {

		List<ProductQnAResponse> products
				= productService.findProductQnAList(productId);

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("OK")
				.data(products)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}
}