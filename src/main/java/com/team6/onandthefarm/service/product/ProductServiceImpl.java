package com.team6.onandthefarm.service.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.team6.onandthefarm.dto.product.ProductWishCancelDto;
import com.team6.onandthefarm.dto.product.ProductWishFormDto;
import com.team6.onandthefarm.entity.product.ProductQna;
import com.team6.onandthefarm.entity.product.ProductQnaAnswer;
import com.team6.onandthefarm.entity.product.Wish;
import com.team6.onandthefarm.entity.seller.Seller;
import com.team6.onandthefarm.repository.product.ProductPagingRepository;
import com.team6.onandthefarm.repository.product.ProductQnaAnswerRepository;
import com.team6.onandthefarm.repository.product.ProductQnaRepository;
import com.team6.onandthefarm.repository.product.ProductWishRepository;
import com.team6.onandthefarm.repository.seller.SellerRepository;
import com.team6.onandthefarm.vo.product.ProductQnAResponse;
import com.team6.onandthefarm.vo.product.ProductQnaAnswerResponse;
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
import com.team6.onandthefarm.vo.product.ProductSelectionResponse;

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
		Optional<Category> category = categoryRepository.findById(productUpdateFormDto.getProductCategoryId());
		product.get().setProductName(productUpdateFormDto.getProductName());
		product.get().setCategory(category.get());
		product.get().setProductPrice(productUpdateFormDto.getProductPrice());
		product.get().setProductTotalStock(productUpdateFormDto.getProductTotalStock());
		//product.get().~~~~ 이미지 추가 해야함
		product.get().setProductDetail(productUpdateFormDto.getProductDetail());
		product.get().setProductOriginPlace(productUpdateFormDto.getProductOriginPlace());
		product.get().setProductDeliveryCompany(productUpdateFormDto.getProductDeliveryCompany());
		product.get().setProductStatus(productUpdateFormDto.getProductStatus());
		product.get().setProductDetailShort(productUpdateFormDto.getProductDetailShort());
		product.get().setProductUpdateDate(dateUtils.transDate(env.getProperty("dateutils.format")));

		return product.get().getProductId();
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

	public List<ProductSelectionResponse> getAllProductListOrderByNewest(Integer pageNumber){
		PageRequest pageRequest = PageRequest.of(pageNumber, 8, Sort.by("productRegisterDate").descending());
		return productPagingRepository.findAllProductOrderByNewest(pageRequest)
				.stream()
				.map(ProductSelectionResponse::new)
				.collect(Collectors.toList());
	}

	public List<ProductSelectionResponse> getProductsListByHighPrice(Integer pageNumber) {
		PageRequest pageRequest = PageRequest.of(pageNumber,8, Sort.by("productPrice").descending());
		return productPagingRepository.findProductListByHighPrice(pageRequest)
				.stream()
				.map(ProductSelectionResponse::new)
				.collect(Collectors.toList());
	}

	public List<ProductSelectionResponse> getProductsListByLowPrice(Integer pageNumber) {
		PageRequest pageRequest = PageRequest.of(pageNumber,8, Sort.by("productPrice").ascending());
		return productPagingRepository.findProductListByLowPrice(pageRequest)
				.stream()
				.map(ProductSelectionResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ProductSelectionResponse> getProductsBySoldCount(Integer pageNumber) {
		PageRequest pageRequest = PageRequest.of(pageNumber,8, Sort.by("productSoldCount").descending());
		return productPagingRepository.findProductBySoldCount(pageRequest)
				.stream()
				.map(ProductSelectionResponse::new)
				.collect(Collectors.toList());
	}

	public List<ProductSelectionResponse> getProductListBySellerNewest(Long sellerId, Integer pageNumber){
		PageRequest pageRequest = PageRequest.of(pageNumber, 8, Sort.by("productRegisterDate").descending());
		return productPagingRepository.findProductBySellerNewest(pageRequest, sellerId)
				.stream()
				.map(ProductSelectionResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ProductSelectionResponse> getProductListByCategoryNewest(Long categoryId, Integer pageNumber) {
		PageRequest pageRequest = PageRequest.of(pageNumber,8, Sort.by("productRegisterDate").descending());
		return productPagingRepository.findProductsByCategoryNewest(pageRequest,categoryId)
				.stream()
				.map(ProductSelectionResponse::new)
				.collect(Collectors.toList());
	}

	public Map<ProductQnAResponse, ProductQnaAnswerResponse> findProductQnAList(Long productId){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Optional<Product> product = productRepository.findById(productId);
		List<ProductQna> productQnas = productQnaRepository.findByProduct(product.get());
		// QNA : QNA답변
		Map<ProductQnAResponse, ProductQnaAnswerResponse> matching = new HashMap<>();
		for(ProductQna productQna : productQnas){
			ProductQnAResponse response = modelMapper.map(productQna,ProductQnAResponse.class);
			if(productQna.getProductQnaStatus().equals("qna0")||productQna.getProductQnaStatus().equals("qna2")){
				matching.put(response,null);
			}
			else{
				ProductQnaAnswer productQnaAnswer = productQnaAnswerRepository.findByProductQna(productQna);
				ProductQnaAnswerResponse productQnaAnswerResponse
						= modelMapper.map(productQnaAnswer, ProductQnaAnswerResponse.class);
				matching.put(response,productQnaAnswerResponse);
			}
		}

		return matching;
	}
}
