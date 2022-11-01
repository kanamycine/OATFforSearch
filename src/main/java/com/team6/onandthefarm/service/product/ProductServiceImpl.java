package com.team6.onandthefarm.service.product;

import java.io.IOException;
import java.util.*;

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
import com.team6.onandthefarm.vo.PageVo;
import com.team6.onandthefarm.vo.product.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team6.onandthefarm.entity.category.Category;
import com.team6.onandthefarm.repository.category.CategoryRepository;
import com.team6.onandthefarm.repository.user.UserRepository;
import com.team6.onandthefarm.util.DateUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ProductServiceImpl implements ProductService {

	private final int pageContentNumber = 8;

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

		Long categoryId = productFormDto.getCategoryId();
		Optional<Category> category = categoryRepository.findById(categoryId);

		int cnt = 0;
		for(MultipartFile multipartFile : productFormDto.getImages()){
			String url = s3Upload.productUpload(multipartFile);
			if(cnt==0){
				product.setProductMainImgSrc(url);
			}else {
				ProductImg img = ProductImg.builder()
						.product(product)
						.productImgSrc(url)
						.build();
				productImgRepository.save(img);
			}
			cnt++;
		}


		product.setCategory(category.get());
		product.setProductRegisterDate(dateUtils.transDate(env.getProperty("dateutils.format")));
		product.setSeller(seller.get());
		product.setProductWishCount(0);
		product.setProductSoldCount(0);
		product.setProductViewCount(0);
		return productRepository.save(product).getProductId();
	}

	@Override
	public Long updateProduct(ProductUpdateFormDto productUpdateFormDto) throws IOException {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		Optional<Product> product = productRepository.findById(productUpdateFormDto.getProductId());
		Optional<Category> category = categoryRepository.findById(productUpdateFormDto.getCategoryId());
		product.get().setProductName(productUpdateFormDto.getProductName());
		product.get().setCategory(category.get());
		product.get().setProductPrice(productUpdateFormDto.getProductPrice());
		product.get().setProductTotalStock(productUpdateFormDto.getProductTotalStock());
		product.get().setProductDetail(productUpdateFormDto.getProductDetail());
		product.get().setProductOriginPlace(productUpdateFormDto.getProductOriginPlace());
		product.get().setProductDeliveryCompany(productUpdateFormDto.getProductDeliveryCompany());
		product.get().setProductStatus(productUpdateFormDto.getProductStatus());
		product.get().setProductDetailShort(productUpdateFormDto.getProductDetailShort());
		product.get().setProductUpdateDate(dateUtils.transDate(env.getProperty("dateutils.format")));

		Optional<Product> changedProduct = productRepository.findById(productUpdateFormDto.getProductId());
		if(changedProduct.get().getProductStatus().equals("soldout")){
			changedProduct.get().setProductTotalStock(0);
		}

		//새로운 이미지 추가 시 기존 이미지 삭제 후 이미지 추가
		if(productUpdateFormDto.getAddImageList() != null){
			List<ProductImg> existingProductImgList = productImgRepository.findByProduct(product.get());
			for(ProductImg productImg : existingProductImgList){
				productImgRepository.delete(productImg);
			}

			for(MultipartFile productImgs : productUpdateFormDto.getAddImageList()){
				String url = s3Upload.productUpload(productImgs);

				ProductImg productImg = ProductImg.builder()
						.product(product.get())
						.productImgSrc(url)
						.build();
				productImgRepository.save(productImg);
			}
		}
		//메인 이미지 변경
		if(productUpdateFormDto.getMainImage() != null){
			MultipartFile mainImage = productUpdateFormDto.getMainImage().get(0);
			String url = s3Upload.productUpload(mainImage);
			product.get().setProductMainImgSrc(url);
		}

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
		wish.setWishStatus(true);
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
			wish.setWishStatus(false);

			Product product = productRepository.findById(wish.getProduct().getProductId()).get();
			product.setProductWishCount(product.getProductWishCount() - 1);
		}

		return productWishCancelDto.getWishId();
	}

	@Override
	public ProductWishResult getWishList(Long userId, Integer pageNumber) {

		ProductWishResult productWishResult = new ProductWishResult();

		List<Wish> wishList =  productWishRepository.findWishListByUserId(userId);
		int startIndex = pageNumber * pageContentNumber;
		int size = wishList.size();

		List<ProductWishResponse> productInfos = getWishListPagination(size, startIndex, wishList);

		productWishResult.setProductWishResponseList(productInfos);
		productWishResult.setCurrentPageNum(pageNumber);
		productWishResult.setTotalElementNum(size);
		if(size%pageContentNumber==0){
			productWishResult.setTotalPageNum(size/pageContentNumber);
		}
		else{
			productWishResult.setTotalPageNum((size/pageContentNumber)+1);
		}

		return productWishResult;
	}

	public List<ProductWishResponse> getWishListPagination(int size, int startIndex, List<Wish> wishList){
		List<ProductWishResponse> productInfos = new ArrayList<>();

		if(size < startIndex){
			return productInfos;
		}

		if (size < startIndex + pageContentNumber) {
			for (Wish w : wishList.subList(startIndex, size)) {
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
		for (Wish w : wishList.subList(startIndex, startIndex + pageContentNumber)) {
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
		product.setProductViewCount(product.getProductViewCount()+1);

		ProductDetailResponse productDetailResponse = new ProductDetailResponse(product);
		productDetailResponse.setProductViewCount(productDetailResponse.getProductViewCount()+1);
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

		List<ProductImg> productImgList = productImgRepository.findByProduct(product);
		List<ProductImageResponse> productImgSrcList = new ArrayList<>();
		for(ProductImg productImg : productImgList){
			ProductImageResponse productImageResponse = new ProductImageResponse();
			productImageResponse.setProductImgId(productImg.getProductImgId());
			productImageResponse.setProductImgSrc(productImg.getProductImgSrc());
			productImgSrcList.add(productImageResponse);
		}
		productDetailResponse.setProductImageList(productImgSrcList);

		List<Review> reviewList = reviewRepository.findReviewByProduct(product);
		productDetailResponse.setReviewCount(reviewList.size());
		productDetailResponse.setReviewRate(0.0);
		if(reviewList.size() > 0) {
			Integer reviewSum = 0;
			for (Review review : reviewList) {
				reviewSum += review.getReviewRate();
			}
			productDetailResponse.setReviewRate((double) reviewSum / reviewList.size());
		}

		return productDetailResponse;
	}

	@Override
	public ProductSelectionResponseResult getAllProductListOrderByNewest(Long userId, Integer pageNumber){
		PageRequest pageRequest = PageRequest.of(pageNumber, 16, Sort.by("productRegisterDate").descending());

		Page<Product> productList = productPagingRepository.findProductOrderBy(pageRequest);
		int totalPage = productList.getTotalPages();
		Long totalElements = productList.getTotalElements();

		PageVo pageVo = PageVo.builder()
				.totalPage(totalPage)
				.nowPage(pageNumber)
				.totalElement(totalElements)
				.build();

		return setProductSelectResponse(productList, userId, pageVo);
	}

	@Override
	public ProductSelectionResponseResult getProductsListByHighPrice(Long userId, Integer pageNumber) {
		PageRequest pageRequest = PageRequest.of(pageNumber,16, Sort.by("productPrice").descending());

		Page<Product> productList =  productPagingRepository.findProductOrderBy(pageRequest);
		int totalPage = productList.getTotalPages();
		Long totalElements = productList.getTotalElements();

		PageVo pageVo = PageVo.builder()
				.totalPage(totalPage)
				.nowPage(pageNumber)
				.totalElement(totalElements)
				.build();

		return setProductSelectResponse(productList, userId, pageVo);
	}

	@Override
	public ProductSelectionResponseResult getProductsListByLowPrice(Long userId, Integer pageNumber) {
		PageRequest pageRequest = PageRequest.of(pageNumber,16, Sort.by("productPrice").ascending());

		Page<Product> productList =  productPagingRepository.findProductOrderBy(pageRequest);
		int totalPage = productList.getTotalPages();
		Long totalElements = productList.getTotalElements();

		PageVo pageVo = PageVo.builder()
				.totalPage(totalPage)
				.nowPage(pageNumber)
				.totalElement(totalElements)
				.build();

		return setProductSelectResponse(productList, userId, pageVo);
	}

	@Override
	public ProductSelectionResponseResult getMainProductsBySoldCount(Long userId){
		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("productSoldCount").descending());

		Page<Product> productList = productPagingRepository.findProductOrderBy(pageRequest);
		int totalPage = productList.getTotalPages();
		Long totalElements = productList.getTotalElements();

		PageVo pageVo = PageVo.builder()
				.totalPage(totalPage)
				.nowPage(0)
				.totalElement(totalElements)
				.build();

		return setProductSelectResponse(productList, userId, pageVo);
	}

	@Override
	public ProductSelectionResponseResult getProductsBySoldCount(Long userId, Integer pageNumber) {
		PageRequest pageRequest = PageRequest.of(pageNumber,16, Sort.by("productSoldCount").descending());

		Page<Product> productList =  productPagingRepository.findProductOrderBy(pageRequest);
		int totalPage = productList.getTotalPages();
		Long totalElements = productList.getTotalElements();

		PageVo pageVo = PageVo.builder()
				.totalPage(totalPage)
				.nowPage(pageNumber)
				.totalElement(totalElements)
				.build();

		return setProductSelectResponse(productList, userId, pageVo);
	}

	@Override
	public ProductSelectionResponseResult getProductListBySellerNewest(Long userId, Long sellerId, Integer pageNumber){
		PageRequest pageRequest = PageRequest.of(pageNumber, 16, Sort.by("productRegisterDate").descending());

		Page<Product> productList =  productPagingRepository.findProductBySellerNewest(pageRequest, sellerId);
		int totalPage = productList.getTotalPages();
		Long totalElements = productList.getTotalElements();

		PageVo pageVo = PageVo.builder()
				.totalPage(totalPage)
				.nowPage(pageNumber)
				.totalElement(totalElements)
				.build();

		return setProductSelectResponse(productList, userId, pageVo);
	}

	@Override
	public ProductSelectionResponseResult getProductListByCategoryNewest(Long userId, String category, Integer pageNumber) {
		PageRequest pageRequest = PageRequest.of(pageNumber,16, Sort.by("productRegisterDate").descending());

		Page<Product> productList =  productPagingRepository.findProductsByCategoryOrderBy(pageRequest, category);
		int totalPage = productList.getTotalPages();
		Long totalElements = productList.getTotalElements();

		PageVo pageVo = PageVo.builder()
				.totalPage(totalPage)
				.nowPage(pageNumber)
				.totalElement(totalElements)
				.build();

		return setProductSelectResponse(productList, userId, pageVo);
	}

	@Override
	public ProductSelectionResponseResult getProductListByCategoryHighest(Long userId, String category, Integer pageNumber) {
		PageRequest pageRequest = PageRequest.of(pageNumber,16, Sort.by("productPrice").descending());

		Page<Product> productList =  productPagingRepository.findProductsByCategoryOrderBy(pageRequest, category);
		int totalPage = productList.getTotalPages();
		Long totalElements = productList.getTotalElements();

		PageVo pageVo = PageVo.builder()
				.totalPage(totalPage)
				.nowPage(pageNumber)
				.totalElement(totalElements)
				.build();

		return setProductSelectResponse(productList, userId, pageVo);
	}

	@Override
	public ProductSelectionResponseResult getProductListByCategoryLowest(Long userId, String category, Integer pageNumber) {
		PageRequest pageRequest = PageRequest.of(pageNumber,16, Sort.by("productPrice").ascending());

		Page<Product> productList =  productPagingRepository.findProductsByCategoryOrderBy(pageRequest, category);
		int totalPage = productList.getTotalPages();
		Long totalElements = productList.getTotalElements();

		PageVo pageVo = PageVo.builder()
				.totalPage(totalPage)
				.nowPage(pageNumber)
				.totalElement(totalElements)
				.build();

		return setProductSelectResponse(productList, userId, pageVo);
	}

	@Override
	public ProductSelectionResponseResult getProductsByCategorySoldCount(Long userId, String category, Integer pageNumber){
		PageRequest pageRequest = PageRequest.of(pageNumber,16, Sort.by("productSoldCount").descending());

		Page<Product> productList =  productPagingRepository.findProductsByCategoryOrderBy(pageRequest, category);
		int totalPage = productList.getTotalPages();
		Long totalElements = productList.getTotalElements();

		PageVo pageVo = PageVo.builder()
				.totalPage(totalPage)
				.nowPage(pageNumber)
				.totalElement(totalElements)
				.build();

		return setProductSelectResponse(productList, userId, pageVo);
	}

	@Override
	public ProductSelectionResponseResult getSellingProductListBySellerNewest(Long userId, Long sellerId, Integer pageNumber){
		PageRequest pageRequest = PageRequest.of(pageNumber, 16, Sort.by("productRegisterDate").descending());

		Page<Product> productList =  productPagingRepository.findSellingProductBySellerNewest(pageRequest, sellerId);
		int totalPage = productList.getTotalPages();
		Long totalElements = productList.getTotalElements();

		PageVo pageVo = PageVo.builder()
				.totalPage(totalPage)
				.nowPage(pageNumber)
				.totalElement(totalElements)
				.build();

		return setProductSelectResponse(productList, userId, pageVo);
	}

	@Override
	public ProductSelectionResponseResult getPauseProductListBySellerNewest(Long userId, Long sellerId, Integer pageNumber){
		PageRequest pageRequest = PageRequest.of(pageNumber, 16, Sort.by("productRegisterDate").descending());

		Page<Product> productList =  productPagingRepository.findPauseProductBySellerNewest(pageRequest, sellerId);
		int totalPage = productList.getTotalPages();
		Long totalElements = productList.getTotalElements();

		PageVo pageVo = PageVo.builder()
				.totalPage(totalPage)
				.nowPage(pageNumber)
				.totalElement(totalElements)
				.build();

		return setProductSelectResponse(productList, userId, pageVo);
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
					.userProfileImg(user.getUserProfileImg())
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
	private ProductSelectionResponseResult setProductSelectResponse(Page<Product> productList, Long userId, PageVo pageVo){
		List<ProductSelectionResponse> productResponseList = new ArrayList<>();

		for(Product p : productList) {
			ProductSelectionResponse pResponse = new ProductSelectionResponse(p);


			List<Review> reviewList = reviewRepository.findReviewByProduct(p);
			pResponse.setProductReviewCount(reviewList.size());
			pResponse.setReviewRate(0.0);
			if(reviewList.size() > 0) {
				Integer reviewSum = 0;
				for (Review review : reviewList) {
					reviewSum += review.getReviewRate();
				}
				pResponse.setReviewRate((double) reviewSum / reviewList.size());
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

		ProductSelectionResponseResult productSelectionResponseResult = ProductSelectionResponseResult.builder()
				.productSelectionResponses(productResponseList)
				.pageVo(pageVo)
				.build();

		return productSelectionResponseResult;
	}

	/**
	 * 유저별로 리뷰 작성이 가능한 상품을 조회하는 메서드
	 * @param userId
	 * @return List<ProductReviewResponse>
	 */
	@Override
	public ProductReviewResult getProductsWithoutReview(Long userId, Integer pageNumber) {

		ProductReviewResult productReviewResult = new ProductReviewResult();

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
							.productId(orderProduct.getProductId())
							.orderProductId(orderProduct.getOrderProductId())
							.ordersDate(o.getOrdersDate())
							.build();
					productReviewResponses.add(productReviewResponse);
				}
			}
		}

		int startIndex = pageNumber * pageContentNumber;
		int size = productReviewResponses.size();

		List<ProductReviewResponse> pagedProductReviewResponses = getReviewableProductPagination(size, startIndex, productReviewResponses);
		productReviewResult.setProductReviewResponseList(pagedProductReviewResponses);
		productReviewResult.setCurrentPageNum(pageNumber);
		productReviewResult.setTotalElementNum(size);
		if(size%pageContentNumber==0){
			productReviewResult.setTotalPageNum(size/pageContentNumber);
		}
		else{
			productReviewResult.setTotalPageNum((size/pageContentNumber)+1);
		}

		return productReviewResult;
	}

	public List<ProductReviewResponse> getReviewableProductPagination(int size, int startIndex, List<ProductReviewResponse> productList){
		List<ProductReviewResponse> productReviewResponseList = new ArrayList<>();

		if(size < startIndex){
			return productReviewResponseList;
		}

		if (size < startIndex + pageContentNumber) {
			for (ProductReviewResponse product : productList.subList(startIndex, size)) {
				productReviewResponseList.add(product);
			}
			return productReviewResponseList;
		}
		for (ProductReviewResponse product : productList.subList(startIndex, startIndex + pageContentNumber)) {
			productReviewResponseList.add(product);
		}
		return productReviewResponseList;
	}

}
