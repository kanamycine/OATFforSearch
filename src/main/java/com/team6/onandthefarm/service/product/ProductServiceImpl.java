package com.team6.onandthefarm.service.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.team6.onandthefarm.entity.product.ProductQna;
import com.team6.onandthefarm.entity.product.ProductQnaAnswer;
import com.team6.onandthefarm.repository.product.ProductQnaAnswerRepository;
import com.team6.onandthefarm.repository.product.ProductQnaRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team6.onandthefarm.dto.product.ProductDeleteDto;
import com.team6.onandthefarm.dto.product.ProductFormDto;
import com.team6.onandthefarm.dto.product.ProductUpdateFormDto;
import com.team6.onandthefarm.entity.category.Category;
import com.team6.onandthefarm.entity.product.Product;
import com.team6.onandthefarm.repository.category.CategoryRepository;
import com.team6.onandthefarm.repository.product.ProductRepository;
import com.team6.onandthefarm.util.DateUtils;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	private ProductRepository productRepository;
	private CategoryRepository categoryRepository;
	private ProductQnaRepository productQnaRepository;
	private ProductQnaAnswerRepository productQnaAnswerRepository;
	private DateUtils dateUtils;
	private Environment env;

	@Autowired
	public ProductServiceImpl(ProductRepository productRepository,
							  CategoryRepository categoryRepository,
							  DateUtils dateUtils,
							  Environment env,
							  ProductQnaRepository productQnaRepository,
							  ProductQnaAnswerRepository productQnaAnswerRepository) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
		this.dateUtils = dateUtils;
		this.env = env;
		this.productQnaRepository=productQnaRepository;
		this.productQnaAnswerRepository=productQnaAnswerRepository;
	}

	public Long saveProduct(ProductFormDto productFormDto){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		Product product = modelMapper.map(productFormDto, Product.class);

		Long categoryId = productFormDto.getProductCategory();
		Optional<Category> category = categoryRepository.findById(categoryId);

		product.setCategory(category.get());
		product.setProductRegisterDate(dateUtils.transDate(env.getProperty("dateutils.format")));

		return productRepository.save(product).getProductId();
	}

	public Long updateProduct(ProductUpdateFormDto productUpdateFormDto){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		Optional<Product> product = productRepository.findById(productUpdateFormDto.getProductId());

		// 나중에 productUpdateFormDto의 ID로 findbyId 해서 가져온 카테고리로 대치!!!!!!

		//test 로직
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
				productUpdateFormDto.getProductWishCount(),
				productUpdateFormDto.getProductSoldCount());
		product.get().setProductUpdateDate(dateUtils.transDate(env.getProperty("dateutils.format")));
		productRepository.save(product.get());

		return productId;
	}

	public Long deleteProduct(ProductDeleteDto productDeleteDto){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		Optional<Product> product = productRepository.findById(productDeleteDto.getProductId());
		product.get().setProductStatus("deleted");
		product.get().setProductUpdateDate(dateUtils.transDate(env.getProperty("dateutils.format")));

		return product.get().getProductId();
	}

	public List<Product> getProductsListByHighPrice() {
		List<Product> products = productRepository.findProductListByHighPrice();
		return products;
	}
	public List<Product> getProductsListByLowPrice() {
		List<Product> products = productRepository.findProductListByLowPrice();
		return products;
	}

	@Override
	public List<Product> getProductsBySoldCount() {
		List<Product> products = productRepository.findProductBySoldCount();
		return products;
	}

	@Override
	public List<Product> getProductListByCategoryNewest(Long categoryId) {
		List<Product> products = productRepository.findProductsByCategoryNewest(categoryId);
		return products;
	}

	public Map<ProductQna, ProductQnaAnswer> findProductQnAList(Long productId){
		Optional<Product> product = productRepository.findById(productId);
		List<ProductQna> productQnas = productQnaRepository.findByProduct(product.get());
		// QNA : QNA답변
		Map<ProductQna, ProductQnaAnswer> matching = new HashMap<>();
		for(ProductQna productQna : productQnas){
			if(productQna.getProductQnaStatus()=="qna0"||productQna.getProductQnaStatus()=="qna2"){
				matching.put(productQna,null);
			}
			matching.put(productQna,productQnaAnswerRepository.findByProductQna(productQna));
		}

		return matching;
	}
}
