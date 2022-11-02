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

    private final int pageContentNumber = 8;

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
        this.s3Upload = s3Upload;
        this.productImgRepository = productImgRepository;
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
        for (MultipartFile multipartFile : productFormDto.getImages()) {
            String url = s3Upload.productUpload(multipartFile);
            if (cnt == 0) {
                product.setProductMainImgSrc(url);
            } else {
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
        if (changedProduct.get().getProductStatus().equals("soldout")) {
            changedProduct.get().setProductTotalStock(0);
        }

        //기존 이미지 삭제
        if (productUpdateFormDto.getDeleteImageIdList() != null) {
            for (Long deleteImgId : productUpdateFormDto.getDeleteImageIdList()) {
                Optional<ProductImg> productImg = productImgRepository.findById(deleteImgId);
                productImgRepository.delete(productImg.get());
            }
        }
        //이미지 추가
        if (productUpdateFormDto.getAddImageList() != null) {
            for (MultipartFile productImgs : productUpdateFormDto.getAddImageList()) {
                String url = s3Upload.productUpload(productImgs);

                ProductImg productImg = ProductImg.builder()
                        .product(product.get())
                        .productImgSrc(url)
                        .build();
                productImgRepository.save(productImg);
            }
        }
        //메인 이미지 변경
        if (productUpdateFormDto.getMainImage() != null) {
            MultipartFile mainImage = productUpdateFormDto.getMainImage().get(0);
            String url = s3Upload.productUpload(mainImage);
            product.get().setProductMainImgSrc(url);
        }

        return product.get().getProductId();
    }

    @Override
    public Long deleteProduct(ProductDeleteDto productDeleteDto) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Optional<Product> product = productRepository.findById(productDeleteDto.getProductId());
        product.get().setProductStatus("deleted");
        product.get().setProductUpdateDate(dateUtils.transDate(env.getProperty("dateutils.format")));

        return product.get().getProductId();
    }

    @Override
    public ProductWishResultDto addProductToWishList(ProductWishFormDto productWishFormDto) {
        ProductWishResultDto resultDto = new ProductWishResultDto();

        Optional<User> user = userRepository.findById(productWishFormDto.getUserId());
        Optional<Product> product = productRepository.findById(productWishFormDto.getProductId());

        Optional<Wish> savedWish = productWishRepository.findWishByUserAndProduct(user.get().getUserId(), product.get().getProductId());
        if (savedWish.isPresent()) {
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
    public List<Long> cancelProductFromWishList(ProductWishCancelDto productWishCancelDto) {

        for (Long wishId : productWishCancelDto.getWishId()) {
            Wish wish = productWishRepository.findById(wishId).get();
            wish.setWishStatus(false);

            Product product = productRepository.findById(wish.getProduct().getProductId()).get();
            product.setProductWishCount(product.getProductWishCount() - 1);
        }

        return productWishCancelDto.getWishId();
    }

    @Override
    public List<ProductWishResponse> getWishList(Long userId) {

        List<Wish> wishList = productWishRepository.findWishListByUserId(userId);

        List<ProductWishResponse> productInfos = new ArrayList<>();
        for (Wish w : wishList) {
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
        product.setProductViewCount(product.getProductViewCount() + 1);

        ProductDetailResponse productDetailResponse = new ProductDetailResponse(product);
        productDetailResponse.setProductViewCount(productDetailResponse.getProductViewCount() + 1);
        if (userId != null) {
            Optional<Wish> savedWish = productWishRepository.findWishByUserAndProduct(userId, productId);
            if (savedWish.isPresent()) {
                productDetailResponse.setProductWishStatus(true);
            }

            Optional<Cart> savedCart = cartRepository.findNotDeletedCartByProduct(productId, userId);
            if (savedCart.isPresent()) {
                productDetailResponse.setProductCartStatus(true);
            }
        }

        List<ProductImg> productImgList = productImgRepository.findByProduct(product);
        List<ProductImageResponse> productImgSrcList = new ArrayList<>();
        for (ProductImg productImg : productImgList) {
            ProductImageResponse productImageResponse = new ProductImageResponse();
            productImageResponse.setProductImgId(productImg.getProductImgId());
            productImageResponse.setProductImgSrc(productImg.getProductImgSrc());
            productImgSrcList.add(productImageResponse);
        }
        productDetailResponse.setProductImageList(productImgSrcList);

        List<Review> reviewList = reviewRepository.findReviewByProduct(product);
        productDetailResponse.setReviewCount(reviewList.size());
        productDetailResponse.setReviewRate(0.0);
        if (reviewList.size() > 0) {
            Integer reviewSum = 0;
            for (Review review : reviewList) {
                reviewSum += review.getReviewRate();
            }
            productDetailResponse.setReviewRate((double) reviewSum / reviewList.size());
        }

        return productDetailResponse;
    }

    @Override
    public List<ProductSelectionResponse> getAllProductListOrderByNewest(Long userId, Integer pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber, 8, Sort.by("productRegisterDate").descending());

        Page<Product> productList = productPagingRepository.findAllProductOrderByNewest(pageRequest);
        int totalPage = productList.getTotalPages();
        Long totalElements = productList.getTotalElements();

        return setProductSelectResponse(productList, userId, totalPage, pageNumber, totalElements);
    }

    @Override
    public List<ProductSelectionResponse> getProductsListByHighPrice(Long userId, Integer pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber, 8, Sort.by("productPrice").descending());

        Page<Product> productList = productPagingRepository.findProductListByHighPrice(pageRequest);
        int totalPage = productList.getTotalPages();
        Long totalElements = productList.getTotalElements();

        return setProductSelectResponse(productList, userId, totalPage, pageNumber, totalElements);
    }

    @Override
    public List<ProductSelectionResponse> getProductsListByLowPrice(Long userId, Integer pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber, 8, Sort.by("productPrice").ascending());

        Page<Product> productList = productPagingRepository.findProductListByLowPrice(pageRequest);
        int totalPage = productList.getTotalPages();
        Long totalElements = productList.getTotalElements();

        return setProductSelectResponse(productList, userId, totalPage, pageNumber, totalElements);
    }

    @Override
    public List<ProductSelectionResponse> getMainProductsBySoldCount(Long userId) {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("productSoldCount").descending());

        Page<Product> productList = productPagingRepository.findProductBySoldCount(pageRequest);
        int totalPage = productList.getTotalPages();
        Long totalElements = productList.getTotalElements();

        return setProductSelectResponse(productList, userId, totalPage, 0, totalElements);
    }

    @Override
    public List<ProductSelectionResponse> getProductsBySoldCount(Long userId, Integer pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber, 8, Sort.by("productSoldCount").descending());

        Page<Product> productList = productPagingRepository.findProductBySoldCount(pageRequest);
        int totalPage = productList.getTotalPages();
        Long totalElements = productList.getTotalElements();


        return setProductSelectResponse(productList, userId, totalPage, pageNumber, totalElements);
    }

    @Override
    public List<ProductSelectionResponse> getProductListBySellerNewest(Long userId, Long sellerId, Integer pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber, 8, Sort.by("productRegisterDate").descending());

        Page<Product> productList = productPagingRepository.findProductBySellerNewest(pageRequest, sellerId);
        int totalPage = productList.getTotalPages();
        Long totalElements = productList.getTotalElements();

        return setProductSelectResponse(productList, userId, totalPage, pageNumber, totalElements);
    }

    @Override
    public List<ProductSelectionResponse> getProductListByCategoryNewest(Long userId, String category, Integer pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber, 8, Sort.by("productRegisterDate").descending());

        Page<Product> productList = productPagingRepository.findProductsByCategoryNewest(pageRequest, category);
        int totalPage = productList.getTotalPages();
        Long totalElements = productList.getTotalElements();

        return setProductSelectResponse(productList, userId, totalPage, pageNumber, totalElements);
    }

    @Override
    public List<ProductSelectionResponse> getProductListByCategoryHighest(Long userId, String category, Integer pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber, 8, Sort.by("productPrice").descending());

        Page<Product> productList = productPagingRepository.findProductByCategoryHighest(pageRequest, category);
        int totalPage = productList.getTotalPages();
        Long totalElements = productList.getTotalElements();

        return setProductSelectResponse(productList, userId, totalPage, pageNumber, totalElements);
    }

    @Override
    public List<ProductSelectionResponse> getProductListByCategoryLowest(Long userId, String category, Integer pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber, 8, Sort.by("productPrice").ascending());

        Page<Product> productList = productPagingRepository.findProductByCategoryLowest(pageRequest, category);
        int totalPage = productList.getTotalPages();
        Long totalElements = productList.getTotalElements();

        return setProductSelectResponse(productList, userId, totalPage, pageNumber, totalElements);
    }

    @Override
    public List<ProductSelectionResponse> getProductsByCategorySoldCount(Long userId, String category, Integer pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber, 8, Sort.by("productSoldCount").descending());

        Page<Product> productList = productPagingRepository.findProductByCategorySoldCount(pageRequest, category);
        int totalPage = productList.getTotalPages();
        Long totalElements = productList.getTotalElements();

        return setProductSelectResponse(productList, userId, totalPage, pageNumber, totalElements);
    }

    @Override
    public List<ProductSelectionResponse> getSellingProductListBySellerNewest(Long userId, Long sellerId, Integer pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber, 8, Sort.by("productRegisterDate").descending());

        Page<Product> productList = productPagingRepository.findSellingProductBySellerNewest(pageRequest, sellerId);
        int totalPage = productList.getTotalPages();
        Long totalElements = productList.getTotalElements();

        return setProductSelectResponse(productList, userId, totalPage, pageNumber, totalElements);
    }

    @Override
    public List<ProductSelectionResponse> getPauseProductListBySellerNewest(Long userId, Long sellerId, Integer pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber, 8, Sort.by("productRegisterDate").descending());

        Page<Product> productList = productPagingRepository.findPauseProductBySellerNewest(pageRequest, sellerId);
        int totalPage = productList.getTotalPages();
        Long totalElements = productList.getTotalElements();

        return setProductSelectResponse(productList, userId, totalPage, pageNumber, totalElements);
    }

    @Override
    public ProductQnAResponseResult findProductQnAList(Long productId, Integer pageNumber) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Optional<Product> product = productRepository.findById(productId);
        List<ProductQna> productQnas = productQnaRepository.findByProduct(product.get());

        List<ProductQnAResponse> responses = new ArrayList<>();

        for (ProductQna productQna : productQnas) {
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
            if (productQna.getProductQnaStatus().equals("waiting")) {
                responses.add(response);
                continue;
            }
            if (productQna.getProductQnaStatus().equals("deleted")) {
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

        ProductQnAResponseResult resultResponse = new ProductQnAResponseResult();
        responses.sort((o1, o2) -> {
            int result = o2.getProductQnaCreatedAt().compareTo(o1.getProductQnaCreatedAt());
            return result;
        });

        int startIndex = pageNumber * pageContentNumber;

        int size = responses.size();


        if (size < startIndex + pageContentNumber) {
            resultResponse.setProductQnAResponseList(responses.subList(startIndex, size));
            resultResponse.setCurrentPageNum(pageNumber);
            if (size % pageContentNumber != 0) {
                resultResponse.setTotalPageNum((size / pageContentNumber) + 1);
            } else {
                resultResponse.setTotalPageNum(size / pageContentNumber);
            }
            return resultResponse;
        }

        resultResponse.setProductQnAResponseList(responses.subList(startIndex, startIndex + pageContentNumber));
        resultResponse.setCurrentPageNum(pageNumber);
        if (size % pageContentNumber != 0) {
            resultResponse.setTotalPageNum((size / pageContentNumber) + 1);
        } else {
            resultResponse.setTotalPageNum(size / pageContentNumber);
        }
        return resultResponse;
    }

    /**
     * 상품별로 로그인한 사용자의 wish, cart 여부 조회 메서드
     *
     * @param productList, userId
     * @return List<ProductSelectionResponse>
     */
    private List<ProductSelectionResponse> setProductSelectResponse(
            Page<Product> productList, Long userId, Integer totalPage, Integer nowPage, Long totalElements) {
        List<ProductSelectionResponse> productResponseList = new ArrayList<>();

        for (Product p : productList) {
            ProductSelectionResponse pResponse = new ProductSelectionResponse(p);
            pResponse.setTotalPage(totalPage);
            pResponse.setNowPage(nowPage);
            pResponse.setTotalElement(totalElements);

            List<Review> reviewList = reviewRepository.findReviewByProduct(p);
            pResponse.setProductReviewCount(reviewList.size());
            pResponse.setReviewRate(0.0);
            if (reviewList.size() > 0) {
                Integer reviewSum = 0;
                for (Review review : reviewList) {
                    reviewSum += review.getReviewRate();
                }
                pResponse.setReviewRate((double) reviewSum / reviewList.size());
            }

            if (userId != null) {
                Optional<Wish> savedWish = productWishRepository.findWishByUserAndProduct(userId, p.getProductId());
                if (savedWish.isPresent()) {
                    pResponse.setProductWishStatus(true);
                }

                Optional<Cart> savedCart = cartRepository.findNotDeletedCartByProduct(p.getProductId(), userId);
                if (savedCart.isPresent()) {
                    pResponse.setProductCartStatus(true);
                }
            }

            productResponseList.add(pResponse);
        }

        return productResponseList;
    }

    /**
     * 유저별로 리뷰 작성이 가능한 상품을 조회하는 메서드
     *
     * @param userId
     * @return List<ProductReviewResponse>
     */
    @Override
    public List<ProductReviewResponse> getProductsWithoutReview(Long userId) {

        List<ProductReviewResponse> productReviewResponses = new ArrayList<>();

        List<Orders> orders = orderRepository.findWithOrderAndOrdersStatus(userId);
        for (Orders o : orders) {
            List<OrderProduct> orderProducts = orderProductRepository.findByOrders(o);
            Optional<Seller> seller = sellerRepository.findById(o.getOrdersSellerId());

            for (OrderProduct orderProduct : orderProducts) {
                Optional<Review> review = reviewRepository.findReviewByOrderProductId(orderProduct.getOrderProductId());

                if (!review.isPresent()) {
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

        return productReviewResponses;
    }
}
