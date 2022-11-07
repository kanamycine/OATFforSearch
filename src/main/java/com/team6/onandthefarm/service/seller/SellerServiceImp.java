package com.team6.onandthefarm.service.seller;

import com.team6.onandthefarm.dto.seller.SellerDto;
import com.team6.onandthefarm.dto.seller.SellerMypageDto;
import com.team6.onandthefarm.dto.seller.SellerQnaDto;
import com.team6.onandthefarm.entity.admin.Admin;
import com.team6.onandthefarm.entity.order.OrderProduct;
import com.team6.onandthefarm.entity.product.Product;
import com.team6.onandthefarm.entity.product.ProductQna;
import com.team6.onandthefarm.entity.product.ProductQnaAnswer;
import com.team6.onandthefarm.entity.review.Review;
import com.team6.onandthefarm.entity.seller.Seller;
import com.team6.onandthefarm.entity.user.User;
import com.team6.onandthefarm.repository.admin.AdminRepository;
import com.team6.onandthefarm.repository.order.OrderProductRepository;
import com.team6.onandthefarm.repository.product.ProductImgRepository;
import com.team6.onandthefarm.repository.product.ProductQnaAnswerRepository;
import com.team6.onandthefarm.repository.product.ProductQnaRepository;
import com.team6.onandthefarm.repository.product.ProductRepository;
import com.team6.onandthefarm.repository.review.ReviewRepository;
import com.team6.onandthefarm.repository.seller.SellerRepository;
import com.team6.onandthefarm.security.jwt.JwtTokenUtil;
import com.team6.onandthefarm.security.jwt.Token;
import com.team6.onandthefarm.util.DateUtils;
import com.team6.onandthefarm.util.S3Upload;
import com.team6.onandthefarm.vo.seller.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
@Transactional
@Slf4j
public class SellerServiceImp implements SellerService{

    private final int listNum = 5;

    private final int pageContentNumber = 8;
    
    private SellerRepository sellerRepository;

    private final AdminRepository adminRepository;

    private final ProductQnaRepository productQnaRepository;

    private final ProductQnaAnswerRepository productQnaAnswerRepository;

    private ReviewRepository reviewRepository;

    private ProductRepository productRepository;

    private OrderProductRepository orderProductRepository;

    private ProductImgRepository productImgRepository;

    private DateUtils dateUtils;

    private S3Upload s3Upload;

    private final JwtTokenUtil jwtTokenUtil;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private Environment env;


    @Autowired
    public SellerServiceImp(SellerRepository sellerRepository,
                            AdminRepository adminRepository,
                            DateUtils dateUtils,
                            Environment env,
                            ProductQnaRepository productQnaRepository,
                            ProductQnaAnswerRepository productQnaAnswerRepository,
                            ReviewRepository reviewRepository,
                            ProductRepository productRepository,
                            JwtTokenUtil jwtTokenUtil,
                            OrderProductRepository orderProductRepository,
                            ProductImgRepository productImgRepository,
                            BCryptPasswordEncoder bCryptPasswordEncoder,
                            S3Upload s3Upload) {

        this.sellerRepository = sellerRepository;
        this.adminRepository = adminRepository;
        this.dateUtils=dateUtils;
        this.env=env;
        this.productQnaRepository=productQnaRepository;
        this.productQnaAnswerRepository=productQnaAnswerRepository;
        this.reviewRepository=reviewRepository;
        this.productRepository=productRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.orderProductRepository=orderProductRepository;
        this.productImgRepository=productImgRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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
                .sellerProfileImg(seller.getSellerProfileImg())
                .build();
        return response;
    }

