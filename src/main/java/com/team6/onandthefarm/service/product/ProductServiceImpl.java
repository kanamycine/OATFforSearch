package com.team6.onandthefarm.service.product;

import java.util.*;
import java.util.stream.Collectors;

import com.team6.onandthefarm.dto.product.ProductImgDto;
import com.team6.onandthefarm.dto.product.ProductWishCancelDto;
import com.team6.onandthefarm.dto.product.ProductWishFormDto;
import com.team6.onandthefarm.entity.order.OrderProduct;
import com.team6.onandthefarm.entity.order.Orders;
import com.team6.onandthefarm.entity.product.ProductQna;
import com.team6.onandthefarm.entity.product.ProductQnaAnswer;
import com.team6.onandthefarm.entity.product.Wish;
import com.team6.onandthefarm.entity.review.Review;
import com.team6.onandthefarm.entity.seller.Seller;
import com.team6.onandthefarm.entity.user.User;
import com.team6.onandthefarm.repository.order.OrderProductRepository;
import com.team6.onandthefarm.repository.order.OrderRepository;
import com.team6.onandthefarm.repository.product.ProductPagingRepository;
import com.team6.onandthefarm.repository.product.ProductQnaAnswerRepository;
import com.team6.onandthefarm.repository.product.ProductQnaRepository;
import com.team6.onandthefarm.repository.product.ProductWishRepository;
import com.team6.onandthefarm.repository.review.ReviewRepository;
import com.team6.onandthefarm.repository.seller.SellerRepository;
import com.team6.onandthefarm.vo.product.*;
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
	private UserRepository userRepository;
	private ProductPagingRepository productPagingRepository;
	private ProductWishRepository productWishRepository;
	private OrderRepository orderRepository;
	private OrderProductRepository orderProductRepository;
	private ReviewRepository reviewRepository;
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
							  UserRepository userRepository,
							  ProductWishRepository productWishRepository,
							  ProductPagingRepository productPagingRepository,
							  OrderRepository orderRepository,
							  OrderProductRepository orderProductRepository,
							  ReviewRepository reviewRepository) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
		this.productPagingRepository = productPagingRepository;
		this.dateUtils = dateUtils;
		this.env = env;
		this.productQnaRepository = productQnaRepository;
		this.productQnaAnswerRepository = productQnaAnswerRepository;
		this.sellerRepository = sellerRepository;
		this.userRepository = userRepository;
		this.productWishRepository = productWishRepository;
		this.orderRepository = orderRepository;
		this.orderProductRepository = orderProductRepository;
		this.reviewRepository = reviewRepository;
	}

	@Override
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

	@Override
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

	@Override
	public Long deleteProduct(ProductDeleteDto productDeleteDto){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		Optional<Product> product = productRepository.findById(productDeleteDto.getProductId());
		product.get().setProductStatus("deleted");
		product.get().setProductUpdateDate(dateUtils.transDate(env.getProperty("dateutils.format")));

		return product.get().getProductId();
	}

	@Override
	public Long addProductToWishList(ProductWishFormDto productWishFormDto){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Wish wish = modelMapper.map(productWishFormDto, Wish.class);

		Optional<User> user = userRepository.findById(productWishFormDto.getUserId());
		Optional<Product> product = productRepository.findById(productWishFormDto.getProductId());

		Optional<Wish> savedWish = productWishRepository.findWishByUserAndProduct(user.get().getUserId(), product.get().getProductId());

		if(savedWish.isPresent()){
			return savedWish.get().getWishId();
		}
		wish.setUser(user.get());
		wish.setProduct(product.get());
		product.get().setProductWishCount(product.get().getProductWishCount() + 1);
		Long wishId = productWishRepository.save(wish).getWishId();

		return wishId;
	}

	@Override
	public Long cancelProductFromWishList(ProductWishCancelDto productWishCancelDto){
		Long wishId = productWishCancelDto.getWishId();
		Wish wish = productWishRepository.findById(wishId).get();
		productWishRepository.delete(wish);
		Product product = productRepository.findById(productWishCancelDto.getProductId()).get();
		product.setProductWishCount(product.getProductWishCount() - 1);
		return wishId;
	}

	@Override
	public List<ProductInfoResponse> getWishList(Long userId) {

		List<Wish> wishList =  productWishRepository.findWishListByUserId(userId);

		List<ProductInfoResponse> productInfos = new ArrayList<>();
		for(Wish w : wishList){
			ProductInfoResponse productInfoResponse = ProductInfoResponse.builder()
					.productId(w.getProduct().getProductId())
					.productName(w.getProduct().getProductName())
					.productMainImgSrc(w.getProduct().getProductMainImgSrc())
					.productDetail(w.getProduct().getProductDetail())
					.productDetailShort(w.getProduct().getProductDetailShort())
					.productPrice(w.getProduct().getProductPrice())
					.build();

			productInfos.add(productInfoResponse);
		}

		return productInfos;
	}

	@Override
	public ProductDetailResponse findProductDetail(Long productId, Long userId) {
		Product product = productRepository.findById(productId).get();
		ProductDetailResponse productDetailResponse = new ProductDetailResponse(product);
		if(userId != null){
			Optional<Wish> savedWish = productWishRepository.findWishByUserAndProduct(userId, productId);
			if(savedWish.isPresent()){
				productDetailResponse.setProductWishStatus(true);
			}
		}
				// product 상품 설명 이미지 dto List 추가 필요
		return productDetailResponse;
	}

	@Override
	public List<ProductSelectionResponse> getAllProductListOrderByNewest(Integer pageNumber){
		PageRequest pageRequest = PageRequest.of(pageNumber, 8, Sort.by("productRegisterDate").descending());
		return productPagingRepository.findAllProductOrderByNewest(pageRequest)
				.stream()
				.map(ProductSelectionResponse::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ProductSelectionResponse> getProductsListByHighPrice(Integer pageNumber) {
		PageRequest pageRequest = PageRequest.of(pageNumber,8, Sort.by("productPrice").descending());
		return productPagingRepository.findProductListByHighPrice(pageRequest)
				.stream()
				.map(ProductSelectionResponse::new)
				.collect(Collectors.toList());
	}

	@Override
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

	@Override
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

	@Override
	public Map<ProductQnAResponse, ProductQnaAnswerResponse> findProductQnAList(Long productId){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Optional<Product> product = productRepository.findById(productId);
		List<ProductQna> productQnas = productQnaRepository.findByProduct(product.get());
		// QNA : QNA답변
		Map<ProductQnAResponse, ProductQnaAnswerResponse> matching = new HashMap<>();
		for(ProductQna productQna : productQnas){
			ProductQnAResponse response = modelMapper.map(productQna,ProductQnAResponse.class);
			if(productQna.getProductQnaStatus().equals("waiting")||productQna.getProductQnaStatus().equals("deleted")){
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

	/**
	 * 유저별로 리뷰 작성이 가능한 상품을 조회하는 메서드
	 * @param userId
	 * @return List<ProductReviewResponse>
	 */
	@Override
	public List<ProductReviewResponse> getProductsWithoutReview(Long userId) {

		List<ProductReviewResponse> productReviewResponses = new ArrayList<>();

		List<Orders> orders = orderRepository.findWithOrderAndOrdersStatus(userId);
		for(Orders o : orders){
			List<OrderProduct> orderProducts = orderProductRepository.findByOrders(o);
			Optional<Seller> seller = sellerRepository.findById(o.getOrdersSellerId());

			for(OrderProduct orderProduct : orderProducts){
				Optional<Review> review = reviewRepository.findReviewByOrderProductId(orderProduct.getOrderProductId());

				if(!review.isPresent()) {
					Optional<Product> product = productRepository.findById(orderProduct.getProductId());
					ProductReviewResponse productReviewResponse = ProductReviewResponse.builder()
							.productName(product.get().getProductName())
							.productMainImgSrc(product.get().getProductMainImgSrc())
							.productOriginPlace(product.get().getProductOriginPlace())
							.sellerShopName(seller.get().getSellerShopName())
							.build();
					productReviewResponses.add(productReviewResponse);
				}
			}
		}

		return productReviewResponses;
	}

}
