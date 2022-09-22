package com.team6.onandthefarm.service.product;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team6.onandthefarm.dto.product.ProductFormDto;
import com.team6.onandthefarm.dto.product.ProductUpdateFormDto;
import com.team6.onandthefarm.entity.category.Category;
import com.team6.onandthefarm.entity.product.Product;
import com.team6.onandthefarm.repository.product.ProductRepository;
import com.team6.onandthefarm.util.DateUtils;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	private ProductRepository productRepository;
	private DateUtils dateUtils;
	private Environment env;

	@Autowired
	public ProductServiceImpl(ProductRepository productRepository, DateUtils dateUtils, Environment env) {
		this.productRepository = productRepository;
		this.dateUtils = dateUtils;
		this.env = env;
	}

	public Long saveProduct(ProductFormDto productFormDto){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Product product = modelMapper.map(productFormDto, Product.class);

		return productRepository.save(product).getProductId();
	}

	public Long updateProduct(ProductUpdateFormDto productUpdateFormDto){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		Optional<Product> product = productRepository.findById(productUpdateFormDto.getProductId());

		// 나중에 productUpdateFormDto의 ID로 findbyId 해서 가져온 카테고리로 대치!!!!!!
		Category tmpCategory = new Category(1l, "strawberry");
		product.get().setProductName("dskjdsfjj");
		long productId = product.get().updateProduct(tmpCategory,
				productUpdateFormDto.getProductName(),
				productUpdateFormDto.getProductPrice(),
				productUpdateFormDto.getProductTotalStock(),
				productUpdateFormDto.getProductMainImgSrc(),
				productUpdateFormDto.getProductDetail(),
				productUpdateFormDto.getProductOriginPlace(),
				productUpdateFormDto.getProductDetailShort(),
				productUpdateFormDto.getProductDeliveryCompany(),
				productUpdateFormDto.getProductStatus(),
				productUpdateFormDto.getProductWishCount());
		productRepository.save(product.get());

		return productId;
	}
}
