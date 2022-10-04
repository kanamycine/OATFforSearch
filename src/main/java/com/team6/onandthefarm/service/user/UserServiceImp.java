package com.team6.onandthefarm.service.user;

import com.team6.onandthefarm.dto.user.UserLoginDto;
import com.team6.onandthefarm.dto.user.UserQnaDto;
import com.team6.onandthefarm.dto.user.UserInfoDto;
import com.team6.onandthefarm.entity.product.Product;
import com.team6.onandthefarm.entity.product.ProductQna;
import com.team6.onandthefarm.entity.user.User;
import com.team6.onandthefarm.repository.product.ProductQnaRepository;
import com.team6.onandthefarm.repository.product.ProductRepository;
import com.team6.onandthefarm.repository.user.UserRepository;
import com.team6.onandthefarm.security.jwt.JwtTokenUtil;
import com.team6.onandthefarm.security.jwt.Token;
import com.team6.onandthefarm.security.oauth.dto.OAuth2UserDto;
import com.team6.onandthefarm.security.oauth.provider.KakaoOAuth2;
import com.team6.onandthefarm.security.oauth.provider.NaverOAuth2;
import com.team6.onandthefarm.util.DateUtils;
import com.team6.onandthefarm.vo.user.UserInfoResponse;
import com.team6.onandthefarm.vo.user.UserTokenResponse;
import com.team6.onandthefarm.vo.product.ProductQnAResponse;
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
    private final NaverOAuth2 naverOAuth2;

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
                          NaverOAuth2 naverOAuth2,
                          JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.dateUtils = dateUtils;
        this.env = env;
        this.productQnaRepository=productQnaRepository;
        this.productRepository=productRepository;
        this.kakaoOAuth2 = kakaoOAuth2;
        this.naverOAuth2 = naverOAuth2;
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
    public UserTokenResponse login(UserLoginDto userLoginDto) {

        Token token = null;
        Boolean needRegister = false;

        String provider = userLoginDto.getProvider();
        if(provider.equals("google")){

        }
        else if(provider.equals("naver")){
            // 카카오 액세스 토큰 받아오기
            String naverAccessToken = naverOAuth2.getAccessToken(userLoginDto);

            if(naverAccessToken != null){
                // 카카오 액세스 토큰으로 유저 정보 받아오기
                OAuth2UserDto userInfo = naverOAuth2.getUserInfo(naverAccessToken);

                User user = userRepository.findByUserEmailAndProvider(userInfo.getEmail(), provider);

                // DB에 유저 정보가 없다면 저장
                if(user == null){
                    // 유저 정보 추가 등록이 필요함
                    needRegister = true;

                    User newUser = User.builder()
                            .userEmail(userInfo.getEmail())
                            .role("ROLE_USER")
                            .provider(provider)
                            .userNaverNumber(userInfo.getNaverId())
                            .build();
                    user = userRepository.save(newUser);
                }

                // jwt 토큰 발행
                token = jwtTokenUtil.generateToken(user.getUserId(), user.getRole());
            }
        }
        else if(provider.equals("kakao")){
            // 카카오 액세스 토큰 받아오기
            String kakaoAccessToken = kakaoOAuth2.getAccessToken(userLoginDto);

            if(kakaoAccessToken != null){
                // 카카오 액세스 토큰으로 유저 정보 받아오기
                OAuth2UserDto userInfo = kakaoOAuth2.getUserInfo(kakaoAccessToken);

                User user = userRepository.findByUserEmailAndProvider(userInfo.getEmail(), provider);

                // DB에 유저 정보가 없다면 저장
                if(user == null){
                    // 유저 정보 추가 등록이 필요함
                    needRegister = true;

                    User newUser = User.builder()
                            .userEmail(userInfo.getEmail())
                            .role("ROLE_USER")
                            .provider(provider)
                            .userKakaoNumber(userInfo.getKakaoId())
                            .build();
                    user = userRepository.save(newUser);
                }

                // jwt 토큰 발행
                token = jwtTokenUtil.generateToken(user.getUserId(), user.getRole());
            }
        }
        UserTokenResponse userTokenResponse = UserTokenResponse.builder()
                .token(token)
                .needRegister(needRegister)
                .build();

        return userTokenResponse;
    }

    @Override
    public Boolean logout(Long userId) {
        Optional<User> user = userRepository.findById(userId);

        Long kakaoNumber = user.get().getUserKakaoNumber();
        Long returnKakaoNumber = kakaoOAuth2.logout(kakaoNumber);
        if(returnKakaoNumber == null){
            return false;
        }

        return true;
    }

    @Override
    public Long registerUserInfo(UserInfoDto userInfoDto) {
        Optional<User> user = userRepository.findById(userInfoDto.getUserId());

        user.get().setUserName(userInfoDto.getUserName());
        user.get().setUserPhone(userInfoDto.getUserPhone());
        user.get().setUserZipcode(userInfoDto.getUserZipcode());
        user.get().setUserAddress(userInfoDto.getUserAddress());
        user.get().setUserAddressDetail(userInfoDto.getUserAddressDetail());
        user.get().setUserBirthday(userInfoDto.getUserBirthday());
        user.get().setUserSex(userInfoDto.getUserSex());

        return user.get().getUserId();
    }

    @Override
    public Token reIssueToken(String refreshToken, HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    @Override
    public Long updateUserInfo(UserInfoDto userInfoDto) {
        Optional<User> user = userRepository.findById(userInfoDto.getUserId());

        user.get().setUserName(userInfoDto.getUserName());
        user.get().setUserPhone(userInfoDto.getUserPhone());
        user.get().setUserZipcode(userInfoDto.getUserZipcode());
        user.get().setUserAddress(userInfoDto.getUserAddress());
        user.get().setUserAddressDetail(userInfoDto.getUserAddressDetail());
        user.get().setUserBirthday(userInfoDto.getUserBirthday());
        user.get().setUserSex(userInfoDto.getUserSex());

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
