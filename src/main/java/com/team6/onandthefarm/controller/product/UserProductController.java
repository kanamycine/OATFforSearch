package com.team6.onandthefarm.controller.product;

import com.team6.onandthefarm.dto.product.*;
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

@RestController
@RequestMapping("/api/user/product")
@RequiredArgsConstructor
public class UserProductController {

	private final ProductService productService;

	@PostMapping(value = "/wish/add")
	@ApiOperation("위시리스트 추가")
	public ResponseEntity<BaseResponse> addProductToWishList(@ApiIgnore Principal principal,
			@RequestBody ProductWishFormRequest productWishFormRequest) throws Exception {

		if(principal == null){
			BaseResponse baseResponse = BaseResponse.builder()
					.httpStatus(HttpStatus.FORBIDDEN)
					.message("no authorization")
					.build();
			return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
		}

		String[] principalInfo = principal.getName().split(" ");
		Long userId = Long.parseLong(principalInfo[0]);

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		ProductWishFormDto productWishFormDto = modelMapper.map(productWishFormRequest, ProductWishFormDto.class);
		productWishFormDto.setUserId(userId);
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

		if(principal == null){
			BaseResponse baseResponse = BaseResponse.builder()
					.httpStatus(HttpStatus.FORBIDDEN)
					.message("no authorization")
					.build();
			return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
		}

		String[] principalInfo = principal.getName().split(" ");
		Long userId = Long.parseLong(principalInfo[0]);

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		ProductWishCancelDto productWishCancelDto = modelMapper.map(productWishCancelRequest,
				ProductWishCancelDto.class);
		productWishCancelDto.setUserId(userId);
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
	public ResponseEntity<ProductDetailResponse> findProductDetail(@ApiIgnore Principal principal, @PathVariable("product-id") Long productId) {

		Long userId = null;
		if (principal != null){
			String[] principalInfo = principal.getName().split(" ");
			userId = Long.parseLong(principalInfo[0]);
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
	public ResponseEntity<BaseResponse<ProductSelectionResponseResult>> getAllProductListOrderByNewest(
			@ApiIgnore Principal principal,
			@PathVariable("page-no") String pageNumber) {

		Long userId = null;
		if (principal != null){
			String[] principalInfo = principal.getName().split(" ");
			userId = Long.parseLong(principalInfo[0]);
		}
		ProductSelectionResponseResult products = productService.getAllProductListOrderByNewest(userId,
				Integer.valueOf(pageNumber));

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("getting All products by Newest")
				.data(products)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}

	@GetMapping(value = "/list/all/highprice/{page-no}")
	@ApiOperation(value = "상품 높은 가격 순 조회")
	public ResponseEntity<BaseResponse<ProductSelectionResponseResult>> getProductListByHighPrice(
			@ApiIgnore Principal principal,
			@PathVariable("page-no") String pageNumber) {

		Long userId = null;
		if (principal != null){
			String[] principalInfo = principal.getName().split(" ");
			userId = Long.parseLong(principalInfo[0]);
		}
		ProductSelectionResponseResult products = productService.getProductsListByHighPrice(userId,
				Integer.valueOf(pageNumber));

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("getting products by high price completed")
				.data(products)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}

	@GetMapping(value = "/list/all/lowprice/{page-no}")
	@ApiOperation(value = "상품 낮은 가격 순 조회")
	public ResponseEntity<BaseResponse<ProductSelectionResponseResult>> getProductListByLowPrice(
			@ApiIgnore Principal principal,
			@PathVariable("page-no") String pageNumber) {

		Long userId = null;
		if (principal != null){
			String[] principalInfo = principal.getName().split(" ");
			userId = Long.parseLong(principalInfo[0]);
		}
		ProductSelectionResponseResult products = productService.getProductsListByLowPrice(userId, Integer.valueOf(pageNumber));

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("getting products by high price completed")
				.data(products)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}

	@GetMapping(value = "/list/main")
	@ApiOperation(value = "상품 메인 화면 조회(판매순 10개)")
	public ResponseEntity<BaseResponse<ProductSelectionResponseResult>> getMainProductsListBySoldCount(
			@ApiIgnore Principal principal){
		Long userId = null;
		if(principal != null){
			String [] principalInfo = principal.getName().split(" ");
			userId = Long.parseLong(principalInfo[0]);
		}
		ProductSelectionResponseResult products = productService.getMainProductsBySoldCount(userId);
		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("getting main view products by sold count")
				.data(products)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}

	@GetMapping(value = "/list/all/soldcount/{page-no}")
	@ApiOperation(value = "상품 높은 판매순 조회")
	public ResponseEntity<BaseResponse<ProductSelectionResponseResult>> getProductsListBySoldCount(
			@ApiIgnore Principal principal,
			@PathVariable("page-no") String pageNumber) {

		Long userId = null;
		if (principal != null){
			String[] principalInfo = principal.getName().split(" ");
			userId = Long.parseLong(principalInfo[0]);
		}
		ProductSelectionResponseResult products = productService.getProductsBySoldCount(userId, Integer.valueOf(pageNumber));

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("getting products by sold count completed")
				.data(products)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}

	@GetMapping(value = "/list/orderby/seller/{sellerId}/{page-no}")
	@ApiOperation(value = "상품 농부별 최신순 조회")
	public ResponseEntity<BaseResponse<ProductSelectionResponseResult>> getProductsListBySellerNewest(
			@ApiIgnore Principal principal,
			@PathVariable("sellerId") Long sellerId, @PathVariable("page-no") String pageNumber) {

		Long userId = null;
		if (principal != null){
			String[] principalInfo = principal.getName().split(" ");
			userId = Long.parseLong(principalInfo[0]);
		}
		ProductSelectionResponseResult products = productService.getProductListBySellerNewest(userId, sellerId,
				Integer.valueOf(pageNumber));

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("getting Newest products by farmer completed")
				.data(products)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}

	@GetMapping(value = "/list/{category}/newest/{page-no}")
	@ApiOperation(value = "상품 카테고리별 최신순 조회")
	public ResponseEntity<BaseResponse<ProductSelectionResponseResult>> getProductsListByCategoryNewest(
			@ApiIgnore Principal principal,
			@PathVariable("category") String category, @PathVariable("page-no") String pageNumber) {

		Long userId = null;
		if (principal != null){
			String[] principalInfo = principal.getName().split(" ");
			userId = Long.parseLong(principalInfo[0]);
		}
		ProductSelectionResponseResult products = productService.getProductListByCategoryNewest(userId, category,
				Integer.valueOf(pageNumber));

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("getting Newest products by category  completed")
				.data(products)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}

	@GetMapping(value = "/list/{category}/highprice/{page-no}")
	@ApiOperation(value = "상품 카테고리별 높은가격 순 조회")
	public ResponseEntity<BaseResponse<ProductSelectionResponseResult>> getProductListByCategoryHighest(
			@ApiIgnore Principal principal,
			@PathVariable("category") String category, @PathVariable("page-no") String pageNumber){

		Long userId = null;
		if (principal != null){
			String[] principalInfo = principal.getName().split(" ");
			userId = Long.parseLong(principalInfo[0]);
		}
		ProductSelectionResponseResult products = productService.getProductListByCategoryHighest(userId, category, Integer.valueOf(pageNumber));

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("getting highest products by category  completed")
				.data(products)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}

	@GetMapping(value = "/list/{category}/lowprice/{page-no}")
	@ApiOperation(value = "상품 카테고리별 낮은가격 순 조회")
	public ResponseEntity<BaseResponse<ProductSelectionResponseResult>> getProductListByCategoryLowest(
			@ApiIgnore Principal principal,
			@PathVariable("category") String category, @PathVariable("page-no") String pageNumber){

		Long userId = null;
		if (principal != null){
			String[] principalInfo = principal.getName().split(" ");
			userId = Long.parseLong(principalInfo[0]);
		}
		ProductSelectionResponseResult products = productService.getProductListByCategoryLowest(userId, category, Integer.valueOf(pageNumber));
		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("getting lowest products by category completed")
				.data(products)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}

	@GetMapping(value = "/list/{category}/soldcount/{page-no}")
	@ApiOperation(value = "상품 카테고리별 판매 순 조회")
	public ResponseEntity<BaseResponse<ProductSelectionResponseResult>> getProductsListByCategorySoldCount(
			@ApiIgnore Principal principal,
			@PathVariable("category") String category, @PathVariable("page-no") String pageNumber) {

		Long userId = null;
		if (principal != null){
			String[] principalInfo = principal.getName().split(" ");
			userId = Long.parseLong(principalInfo[0]);
		}
		ProductSelectionResponseResult products = productService.getProductsByCategorySoldCount(userId, category, Integer.valueOf(pageNumber));

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("getting products by high price completed")
				.data(products)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}

	@GetMapping("/QnA/{product-no}")
	@ApiOperation(value = "상품에 대한 질의 조회")
	public ResponseEntity<BaseResponse<ProductQnAResponseResult>> findProductQnAList(
			@PathVariable("product-no") Long productId, @RequestParam Integer pageNumber) {

        ProductQnAResponseResult qnAList
				= productService.findProductQnAList(productId,pageNumber);

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("OK")
				.data(qnAList)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}
}