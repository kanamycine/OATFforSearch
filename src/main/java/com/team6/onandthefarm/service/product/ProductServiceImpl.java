package com.team6.onandthefarm.service.product;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.team6.onandthefarm.dto.product.*;
import com.team6.onandthefarm.entity.cart.Cart;
import com.team6.onandthefarm.entity.order.OrderProduct;
import com.team6.onandthefarm.entity.order.Orders;
import com.team6.onandthefarm.entity.product.*;
import com.team6.onandthefarm.entity.review.Review;
import com.team6.onandthefarm.entity.seller.Seller;
import com.team6.onandthefarm.entity.user.User;
import com.team6.onandthefarm.repository.cart.CartRepository;
import com.team6.onandthefarm.repository.order.OrderProductRepository;
import com.team6.onandthefarm.repository.order.OrderRepository;
import com.team6.onandthefarm.repository.product.*;
import com.team6.onandthefarm.repository.review.ReviewRepository;
import com.team6.onandthefarm.repository.seller.SellerRepository;
import com.team6.onandthefarm.util.S3Upload;
import com.team6.onandthefarm.vo.product.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team6.onandthefarm.entity.category.Category;
import com.team6.onandthefarm.repository.category.CategoryRepository;
import com.team6.onandthefarm.repository.user.UserRepository;
import com.team6.onandthefarm.util.DateUtils;
import org.springframework.web.multipart.MultipartFile;

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
	private ProductImgRepository productImgRepository;
	private ProductWishRepository productWishRepository;
	private CartRepository cartRepository;
	private OrderRepository orderRepository;
	private OrderProductRepository orderProductRepository;
	private ReviewRepository reviewRepository;
	private DateUtils dateUtils;

	private S3Upload s3Upload;
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
							  CartRepository cartRepository,
							  ProductPagingRepository productPagingRepository,
							  OrderRepository orderRepository,
							  OrderProductRepository orderProductRepository,
							  ReviewRepository reviewRepository,
							  ProductImgRepository productImgRepository,
							  S3Upload s3Upload) {
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
		this.cartRepository = cartRepository;
		this.orderRepository = orderRepository;
		this.orderProductRepository = orderProductRepository;
		this.reviewRepository = reviewRepository;
		this.s3Upload=s3Upload;
		this.productImgRepository=productImgRepository;
	}

	@Override
	public Long saveProduct(ProductFormDto productFormDto) throws IOException {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		Product product = modelMapper.map(productFormDto, Product.class);

		Optional<Seller> seller = sellerRepository.findById(productFormDto.getSellerId());

		Long categoryId = productFormDto.getProductCategory();
		Optional<Category> category = categoryRepository.findById(categoryId);

		for(MultipartFile multipartFile : productFormDto.getImages()){
			String url = s3Upload.upload(multipartFile);
			ProductImg img = ProductImg.builder()
					.product(product)
					.productImgSrc(url)
					.build();
			productImgRepository.save(img);
		}


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
	public ProductWishResultDto addProductToWishList(ProductWishFormDto productWishFormDto){
		ProductWishResultDto resultDto = new ProductWishResultDto();

		Optional<User> user = userRepository.findById(productWishFormDto.getUserId());
		Optional<Product> product = productRepository.findById(productWishFormDto.getProductId());

		Optional<Wish> savedWish = productWishRepository.findWishByUserAndProduct(user.get().getUserId(), product.get().getProductId());
		if(savedWish.isPresent()){
			resultDto.setWishId(savedWish.get().getWishId());
			resultDto.setIsCreated(false);
			return resultDto;
		}

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Wish wish = modelMapper.map(productWishFormDto, Wish.class);
		wish.setUser(user.get());
		wish.setProduct(product.get());
		product.get().setProductWishCount(product.get().getProductWishCount() + 1);

		Long wishId = productWishRepository.save(wish).getWishId();
		resultDto.setWishId(wishId);
		resultDto.setIsCreated(true);

		return resultDto;
	}

	@Override
	public List<Long> cancelProductFromWishList(ProductWishCancelDto productWishCancelDto){

		for(Long wishId : productWishCancelDto.getWishId()) {
			Wish wish = productWishRepository.findById(wishId).get();
			productWishRepository.delete(wish);

			Product product = productRepository.findById(wish.getProduct().getProductId()).get();
			product.setProductWishCount(product.getProductWishCount() - 1);
		}

		return productWishCancelDto.getWishId();
	}

	@Override
	public List<ProductWishResponse> getWishList(Long userId) {

		List<Wish> wishList =  productWishRepository.findWishListByUserId(userId);

		List<ProductWishResponse> productInfos = new ArrayList<>();
		for(Wish w : wishList){
			ProductWishResponse productWishResponse = ProductWishResponse.builder()
					.wistId(w.getWishId())
					.productId(w.getProduct().getProductId())
					.productName(w.getProduct().getProductName())
					.productMainImgSrc(w.getProduct().getProductMainImgSrc())
					.productDetail(w.getProduct().getProductDetail())
					.productDetailShort(w.getProduct().getProductDetailShort())
					.productOriginPlace(w.getProduct().getProductOriginPlace())
					.productPrice(w.getProduct().getProductPrice())
					.build();

			productInfos.add(productWishResponse);
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

			Optional<Cart> savedCart = cartRepository.findNotDeletedCartByProduct(productId, userId);
			if(savedCart.isPresent()){
				productDetailResponse.setProductCartStatus(true);
			}
		}
				// product 상품 설명 이미지 dto List 추가 필요
		return productDetailResponse;
	}

	@Override
	public List<ProductSelectionResponse> getAllProductListOrderByNewest(Long userId, Integer pageNumber){
		PageRequest pageRequest = PageRequest.of(pageNumber, 8, Sort.by("productRegisterDate").descending());

		List<Product> productList = productPagingRepository.findAllProductOrderByNewest(pageRequest);
		return setProductSelectResponse(productList, userId);
	}

	@Override
	public List<ProductSelectionResponse> getProductsListByHighPrice(Long userId, Integer pageNumber) {
		PageRequest pageRequest = PageRequest.of(pageNumber,8, Sort.by("productPrice").descending());

		List<Product> productList =  productPagingRepository.findProductListByHighPrice(pageRequest);
		return setProductSelectResponse(productList, userId);
	}

	@Override
	public List<ProductSelectionResponse> getProductsListByLowPrice(Long userId, Integer pageNumber) {
		PageRequest pageRequest = PageRequest.of(pageNumber,8, Sort.by("productPrice").ascending());

		List<Product> productList =  productPagingRepository.findProductListByLowPrice(pageRequest);
		return setProductSelectResponse(productList, userId);
	}

	@Override
	public List<ProductSelectionResponse> getProductsBySoldCount(Long userId, Integer pageNumber) {
		PageRequest pageRequest = PageRequest.of(pageNumber,8, Sort.by("productSoldCount").descending());
		List<Product> productList = productPagingRepository.findProductBySoldCount(pageRequest);
		return setProductSelectResponse(productList, userId);
	}

	@Override
	public List<ProductSelectionResponse> getProductListBySellerNewest(Long userId, Long sellerId, Integer pageNumber){
		PageRequest pageRequest = PageRequest.of(pageNumber, 8, Sort.by("productRegisterDate").descending());
		List<Product> productList = productPagingRepository.findProductBySellerNewest(pageRequest, sellerId);
		return setProductSelectResponse(productList, userId);
	}

	@Override
	public List<ProductSelectionResponse> getProductListByCategoryNewest(Long userId, String category, Integer pageNumber) {
		PageRequest pageRequest = PageRequest.of(pageNumber,8, Sort.by("productRegisterDate").descending());
		List<Product> productList = productPagingRepository.findProductsByCategoryNewest(pageRequest,category);
		return setProductSelectResponse(productList, userId);
	}

	@Override
	public List<ProductSelectionResponse> getProductListByCategoryHighest(Long userId, String category, Integer pageNumber) {
		PageRequest pageRequest = PageRequest.of(pageNumber,8, Sort.by("productPrice").descending());
		List<Product> productList = productPagingRepository.findProductByCategoryHighest(pageRequest,category);
		return setProductSelectResponse(productList, userId);
	}

	@Override
	public List<ProductSelectionResponse> getProductListByCategoryLowest(Long userId, String category, Integer pageNumber) {
		PageRequest pageRequest = PageRequest.of(pageNumber,8, Sort.by("productPrice").ascending());
		List<Product> productList = productPagingRepository.findProductByCategoryLowest(pageRequest,category);
		return setProductSelectResponse(productList, userId);
	}

	@Override
	public List<ProductSelectionResponse> getProductsByCategorySoldCount(Long userId, String category, Integer pageNumber){
		PageRequest pageRequest = PageRequest.of(pageNumber,8, Sort.by("productSoldCount").descending());
		List<Product> productList = productPagingRepository.findProductByCategorySoldCount(pageRequest,category);
		return setProductSelectResponse(productList, userId);
	}


	@Override
	public List<ProductQnAResponse> findProductQnAList(Long productId){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Optional<Product> product = productRepository.findById(productId);
		List<ProductQna> productQnas = productQnaRepository.findByProduct(product.get());

		List<ProductQnAResponse> responses = new ArrayList<>();

		for(ProductQna productQna : productQnas){
			User user = userRepository.findById(productQna.getUser().getUserId()).get();
			ProductQnAResponse response = ProductQnAResponse.builder()
					.productQnaStatus(productQna.getProductQnaStatus())
					.productQnaCreatedAt(productQna.getProductQnaCreatedAt())
					.productQnaContent(productQna.getProductQnaContent())
					.productQnaId(productQna.getProductQnaId())
					.productQnaModifiedAt(productQna.getProductQnaModifiedAt())
					.userName(user.getUserName())
					.userProfile(user.getUserProfileImg())
					.build();
			if(productQna.getProductQnaStatus().equals("waiting")){
				responses.add(response);
				continue;
			}
			if(productQna.getProductQnaStatus().equals("deleted")){
				continue;
			}
			String answer =
					productQnaAnswerRepository
							.findByProductQna(productQna)
							.getProductQnaAnswerContent();
			response.setProductSellerAnswer(answer);
			responses.add(response);
		}

//		// QNA : QNA답변
//		Map<ProductQnAResponse, ProductQnaAnswerResponse> matching = new HashMap<>();
//		for(ProductQna productQna : productQnas){
//			ProductQnAResponse response = modelMapper.map(productQna,ProductQnAResponse.class);
//			if(productQna.getProductQnaStatus().equals("waiting")||productQna.getProductQnaStatus().equals("deleted")){
//				matching.put(response,null);
//			}
//			else{
//				ProductQnaAnswer productQnaAnswer = productQnaAnswerRepository.findByProductQna(productQna);
//				ProductQnaAnswerResponse productQnaAnswerResponse
//						= modelMapper.map(productQnaAnswer, ProductQnaAnswerResponse.class);
//				matching.put(response,productQnaAnswerResponse);
//			}
//		}

		return responses;
	}

	/**
	 * 상품별로 로그인한 사용자의 wish, cart 여부 조회 메서드
	 * @param productList, userId
	 * @return List<ProductSelectionResponse>
	 */
	private List<ProductSelectionResponse> setProductSelectResponse(List<Product> productList, Long userId){

		List<ProductSelectionResponse> productResponseList = new ArrayList<>();

		for(Product p : productList) {
			ProductSelectionResponse pResponse = new ProductSelectionResponse(p);

			List<Review> reviewList = reviewRepository.findReviewByProduct(p);
			if(reviewList.size() > 0) {
				Integer reviewSum = 0;
				for (Review review : reviewList) {
					reviewSum += review.getReviewRate();
				}
				pResponse.setReviewRate((double) (reviewSum / reviewList.size()));
			}

			if(userId != null){
				Optional<Wish> savedWish = productWishRepository.findWishByUserAndProduct(userId, p.getProductId());
				if(savedWish.isPresent()){
					pResponse.setProductWishStatus(true);
				}

				Optional<Cart> savedCart = cartRepository.findNotDeletedCartByProduct(p.getProductId(), userId);
				if(savedCart.isPresent()){
					pResponse.setProductCartStatus(true);
				}
			}

			productResponseList.add(pResponse);
		}

		return productResponseList;
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
