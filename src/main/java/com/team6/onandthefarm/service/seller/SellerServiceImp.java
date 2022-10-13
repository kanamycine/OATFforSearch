package com.team6.onandthefarm.service.seller;

import com.team6.onandthefarm.dto.seller.SellerDto;
import com.team6.onandthefarm.dto.seller.SellerMypageDto;
import com.team6.onandthefarm.dto.seller.SellerQnaDto;
import com.team6.onandthefarm.entity.order.OrderProduct;
import com.team6.onandthefarm.entity.product.Product;
import com.team6.onandthefarm.entity.product.ProductImg;
import com.team6.onandthefarm.entity.product.ProductQna;
import com.team6.onandthefarm.entity.product.ProductQnaAnswer;
import com.team6.onandthefarm.entity.review.Review;
import com.team6.onandthefarm.entity.seller.Seller;
import com.team6.onandthefarm.entity.user.User;
import com.team6.onandthefarm.repository.order.OrderProductRepository;
import com.team6.onandthefarm.repository.product.ProductImgRepository;
import com.team6.onandthefarm.repository.product.ProductQnaAnswerRepository;
import com.team6.onandthefarm.repository.product.ProductQnaRepository;
import com.team6.onandthefarm.repository.product.ProductRepository;
import com.team6.onandthefarm.repository.review.ReviewRepository;
import com.team6.onandthefarm.repository.seller.SellerRepository;
import com.team6.onandthefarm.repository.user.UserRepository;
import com.team6.onandthefarm.security.jwt.JwtTokenUtil;
import com.team6.onandthefarm.security.jwt.Token;
import com.team6.onandthefarm.util.DateUtils;
import com.team6.onandthefarm.util.S3Upload;
import com.team6.onandthefarm.vo.order.OrderProductGroupByProduct;
import com.team6.onandthefarm.vo.seller.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class SellerServiceImp implements SellerService{

    private final int listNum = 5;
    
    private SellerRepository sellerRepository;

    private final ProductQnaRepository productQnaRepository;

    private final ProductQnaAnswerRepository productQnaAnswerRepository;

    private ReviewRepository reviewRepository;

    private ProductRepository productRepository;

    private OrderProductRepository orderProductRepository;

    private ProductImgRepository productImgRepository;

    private DateUtils dateUtils;

    private S3Upload s3Upload;

    private final JwtTokenUtil jwtTokenUtil;

    private Environment env;


    @Autowired
    public SellerServiceImp(SellerRepository sellerRepository,
                            DateUtils dateUtils,
                            Environment env,
                            ProductQnaRepository productQnaRepository,
                            ProductQnaAnswerRepository productQnaAnswerRepository,
                            ReviewRepository reviewRepository,
                            ProductRepository productRepository,
                            JwtTokenUtil jwtTokenUtil,
                            OrderProductRepository orderProductRepository,
                            ProductImgRepository productImgRepository,
                            S3Upload s3Upload) {

        this.sellerRepository = sellerRepository;
        this.dateUtils=dateUtils;
        this.env=env;
        this.productQnaRepository=productQnaRepository;
        this.productQnaAnswerRepository=productQnaAnswerRepository;
        this.reviewRepository=reviewRepository;
        this.productRepository=productRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.orderProductRepository=orderProductRepository;
        this.productImgRepository=productImgRepository;
        this.s3Upload=s3Upload;
    }

    public boolean updateByUserId(Long userId, SellerDto sellerDto) throws IOException {
        Optional<Seller> sellerEntity = sellerRepository.findById(userId);

        sellerEntity.get().setSellerZipcode(sellerDto.getZipcode());
        sellerEntity.get().setSellerAddress(sellerDto.getAddress());
        sellerEntity.get().setSellerAddressDetail(sellerDto.getAddressDetail());
        sellerEntity.get().setSellerShopName(sellerDto.getShopName());
        sellerEntity.get().setSellerPhone(sellerDto.getPhone());

        String url = s3Upload.profileSellerUpload(sellerDto.getProfile());
        sellerEntity.get().setSellerProfileImg(url);

        return true;
    }

    /**
     * 셀러 유저의 정보를 조회하는 메서드
     * @param userId
     * @return
     */
    public SellerInfoResponse findByUserId(Long userId){
        Optional<Seller> sellerEntity = sellerRepository.findById(userId);
        Seller seller = sellerEntity.get();

        SellerInfoResponse response = SellerInfoResponse.builder()
                .email(seller.getSellerEmail())
                .zipcode(seller.getSellerZipcode())
                .address(seller.getSellerAddress())
                .addressDetail(seller.getSellerAddressDetail())
                .phone(seller.getSellerPhone())
                .name(seller.getSellerName())
                .businessNumber(seller.getSellerBusinessNumber())
                .registerDate(seller.getSellerRegisterDate())
                .build();
        return response;
    }

    /**
     * 회원의 비밀번호값을 변경해주는 메서드
     * @param sellerDto
     */
    public Boolean updatePassword(SellerDto sellerDto){
        Seller seller = sellerRepository.findBySellerEmail(sellerDto.getEmail());
        seller.setSellerPassword(sellerDto.getPassword());
        if(seller.getSellerPassword().equals(sellerDto.getPassword())){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     *
     * @param sellerDto
     * @return true: 회원가입 됨  false: 회원가입 실패
     */
    public boolean sellerSignup(SellerDto sellerDto){
        String date = dateUtils.transDate(env.getProperty("dateutils.format"));
        Seller seller = Seller.builder()
                .sellerEmail(sellerDto.getEmail())
                .sellerAddress(sellerDto.getAddress())
                .sellerAddressDetail(sellerDto.getAddressDetail())
                .sellerBusinessNumber(sellerDto.getBusinessNumber())
                .sellerName(sellerDto.getName())
                .sellerPassword(sellerDto.getPassword())
                .sellerPhone(sellerDto.getPhone())
                .sellerZipcode(sellerDto.getZipcode())
                .sellerRegisterDate(date)
                .sellerShopName(sellerDto.getShopName())
                .sellerIsActived(Boolean.TRUE)
                .role("ROLE_ADMIN")
                .sellerFollowerCount(0)
                .sellerFollowingCount(0)
                .build();

        seller.setSellerRegisterDate(date);
        sellerRepository.save(seller);
        return true;
    }

    /**
     *
     * @param sellerEmail
     * @return true: 중복안됨 / false: 중복됨
     */
    public boolean sellerIdCheck(String sellerEmail){
        Seller email = sellerRepository.findBySellerEmail(sellerEmail);
        if(email == null){
            return true;
        }
        return false;
    }

    /**
     * 셀러가 가진 QNA 조회
     * @param sellerId
     * @return
     */
    public List<SellerProductQnaResponse> findSellerQnA(Long sellerId){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Optional<Seller> seller = sellerRepository.findById(sellerId);
        List<ProductQna> productQnas = productQnaRepository.findBySeller(seller.get());
        List<SellerProductQnaResponse> sellerProductQnaResponses = new ArrayList<>();
        for(ProductQna productQna : productQnas){
            SellerProductQnaResponse response
                    = modelMapper.map(productQna,SellerProductQnaResponse.class);
            Product product
                    = productRepository.findById(productQna.getProduct().getProductId()).get();
            User user = productQna.getUser();
            response.setProductImg(product.getProductMainImgSrc());
            response.setProductName(product.getProductName());
            response.setUserName(user.getUserName());
            response.setUserProfileImg(null); // 추가 해야 함
            if(productQna.getProductQnaStatus().equals("completed")){
                String answer = productQnaAnswerRepository
                        .findByProductQna(productQna)
                        .getProductQnaAnswerContent();
                response.setProductSellerAnswer(answer);
            }
            sellerProductQnaResponses.add(response);
        }
        return sellerProductQnaResponses;
    }

    /**
     * 답변 생성하는 메서드
     * status
     * waiting(qna0) : 답변 대기
     * completed(qna1) : 답변 완료
     * deleted(qna2) : qna 삭제
     * @param sellerQnaDto
     */
    public Boolean createQnaAnswer(SellerQnaDto sellerQnaDto){
        Optional<ProductQna> qna = productQnaRepository.findById(Long.valueOf(sellerQnaDto.getProductQnaId()));
        qna.get().setProductQnaStatus("completed");
        ProductQnaAnswer productQnaAnswer = ProductQnaAnswer.builder()
                .productQna(qna.get())
                .productQnaAnswerContent(sellerQnaDto.getProductQnaAnswerContent())
                .productQnaAnswerCreatedAt(dateUtils.transDate(env.getProperty("dateutils.format")))
                .build();
        ProductQnaAnswer qnaAnswer = productQnaAnswerRepository.save(productQnaAnswer);
        if(qnaAnswer==null){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 셀러의 마이페이지를 조회하는 메서드
     * @param sellerMypageDto
     * @return
     */
    public SellerMypageResponse findSellerMypage(SellerMypageDto sellerMypageDto){

        SellerMypageResponse response = new SellerMypageResponse();

        List<SellerRecentReviewResponse> reviews = findReviewMypage(sellerMypageDto.getSellerId());

        List<SellerPopularProductResponse> popularProducts = findPopularProduct(sellerMypageDto.getSellerId());

        response.setReviews(reviews);
        response.setPopularProducts(popularProducts);

        List<OrderProduct> orderProducts = orderProductRepository.findBySellerIdAndOrderProductDateBetween(
                sellerMypageDto.getSellerId(),
                sellerMypageDto.getStartDate(),
                sellerMypageDto.getEndDate());

        /*
                해당 기간 총 수익
         */
        int totalPrice = 0; // 기간 총 수익

        /*
                일간 수익 조회
         */
        String startDate = sellerMypageDto.getStartDate().substring(0,10);
        String endDate = sellerMypageDto.getEndDate().substring(0,10);
        String nextDate = startDate;

        List<Integer> dayPrices = new ArrayList<>();

        while(true){
            int dayPrice = 0;
            List<OrderProduct> orderProductList =
                    orderProductRepository.findBySellerIdAndOrderProductDateStartingWith(
                    sellerMypageDto.getSellerId(),nextDate);
            for(OrderProduct orderProduct : orderProductList){
                dayPrice+=orderProduct.getOrderProductPrice()*orderProduct.getOrderProductQty();
            }
            dayPrices.add(dayPrice);
            nextDate = dateUtils.nextDate(nextDate);
            if(nextDate.equals(endDate)){
                break;
            }
        }

        int dayPrice = 0;
        List<OrderProduct> orderProductList =
                orderProductRepository.findBySellerIdAndOrderProductDateStartingWith(
                        sellerMypageDto.getSellerId(),nextDate);
        for(OrderProduct orderProduct : orderProductList){
            dayPrice+=orderProduct.getOrderProductPrice()*orderProduct.getOrderProductQty();
        }
        dayPrices.add(dayPrice);

        for(Integer price : dayPrices){
            totalPrice += price;
        }

        response.setDayPrices(dayPrices);
        response.setTotalPrice(totalPrice);

        return response;
    }

    /**
     * 셀러 메인페이지에 최신 리뷰 4개 보여줄 메서드
     * @param sellerId
     * @return
     */
    public List<SellerRecentReviewResponse> findReviewMypage(Long sellerId){
        List<SellerRecentReviewResponse> responses = new ArrayList<>();

        Optional<Seller> seller = sellerRepository.findById(sellerId);
        List<Review> reviews = reviewRepository.findBySellerOrderByReviewCreatedAtDesc(seller.get());
        if(reviews.size()<listNum){
            for(Review review : reviews){
                if(review.getReviewStatus().equals("deleted")) continue;
                Product product = review.getProduct();
                SellerRecentReviewResponse response = SellerRecentReviewResponse.builder()
                        .productImg(product.getProductMainImgSrc())
                        .productName(product.getProductName())
                        .reviewContent(review.getReviewContent())
                        .reviewRate(review.getReviewRate())
                        .reviewLikeCount(review.getReviewLikeCount())
                        .build();
                responses.add(response);
            }
        }
        else{
            for(Review review : reviews.subList(0,listNum)){
                if(review.getReviewStatus().equals("deleted")) continue;
                Product product = review.getProduct();
                SellerRecentReviewResponse response = SellerRecentReviewResponse.builder()
                        .productImg(product.getProductMainImgSrc())
                        .productName(product.getProductName())
                        .reviewContent(review.getReviewContent())
                        .reviewRate(review.getReviewRate())
                        .reviewLikeCount(review.getReviewLikeCount())
                        .build();
                responses.add(response);
            }
        }


        return responses;
    }

    /**
     * 셀러 메인페이지에 찜순 상품 4개 보여줄 메서드
     * @param sellerId
     * @return
     */
    public List<SellerPopularProductResponse> findPopularProduct(Long sellerId){
        List<SellerPopularProductResponse> responses = new ArrayList<>();

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Optional<Seller> seller = sellerRepository.findById(sellerId);

        List<Product> products = productRepository.findBySellerOrderByProductWishCountDesc(seller.get());

        for(Product product : products){
            SellerPopularProductResponse response =
                    modelMapper.map(product,SellerPopularProductResponse.class);
            responses.add(response);
        }
        if(responses.size()<listNum){
            return responses;
        }
        return responses.subList(0,listNum);
    }
    /**
     * 셀러의 로그인 메소드
     * @param sellerDto
     * @return token
     */

    @Override
    public Token login(SellerDto sellerDto) {

        Token token = null;

        Seller seller = sellerRepository.findBySellerEmailAndSellerPassword(sellerDto.getEmail(), sellerDto.getPassword());
        if(seller != null){
            token = jwtTokenUtil.generateToken(seller.getSellerId(), seller.getRole());
        }

        return token;
    }
}
