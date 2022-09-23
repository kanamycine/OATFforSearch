package com.team6.onandthefarm.controller.product;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team6.onandthefarm.dto.product.ProductDeleteDto;
import com.team6.onandthefarm.dto.product.ProductFormDto;
import com.team6.onandthefarm.dto.product.ProductUpdateFormDto;
import com.team6.onandthefarm.service.product.ProductService;
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
	public ResponseEntity productForm(@RequestBody ProductFormRequest productFormRequest) throws Exception{

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		ProductFormDto productFormDto = modelMapper.map(productFormRequest, ProductFormDto.class);

		Long productId = productService.saveProduct(productFormDto);

		return new ResponseEntity(HttpStatus.CREATED);
	}

	@PutMapping(value="update")
	public ResponseEntity productUpdateForm(@RequestBody ProductUpdateFormRequest productUpdateFormRequest) throws Exception{
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		ProductUpdateFormDto productUpdateFormDto = modelMapper.map(productUpdateFormRequest, ProductUpdateFormDto.class);

		Long productId = productService.updateProduct(productUpdateFormDto);

		return new ResponseEntity(HttpStatus.OK);
	}

	@PutMapping(value="delete")
	public ResponseEntity productDelete(@RequestBody ProductDeleteRequest productDeleteRequest) throws Exception{
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		ProductDeleteDto productDeleteDto = modelMapper.map(productDeleteRequest, ProductDeleteDto.class);

		Long productId = productService.deleteProduct(productDeleteDto);
		return new ResponseEntity(HttpStatus.OK);
	}
}
