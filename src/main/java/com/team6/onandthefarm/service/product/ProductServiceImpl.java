package com.team6.onandthefarm.service.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.team6.onandthefarm.dto.product.ProductWishCancelDto;
import com.team6.onandthefarm.dto.product.ProductWishFormDto;
import com.team6.onandthefarm.entity.product.ProductQna;
import com.team6.onandthefarm.entity.product.ProductQnaAnswer;
import com.team6.onandthefarm.entity.product.Wish;
import com.team6.onandthefarm.entity.seller.Seller;
import com.team6.onandthefarm.entity.user.User;
import com.team6.onandthefarm.repository.product.ProductPagingRepository;
import com.team6.onandthefarm.repository.product.ProductQnaAnswerRepository;
import com.team6.onandthefarm.repository.product.ProductQnaRepository;
import com.team6.onandthefarm.repository.product.ProductWishRepository;
import com.team6.onandthefarm.repository.seller.SellerRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team6.onandthefarm.dto.product.ProductDeleteDto;
import com.team6.onandthefarm.dto.product.ProductFormDto;
import com.team6.onandthefarm.dto.product.ProductUpdateFormDto;
import com.team6.onandthefarm.entity.category.Category;
import com.team6.onandthefarm.entity.product.Product;
import com.team6.onandthefarm.repository.category.CategoryRepository;
import com.team6.onandthefarm.repository.product.ProductRepository;
import com.team6.onandthefarm.repository.user.UserRepository;
import com.team6.onandthefarm.util.DateUtils;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	private ProductRepository productRepository;
	private CategoryRepository categoryRepository;
	private ProductQnaRepository productQnaRepository;
	private ProductQnaAnswerRepository productQnaAnswerRepository;
	private SellerRepository sellerRepository;
	private ProductPagingRepository productPagingRepository;
	private ProductWishRepository productWishRepository;
	private DateUtils dateUtils;
	private Environment env;

	@Autowired
	public ProductServiceImpl(ProductRepository productRepository,
							  CategoryRepository categoryRepository,
							  DateUtils dateUtils,
							  Environment env,
							  ProductQnaRepository productQnaRepository,
							  ProductQnaAnswerRepository productQnaAnswerRepository,
							  SellerRepository sellerRepository,
							  ProductWishRepository productWishRepository,
							  ProductPagingRepository productPagingRepository) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
		this.productPagingRepository = productPagingRepository;
		this.dateUtils = dateUtils;
		this.env = env;
		this.productQnaRepository = productQnaRepository;
		this.productQnaAnswerRepository = productQnaAnswerRepository;
		this.sellerRepository = sellerRepository;
		this.productWishRepository = productWishRepository;
	}

	public Long saveProduct(ProductFormDto productFormDto){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		Product product = modelMapper.map(productFormDto, Product.class);

		Optional<Seller> seller = sellerRepository.findById(productFormDto.getSellerId());

		Long categoryId = productFormDto.getProductCategory();
		Optional<Category> category = categoryRepository.findById(categoryId);

		product.setCategory(category.get());
		product.setProductRegisterDate(dateUtils.transDate(env.getProperty("dateutils.format")));
		product.setSeller(seller.get());
		product.setProductWishCount(0);
		product.setProductSoldCount(0);
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

	public Long addProductToWishList(ProductWishFormDto productWishFormDto){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Wish wish = modelMapper.map(productWishFormDto, Wish.class);

		/*
		추후 유저 추가 되면 추가
		Optional<User> user = userRepository.findById(productWishFormDto.getUserId());
		wish.setUser(user.get());
		* */

		Optional<Product> product = productRepository.findById(productWishFormDto.getProductId());
		wish.setProduct(product.get());
		product.get().setProductWishCount(product.get().getProductWishCount() + 1);
		Long wishId = productWishRepository.save(wish).getWishId();

		return wishId;
	}

	public Long cancelProductFromWishList(ProductWishCancelDto productWishCancelDto){
		Long wishId = productWishCancelDto.getWishId();
		Wish wish = productWishRepository.findById(wishId).get();
		productWishRepository.delete(wish);
		Product product = productRepository.findById(productWishCancelDto.getProductId()).get();
		product.setProductWishCount(product.getProductWishCount() - 1);
		return wishId;
	}

	public List<Product> getAllProductListOrderByNewest(Integer pageNumber){
		PageRequest pageRequest = PageRequest.of(pageNumber, 8, Sort.by("productRegisterDate").descending());
		return productPagingRepository.findAllProductOrderByNewest(pageRequest);
	}

	public List<Product> getProductsListByHighPrice(Integer pageNumber) {
		PageRequest pageRequest = PageRequest.of(pageNumber,8, Sort.by("productPrice").descending());
		return productPagingRepository.findProductListByHighPrice(pageRequest);
	}

	public List<Product> getProductsListByLowPrice(Integer pageNumber) {
		PageRequest pageRequest = PageRequest.of(pageNumber,8, Sort.by("productPrice").ascending());
		return productPagingRepository.findProductListByLowPrice(pageRequest);
	}

	@Override
	public List<Product> getProductsBySoldCount(Integer pageNumber) {
		PageRequest pageRequest = PageRequest.of(pageNumber,8, Sort.by("productSoldCount").descending());
		return productPagingRepository.findProductBySoldCount(pageRequest);
	}

	public List<Product> getProductListBySellerNewest(Long sellerId, Integer pageNumber){
		PageRequest pageRequest = PageRequest.of(pageNumber, 8, Sort.by("productRegisterDate").descending());
		return productPagingRepository.findProductBySellerNewest(pageRequest, sellerId);
	}

	@Override
	public List<Product> getProductListByCategoryNewest(Long categoryId, Integer pageNumber) {
		PageRequest pageRequest = PageRequest.of(pageNumber,8, Sort.by("productRegisterDate").descending());
		return productPagingRepository.findProductsByCategoryNewest(pageRequest,categoryId);
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
