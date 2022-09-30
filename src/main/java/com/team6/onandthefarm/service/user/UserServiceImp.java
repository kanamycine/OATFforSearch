package com.team6.onandthefarm.service.user;

import com.team6.onandthefarm.dto.user.UserLoginDto;
import com.team6.onandthefarm.dto.user.UserQnaDto;
import com.team6.onandthefarm.dto.user.UserRegisterDto;
import com.team6.onandthefarm.dto.user.UserUpdateDto;
import com.team6.onandthefarm.entity.product.Product;
import com.team6.onandthefarm.entity.product.ProductQna;
import com.team6.onandthefarm.entity.seller.Seller;
import com.team6.onandthefarm.entity.user.User;
import com.team6.onandthefarm.repository.category.CategoryRepository;
import com.team6.onandthefarm.repository.product.ProductQnaRepository;
import com.team6.onandthefarm.repository.product.ProductRepository;
import com.team6.onandthefarm.repository.user.UserRepository;
import com.team6.onandthefarm.security.jwt.JwtTokenUtil;
import com.team6.onandthefarm.security.jwt.Token;
import com.team6.onandthefarm.security.oauth.dto.OAuth2UserDto;
import com.team6.onandthefarm.security.oauth.provider.KakaoOAuth2;
import com.team6.onandthefarm.util.DateUtils;
import com.team6.onandthefarm.vo.product.ProductQnAResponse;
import com.team6.onandthefarm.vo.user.UserInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class UserServiceImp implements UserService{

    private final UserRepository userRepository;

    private final ProductQnaRepository productQnaRepository;

    private final ProductRepository productRepository;

    private final KakaoOAuth2 kakaoOAuth2;

    private final JwtTokenUtil jwtTokenUtil;

    private final DateUtils dateUtils;

    private final Environment env;

    @Autowired
    public UserServiceImp(UserRepository userRepository,
                          DateUtils dateUtils,
                          Environment env,
                          ProductQnaRepository productQnaRepository,
                          ProductRepository productRepository,
                          KakaoOAuth2 kakaoOAuth2,
                          JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.dateUtils = dateUtils;
        this.env = env;
        this.productQnaRepository=productQnaRepository;
        this.productRepository=productRepository;
        this.kakaoOAuth2 = kakaoOAuth2;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public Boolean createProductQnA(UserQnaDto userQnaDto){
        Optional<User> user = userRepository.findById(userQnaDto.getUserId());
        Optional<Product> product = productRepository.findById(userQnaDto.getProductId());
        log.info("product 정보  :  "+product.get().toString());
        ProductQna productQna = ProductQna.builder()
                .product(product.get())
                .user(user.get())
                .productQnaContent(userQnaDto.getProductQnaContent())
                .productQnaCreatedAt(dateUtils.transDate(env.getProperty("dateutils.format")))
                .productQnaStatus("qna0")
                .seller(product.get().getSeller())
                .build();
        ProductQna newQna = productQnaRepository.save(productQna);
        if(newQna==null){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public Token login(UserLoginDto userLoginDto) {
        Token token = null;

        String provider = userLoginDto.getProvider();
        if(provider.equals("google")){

        }
        else if(provider.equals("naver")){

        }
        else if(provider.equals("kakao")){
            // 카카오 액세스 토큰 받아오기
            String kakaoAccessToken = kakaoOAuth2.getAccessToken(userLoginDto);
            //System.out.println("kakao access token : "+kakaoAccessToken);

            if(kakaoAccessToken != null){
                // 카카오 액세스 토큰으로 유저 정보 받아오기
                OAuth2UserDto userInfo = kakaoOAuth2.getUserInfo(kakaoAccessToken);
                //System.out.println("userInfo : "+userInfo.toString());

                User user = userRepository.findByUserEmailAndProvider(userInfo.getEmail(), provider);

                // DB에 유저 정보가 없다면 저장
                if(user == null){
                    User newUser = User.builder()
                            .userEmail(userInfo.getEmail())
                            .role("ROLE_USER")
                            .provider("kakao")
                            .userKakaoNumber(userInfo.getKakaoId())
                            .build();
                    user = userRepository.save(newUser);
                    //System.out.println("newUser :"+user.toString());
                }

                // jwt 토큰 발행
                token = jwtTokenUtil.generateToken(user.getUserId());
            }
        }
        return token;
    }

    @Override
    public Long registerUserInfo(UserRegisterDto userRegisterDto) {
        Optional<User> user = userRepository.findById(userRegisterDto.getUserId());

        user.get().setUserName(userRegisterDto.getUserName());
        user.get().setUserPhone(userRegisterDto.getUserPhone());
        user.get().setUserZipcode(userRegisterDto.getUserZipcode());
        user.get().setUserAddress(userRegisterDto.getUserAddress());
        user.get().setUserAddressDetail(userRegisterDto.getUserAddressDetail());
        user.get().setUserBirthday(userRegisterDto.getUserBirthday());
        user.get().setUserSex(userRegisterDto.getUserSex());

        return user.get().getUserId();
    }

    @Override
    public Token reIssueToken(String refreshToken, HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    @Override
    public Long updateUserInfo(UserUpdateDto userUpdateDto) {
        Optional<User> user = userRepository.findById(userUpdateDto.getUserId());

        user.get().setUserName(userUpdateDto.getUserName());
        user.get().setUserPhone(userUpdateDto.getUserPhone());
        user.get().setUserZipcode(userUpdateDto.getUserZipcode());
        user.get().setUserAddress(userUpdateDto.getUserAddress());
        user.get().setUserAddressDetail(userUpdateDto.getUserAddressDetail());
        user.get().setUserBirthday(userUpdateDto.getUserBirthday());
        user.get().setUserSex(userUpdateDto.getUserSex());

        return user.get().getUserId();
    }

    public List<ProductQnAResponse> findUserQna(Long userId){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        List<ProductQna> productQnas = productQnaRepository.findByUser_UserId(userId);

        List<ProductQnAResponse> responses = new ArrayList<>();

        for(ProductQna productQna : productQnas){
            ProductQnAResponse response = modelMapper.map(productQna,ProductQnAResponse.class);
            responses.add(response);
        }

        return responses;
    }

    public UserInfoResponse findUserInfo(Long userId){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Optional<User> user = userRepository.findById(userId);

        UserInfoResponse response = modelMapper.map(user.get(),UserInfoResponse.class);

        return response;
    }
}
