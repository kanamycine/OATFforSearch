package com.team6.onandthefarm.service.product;

import java.util.List;


import org.springframework.web.multipart.MultipartFile;

import com.team6.onandthefarm.dto.product.ProductFormDto;
import com.team6.onandthefarm.dto.product.ProductUpdateFormDto;
import com.team6.onandthefarm.entity.product.Product;

public interface ProductService {

	Long saveProduct(ProductFormDto productFormDto);
	Long updateProduct(ProductUpdateFormDto productUpdateFormDto);
}