    /**
     * 회원의 비밀번호값을 변경해주는 메서드
     * @param sellerDto
     */
    public Boolean updatePassword(SellerDto sellerDto){
        Optional<Seller> seller = sellerRepository.findBySellerEmail(sellerDto.getEmail());
        seller.get().setSellerPassword(sellerDto.getPassword());
        if(seller.get().getSellerPassword().equals(sellerDto.getPassword())){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public String searchSellerId(String name, String phone){
        Seller seller = sellerRepository.findBySellerNameAndAndSellerPhone(name,phone);
        if(seller==null){
            return "";
        }
        return seller.getSellerEmail();
    }

    public Boolean searchSellerpasswd(String sellerEmail, String name){
        Seller seller = sellerRepository.findBySellerEmailAndSellerName(sellerEmail,name);

        if(seller==null){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     *
     * @param sellerDto
     * @return true: 회원가입 됨  false: 회원가입 실패
     */
    public boolean sellerSignup(SellerDto sellerDto){

        String encodePassword = bCryptPasswordEncoder.encode(sellerDto.getPassword());

        Optional<Seller> savedSeller = sellerRepository.findBySellerEmail(sellerDto.getEmail());
        if(savedSeller.isPresent()){
            return false;
        }

        String date = dateUtils.transDate(env.getProperty("dateutils.format"));
        Seller seller = Seller.builder()
                .sellerEmail(sellerDto.getEmail())
                .sellerAddress(sellerDto.getAddress())
                .sellerAddressDetail(sellerDto.getAddressDetail())
                .sellerBusinessNumber(sellerDto.getBusinessNumber())
                .sellerName(sellerDto.getName())
                .sellerPassword(encodePassword)
                .sellerPhone(sellerDto.getPhone())
                .sellerZipcode(sellerDto.getZipcode())
                .sellerRegisterDate(date)
                .sellerShopName(sellerDto.getShopName())
                .sellerIsActivated(Boolean.TRUE)
                .role("ROLE_SELLER")
                .sellerFollowerCount(0)
                .sellerFollowingCount(0)
                .sellerProfileImg("https://lotte-06-s3-test.s3.ap-northeast-2.amazonaws.com/profile/seller/basic_profile.png")
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
        Optional<Seller> email = sellerRepository.findBySellerEmail(sellerEmail);
        if(!email.isPresent()){
            return true;
        }
        return false;
    }

    /**
     * 셀러가 가진 QNA 조회
     * @param sellerId
     * @return
     */
    public SellerProductQnaResponseResult findSellerQnA(Long sellerId,Integer pageNumber){
        SellerProductQnaResponseResult sellerProductQnaResponseResult = new SellerProductQnaResponseResult();
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
            response.setUserProfileImg(user.getUserProfileImg());
            if(productQna.getProductQnaStatus().equals("completed")){
                String answer = productQnaAnswerRepository
                        .findByProductQna(productQna)
                        .getProductQnaAnswerContent();
                response.setProductSellerAnswer(answer);
            }
            sellerProductQnaResponses.add(response);
        }

        sellerProductQnaResponses.sort((o1, o2) -> {
            int result = o2.getProductQnaCreatedAt().compareTo(o1.getProductQnaCreatedAt());
            return result;
        });

        int startIndex = pageNumber*pageContentNumber;

        int size = sellerProductQnaResponses.size();

        if(size<startIndex+pageContentNumber) {
            sellerProductQnaResponseResult
                    .setSellerProductQnaResponseList(sellerProductQnaResponses.subList(startIndex, size));
        }
        else{
            sellerProductQnaResponseResult
                    .setSellerProductQnaResponseList(
                            sellerProductQnaResponses.subList(startIndex,startIndex+pageContentNumber));
        }
        sellerProductQnaResponseResult.setCurrentPageNum(pageNumber);
        if(size%pageContentNumber!=0){
            sellerProductQnaResponseResult.setTotalPageNum((size/pageContentNumber)+1);
        }
        else{
            sellerProductQnaResponseResult.setTotalPageNum(size/pageContentNumber);
        }
        return sellerProductQnaResponseResult;
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
        Set<Long> orderList = new HashSet<>(); // 기간 총 주문을 담은 set
        List<Integer> dayOrderCounts = new ArrayList<>();

        while(true){
            int dayPrice = 0;
            int dayOrderCount = 0;
            List<OrderProduct> orderProductList =
                    orderProductRepository.findBySellerIdAndOrderProductDateStartingWith(
                    sellerMypageDto.getSellerId(),nextDate);
            for(OrderProduct orderProduct : orderProductList){
                if(!orderList.contains(orderProduct.getOrders().getOrdersId())){
                    orderList.add(orderProduct.getOrders().getOrdersId());
                    dayOrderCount++;
                }
                dayPrice+=orderProduct.getOrderProductPrice()*orderProduct.getOrderProductQty();
            }
            dayPrices.add(dayPrice);
            dayOrderCounts.add(dayOrderCount);
            nextDate = dateUtils.nextDate(nextDate);
            if(nextDate.equals(endDate)){
                break;
            }
        }

        int dayPrice = 0;
        int dayOrderCount = 0;
        List<OrderProduct> orderProductList =
                orderProductRepository.findBySellerIdAndOrderProductDateStartingWith(
                        sellerMypageDto.getSellerId(),nextDate);
        for(OrderProduct orderProduct : orderProductList){
            if(!orderList.contains(orderProduct.getOrders().getOrdersId())){
                orderList.add(orderProduct.getOrders().getOrdersId());
                dayOrderCount++;
            }
            dayPrice+=orderProduct.getOrderProductPrice()*orderProduct.getOrderProductQty();
        }
        dayPrices.add(dayPrice);
        dayOrderCounts.add(dayOrderCount);
        for(Integer price : dayPrices){
            totalPrice += price;
        }

        response.setDayPrices(dayPrices);
        response.setTotalPrice(totalPrice);
        response.setTotalOrderCount(orderList.size());
        response.setDayOrderCount(dayOrderCounts);
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
                        .productId(product.getProductId())
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
                        .productId(product.getProductId())
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
    public SellerLoginResponse login(SellerDto sellerDto) {

        SellerLoginResponse sellerLoginResponse = new SellerLoginResponse();

        if(sellerDto.getEmail().equals("admin")){
            Optional<Admin> admin = adminRepository.findAdminByAdminEmailAndAdminPassword(sellerDto.getEmail(), sellerDto.getPassword());

            if(admin.isPresent()) {
                Token token = jwtTokenUtil.generateToken(admin.get().getAdminId(), admin.get().getRole());
                sellerLoginResponse.setToken(token);
                sellerLoginResponse.setRole("admin");

                return sellerLoginResponse;
            }
        }

        Optional<Seller> seller = sellerRepository.findBySellerEmail(sellerDto.getEmail());
        if(seller.isPresent()){
            if(bCryptPasswordEncoder.matches(sellerDto.getPassword(), seller.get().getSellerPassword())) {
                Token token = jwtTokenUtil.generateToken(seller.get().getSellerId(), seller.get().getRole());
                sellerLoginResponse.setToken(token);
                sellerLoginResponse.setRole("seller");
            }
        }

        return sellerLoginResponse;
    }
}
