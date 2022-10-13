package com.team6.onandthefarm.controller.product;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import com.team6.onandthefarm.dto.product.ProductWishCancelDto;
import com.team6.onandthefarm.dto.product.ProductWishFormDto;
import com.team6.onandthefarm.entity.product.ProductQna;
import com.team6.onandthefarm.entity.product.ProductQnaAnswer;

import io.swagger.annotations.ApiOperation;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.team6.onandthefarm.dto.product.ProductDeleteDto;
import com.team6.onandthefarm.dto.product.ProductFormDto;
import com.team6.onandthefarm.dto.product.ProductUpdateFormDto;
import com.team6.onandthefarm.entity.product.Product;
import com.team6.onandthefarm.service.product.ProductService;
import com.team6.onandthefarm.util.BaseResponse;
import com.team6.onandthefarm.vo.product.ProductDeleteRequest;
import com.team6.onandthefarm.vo.product.ProductFormRequest;
import com.team6.onandthefarm.vo.product.ProductSelectionResponse;
import com.team6.onandthefarm.vo.product.ProductUpdateFormRequest;
import com.team6.onandthefarm.vo.product.ProductWishCancelRequest;
import com.team6.onandthefarm.vo.product.ProductWishFormRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/seller/product")
@RequiredArgsConstructor
public class SellerProductController {

	private final ProductService productService;

	@PostMapping(value = "/new")
	@ApiOperation(value = "상품 등록")
	//@RequestPart("productImg") List<MultipartFile> productImgs
	public ResponseEntity<BaseResponse<Product>> productForm(
			@ApiIgnore Principal principal,
			@RequestPart(value = "images", required = false) List<MultipartFile> photo
			//@RequestPart(value = "data", required = false) ProductFormRequest productFormRequest
			) throws Exception {

		String[] principalInfo = principal.getName().split(" ");
		Long sellerId = Long.parseLong(principalInfo[0]);

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		ProductFormDto productFormDto = new ProductFormDto();//modelMapper.map(productFormRequest, ProductFormDto.class);
		productFormDto.setSellerId(sellerId);
		productFormDto.setImages(photo);

		Long productId = productService.saveProduct(productFormDto);

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.CREATED)
				.message("product register completed")
				.data(productId)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.CREATED);
	}

	@PutMapping(value = "/update")
	@ApiOperation(value = "상품 수정")
	public ResponseEntity<BaseResponse<Product>> productUpdateForm(
			@RequestBody ProductUpdateFormRequest productUpdateFormRequest) throws Exception {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		ProductUpdateFormDto productUpdateFormDto = modelMapper.map(productUpdateFormRequest,
				ProductUpdateFormDto.class);

		Long productId = productService.updateProduct(productUpdateFormDto);

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("product update completed")
				.data(productId)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}

	@PutMapping(value = "/delete")
	@ApiOperation(value = "상품 삭제")
	public ResponseEntity<BaseResponse<Product>> productDelete(
			@RequestBody ProductDeleteRequest productDeleteRequest) throws Exception {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		ProductDeleteDto productDeleteDto = modelMapper.map(productDeleteRequest, ProductDeleteDto.class);

		Long productId = productService.deleteProduct(productDeleteDto);

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("product delete completed")
				.data(productId)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}


	@GetMapping(value = "/list/all/newest/{page-no}")
	@ApiOperation(value = "모든 상품 최신순 조회")
	public ResponseEntity<BaseResponse<List<ProductSelectionResponse>>> getAllProductListOrderByNewest(
			@PathVariable("page-no") String pageNumber) {
		Long userId = null;
		List<ProductSelectionResponse> products = productService.getAllProductListOrderByNewest(userId, Integer.valueOf(pageNumber));

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
			@PathVariable("page-no") String pageNumber) {

		Long userId = null;
		List<ProductSelectionResponse> products = productService.getProductsListByHighPrice(userId, Integer.valueOf(pageNumber));

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
			@PathVariable("page-no") String pageNumber) {

		Long userId = null;
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
			@PathVariable("page-no") String pageNumber) {

		Long userId = null;
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
			@PathVariable("sellerId") Long sellerId, @PathVariable("page-no") String pageNumber) {

		Long userId = null;
		List<ProductSelectionResponse> products = productService.getProductListBySellerNewest(userId, sellerId, Integer.valueOf(pageNumber));

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("getting Newest products by farmer completed")
				.data(products)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}

	@GetMapping(value = "/list/orderby/{category}/{page-no}")
	@ApiOperation(value = "상품 카테고리별 최신순 조회")
	public ResponseEntity<BaseResponse<List<ProductSelectionResponse>>> getProductsListByCategoryNewest(
			@PathVariable("category") String category, @PathVariable("page-no") String pageNumber) {

		Long userId = null;
		List<ProductSelectionResponse> products = productService.getProductListByCategoryNewest(userId, category, Integer.valueOf(pageNumber));

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("getting Newest products by category  completed")
				.data(products)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}

	@GetMapping(value = "/list/orderby/seller/{page-no}")
	@ApiOperation(value = "농부별 자신이 등록한 상품 최신순 조회")
	public ResponseEntity<BaseResponse<List<ProductSelectionResponse>>> getNewestProductList(@ApiIgnore Principal principal, @PathVariable("page-no") String pageNumber) {

		String[] principalInfo = principal.getName().split(" ");
		Long sellerId = Long.parseLong(principalInfo[0]);
		Long userId = null;

		List<ProductSelectionResponse> products = productService.getProductListBySellerNewest(userId, sellerId, Integer.valueOf(pageNumber));

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("getting Newest products by farmer completed")
				.data(products)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}

	//셀러 기준 질의 조회 구현 요망!

}