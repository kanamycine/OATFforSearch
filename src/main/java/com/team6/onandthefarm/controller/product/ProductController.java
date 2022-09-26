package com.team6.onandthefarm.controller.product;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team6.onandthefarm.dto.product.ProductDeleteDto;
import com.team6.onandthefarm.dto.product.ProductFormDto;
import com.team6.onandthefarm.dto.product.ProductUpdateFormDto;
import com.team6.onandthefarm.entity.product.Product;
import com.team6.onandthefarm.service.product.ProductService;
import com.team6.onandthefarm.util.BaseResponse;
import com.team6.onandthefarm.vo.product.ProductDeleteRequest;
import com.team6.onandthefarm.vo.product.ProductFormRequest;
import com.team6.onandthefarm.vo.product.ProductUpdateFormRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/product/")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@PostMapping(value = "new")
	//@RequestPart("productImg") List<MultipartFile> productImgs
	public ResponseEntity<BaseResponse<Product>> productForm(@RequestBody ProductFormRequest productFormRequest) throws Exception{

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		ProductFormDto productFormDto = modelMapper.map(productFormRequest, ProductFormDto.class);

		Long productId = productService.saveProduct(productFormDto);

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.CREATED)
				.message("product register completed")
				.data(productId)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.CREATED);
	}

	@PutMapping(value="update")
	public ResponseEntity<BaseResponse<Product>> productUpdateForm(@RequestBody ProductUpdateFormRequest productUpdateFormRequest) throws Exception{
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		ProductUpdateFormDto productUpdateFormDto = modelMapper.map(productUpdateFormRequest, ProductUpdateFormDto.class);

		Long productId = productService.updateProduct(productUpdateFormDto);

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("product update completed")
				.data(productId)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}

	@PutMapping(value="delete")
	public ResponseEntity<BaseResponse<Product>> productDelete(@RequestBody ProductDeleteRequest productDeleteRequest) throws Exception{
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


	@GetMapping(value="list/orderby/highprice")
	public ResponseEntity<BaseResponse<List<Product>>> getProductListByHighPrice(){

		List<Product> productList = productService.getProductsListByHighPrice();

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("getting products by high price completed")
				.data(productList)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}

	@GetMapping(value="list/orderby/lowprice")
	public ResponseEntity<BaseResponse<List<Product>>> getProductListByLowPrice(){

		List<Product> productList = productService.getProductsListByLowPrice();

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("getting products by high price completed")
				.data(productList)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}
	@GetMapping(value="list/orderby/soldcount")
	public ResponseEntity<BaseResponse<List<Product>>> getProductsListBySoldCount() {
		List<Product> productList = productService.getProductsBySoldCount();

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("getting products by high price completed")
				.data(productList)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);

	}

	@GetMapping(value="list/orderby/category/{categoryId}")
	public ResponseEntity<BaseResponse<List<Product>>> getProductsListByCategoryNewest(@PathVariable("categoryId") Long categoryId){

		List<Product> productList = productService.getProductListByCategoryNewest(categoryId);

		BaseResponse baseResponse = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("getting Newest products by category  completed")
				.data(productList)
				.build();

		return new ResponseEntity(baseResponse, HttpStatus.OK);
	}
}
