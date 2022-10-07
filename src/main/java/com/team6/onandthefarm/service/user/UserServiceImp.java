package com.team6.onandthefarm.service.user;

import com.team6.onandthefarm.dto.user.MemberFollowingDto;
import com.team6.onandthefarm.dto.user.UserLoginDto;
import com.team6.onandthefarm.dto.user.UserQnaDto;
import com.team6.onandthefarm.dto.user.UserInfoDto;
import com.team6.onandthefarm.dto.user.UserQnaUpdateDto;
import com.team6.onandthefarm.entity.product.Product;
import com.team6.onandthefarm.entity.product.ProductQna;
import com.team6.onandthefarm.entity.seller.Seller;
import com.team6.onandthefarm.entity.user.Following;
import com.team6.onandthefarm.entity.user.User;
import com.team6.onandthefarm.repository.product.ProductQnaRepository;
import com.team6.onandthefarm.repository.product.ProductRepository;
import com.team6.onandthefarm.repository.seller.SellerRepository;
import com.team6.onandthefarm.repository.user.FollowingRepository;
import com.team6.onandthefarm.repository.user.UserRepository;
import com.team6.onandthefarm.security.jwt.JwtTokenUtil;
import com.team6.onandthefarm.security.jwt.Token;
import com.team6.onandthefarm.security.oauth.dto.OAuth2UserDto;
import com.team6.onandthefarm.security.oauth.provider.KakaoOAuth2;
import com.team6.onandthefarm.security.oauth.provider.NaverOAuth2;
import com.team6.onandthefarm.util.DateUtils;
import com.team6.onandthefarm.vo.user.MemberFollowingCountRequest;
import com.team6.onandthefarm.vo.user.MemberFollowingCountResponse;
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
public class UserServiceImp implements UserService {

	private final UserRepository userRepository;

	private final SellerRepository sellerRepository;

	private final ProductQnaRepository productQnaRepository;

	private final ProductRepository productRepository;

	private final FollowingRepository followingRepository;

	private final KakaoOAuth2 kakaoOAuth2;
	private final NaverOAuth2 naverOAuth2;

	private final JwtTokenUtil jwtTokenUtil;

	private final DateUtils dateUtils;

	private final Environment env;

	@Autowired
	public UserServiceImp(UserRepository userRepository,
			SellerRepository sellerRepository,
			FollowingRepository followingRepository,
			DateUtils dateUtils,
			Environment env,
			ProductQnaRepository productQnaRepository,
			ProductRepository productRepository,
			KakaoOAuth2 kakaoOAuth2,
			NaverOAuth2 naverOAuth2,
			JwtTokenUtil jwtTokenUtil) {
		this.userRepository = userRepository;
		this.sellerRepository = sellerRepository;
		this.followingRepository = followingRepository;
		this.dateUtils = dateUtils;
		this.env = env;
		this.productQnaRepository = productQnaRepository;
		this.productRepository = productRepository;
		this.kakaoOAuth2 = kakaoOAuth2;
		this.naverOAuth2 = naverOAuth2;
		this.jwtTokenUtil = jwtTokenUtil;
	}

	public Boolean createProductQnA(UserQnaDto userQnaDto) {
		Optional<User> user = userRepository.findById(userQnaDto.getUserId());
		Optional<Product> product = productRepository.findById(userQnaDto.getProductId());
		log.info("product 정보  :  " + product.get().toString());
		ProductQna productQna = ProductQna.builder()
				.product(product.get())
				.user(user.get())
				.productQnaContent(userQnaDto.getProductQnaContent())
				.productQnaCreatedAt(dateUtils.transDate(env.getProperty("dateutils.format")))
				.productQnaStatus("waiting")
				.seller(product.get().getSeller())
				.build();
		ProductQna newQna = productQnaRepository.save(productQna);
		if (newQna == null) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	@Override
	public UserTokenResponse login(UserLoginDto userLoginDto) {

		Token token = null;
		Boolean needRegister = false;
		String email = new String();

		String provider = userLoginDto.getProvider();
		if (provider.equals("google")) {

		} else if (provider.equals("naver")) {
			// 카카오 액세스 토큰 받아오기
			String naverAccessToken = naverOAuth2.getAccessToken(userLoginDto);

			if (naverAccessToken != null) {
				// 카카오 액세스 토큰으로 유저 정보 받아오기
				OAuth2UserDto userInfo = naverOAuth2.getUserInfo(naverAccessToken);

				Optional<User> savedUser = userRepository.findByUserEmailAndProvider(userInfo.getEmail(), provider);
				User user = null;

				if (savedUser.isPresent()) {
					user = savedUser.get();

					if (user.getUserName() == null) {
						needRegister = true;
						email = user.getUserEmail();
					}
				} else { // DB에 유저 정보가 없다면 저장
					needRegister = true; // 유저 정보 추가 등록이 필요함
					email = userInfo.getEmail();

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
		} else if (provider.equals("kakao")) {
			// 카카오 액세스 토큰 받아오기
			String kakaoAccessToken = kakaoOAuth2.getAccessToken(userLoginDto);

			if (kakaoAccessToken != null) {
				// 카카오 액세스 토큰으로 유저 정보 받아오기
				OAuth2UserDto userInfo = kakaoOAuth2.getUserInfo(kakaoAccessToken);

				Optional<User> savedUser = userRepository.findByUserEmailAndProvider(userInfo.getEmail(), provider);

				User user = null;
				if (savedUser.isPresent()) {
					user = savedUser.get();

					if (user.getUserName() == null) {
						needRegister = true;
						email = user.getUserEmail();
					}
				} else { // DB에 유저 정보가 없다면 저장
					needRegister = true; // 유저 정보 추가 등록이 필요함
					email = userInfo.getEmail();

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
				.email(email)
				.build();

		return userTokenResponse;
	}

	@Override
	public Boolean logout(Long userId) {
		Optional<User> user = userRepository.findById(userId);

		Long kakaoNumber = user.get().getUserKakaoNumber();
		Long returnKakaoNumber = kakaoOAuth2.logout(kakaoNumber);
		if (returnKakaoNumber == null) {
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

	public List<ProductQnAResponse> findUserQna(Long userId) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		List<ProductQnAResponse> responses = new ArrayList<>();

		Optional<User> user = userRepository.findById(userId);
		if (user.isPresent()) {
			List<ProductQna> productQnas = productQnaRepository.findByUser(user.get());

			for (ProductQna productQna : productQnas) {
				ProductQnAResponse response = modelMapper.map(productQna, ProductQnAResponse.class);
				responses.add(response);
			}
		}

		return responses;
	}

	public UserInfoResponse findUserInfo(Long userId) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		Optional<User> user = userRepository.findById(userId);

		UserInfoResponse response = modelMapper.map(user.get(), UserInfoResponse.class);

		return response;
	}

	/**
	 * 유저의 질의를 수정하는 메서드
	 * @param userQnaUpdateDto
	 * @return
	 */
	public Boolean updateUserQna(UserQnaUpdateDto userQnaUpdateDto) {
		Optional<ProductQna> productQna = productQnaRepository.findById(userQnaUpdateDto.getProductQnaId());
		productQna.get().setProductQnaContent(userQnaUpdateDto.getProductQnaContent());
		productQna.get().setProductQnaModifiedAt(dateUtils.transDate(env.getProperty("dateutils.format")));
		if (productQna.get().getProductQnaContent().equals(userQnaUpdateDto.getProductQnaContent())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean deleteUserQna(Long productQnaId) {
		Optional<ProductQna> productQna = productQnaRepository.findById(productQnaId);
		productQna.get().setProductQnaStatus("deleted");
		if (productQna.get().getProductQnaStatus().equals("deleted")) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	@Override
	public Long addFollowList(MemberFollowingDto memberFollowingDto) {
		Long followingMemberId = memberFollowingDto.getFollowingMemberId();
		Long followerMemberId = memberFollowingDto.getFollowerMemberId();
		String followingMemberRole = memberFollowingDto.getFollowingMemberRole();
		String followerMemberRole = memberFollowingDto.getFollowerMemberRole();

		Optional<Following> savedFollowing = followingRepository.findByFollowingMemberIdAndFollowerMemberId(
				followingMemberId, followerMemberId);

		if (savedFollowing.isPresent()) {
			return savedFollowing.get().getFollowingId();
		}

		if (followingMemberRole.equals("user") && followerMemberRole.equals("user")) {
			User followingMember = userRepository.findById(followingMemberId).get();
			User followerMember = userRepository.findById(followerMemberId).get();

			followingMember.setUserFollowingCount(followingMember.getUserFollowingCount() + 1);
			followerMember.setUserFollowerCount(followerMember.getUserFollowerCount() + 1);
		} else if (followingMemberRole.equals("user") && followerMemberRole.equals("seller")) {
			User followingMember = userRepository.findById(followingMemberId).get();
			Seller followerMember = sellerRepository.findById(followerMemberId).get();

			followingMember.setUserFollowingCount(followingMember.getUserFollowingCount() + 1);
			followerMember.setSellerFollowerCount(followerMember.getSellerFollowerCount() + 1);
		} else if (followingMemberRole.equals("seller") && followerMemberRole.equals("user")) {
			Seller followingMember = sellerRepository.findById(followingMemberId).get();
			User followerMember = userRepository.findById(followerMemberId).get();

			followingMember.setSellerFollowingCount(followingMember.getSellerFollowingCount() + 1);
			followerMember.setUserFollowerCount(followerMember.getUserFollowerCount() + 1);
		} else if (followingMemberRole.equals("seller") && followerMemberRole.equals("seller")) {
			Seller followingMember = sellerRepository.findById(followingMemberId).get();
			Seller followerMember = sellerRepository.findById(followerMemberId).get();

			followingMember.setSellerFollowingCount(followingMember.getSellerFollowingCount() + 1);
			followerMember.setSellerFollowerCount(followingMember.getSellerFollowerCount() + 1);
		}

		Following following = Following.builder()
				.followingMemberId(followingMemberId)
				.followingMemberRole(followingMemberRole)
				.followerMemberId(followerMemberId)
				.followerMemberRole(followerMemberRole)
				.build();
		Long followingId = followingRepository.save(following).getFollowingId();

		return followingId;
	}

	@Override
	public Long cancelFollowList(MemberFollowingDto memberFollowingDto) {
		Long followingCancelMemberId = memberFollowingDto.getFollowingMemberId();
		Long followerCancelMemberId = memberFollowingDto.getFollowerMemberId();
		String followingCancelMemberRole = memberFollowingDto.getFollowingMemberRole();
		String followerCancelMemberRole = memberFollowingDto.getFollowerMemberRole();

		if (followingCancelMemberRole.equals("user") && followerCancelMemberRole.equals("user")) {
			User followingMember = userRepository.findById(followingCancelMemberId).get();
			User followerMember = userRepository.findById(followerCancelMemberId).get();

			followingMember.setUserFollowingCount(followingMember.getUserFollowingCount() - 1);
			followerMember.setUserFollowerCount(followerMember.getUserFollowerCount() - 1);
		} else if (followingCancelMemberRole.equals("user") && followerCancelMemberRole.equals("seller")) {
			User followingMember = userRepository.findById(followingCancelMemberId).get();
			Seller followerMember = sellerRepository.findById(followerCancelMemberId).get();

			followingMember.setUserFollowingCount(followingMember.getUserFollowingCount() - 1);
			followerMember.setSellerFollowerCount(followerMember.getSellerFollowerCount() - 1);
		} else if (followingCancelMemberRole.equals("seller") && followerCancelMemberRole.equals("user")) {
			Seller followingMember = sellerRepository.findById(followingCancelMemberId).get();
			User followerMember = userRepository.findById(followerCancelMemberId).get();

			followingMember.setSellerFollowingCount(followingMember.getSellerFollowingCount() - 1);
			followerMember.setUserFollowerCount(followerMember.getUserFollowerCount() - 1);
		} else if (followingCancelMemberRole.equals("seller") && followerCancelMemberRole.equals("seller")) {
			Seller followingMember = sellerRepository.findById(followingCancelMemberId).get();
			Seller followerMember = sellerRepository.findById(followerCancelMemberId).get();

			followingMember.setSellerFollowingCount(followingMember.getSellerFollowingCount() - 1);
			followerMember.setSellerFollowerCount(followingMember.getSellerFollowerCount() - 1);
		}

		Following following = followingRepository.findByFollowingMemberIdAndFollowerMemberId(
				followingCancelMemberId, followerCancelMemberId).get();
		Long followingId = following.getFollowingId();
		followingRepository.delete(following);

		return followingId;
	}

	@Override
	public MemberFollowingCountResponse getFollowingCount(MemberFollowingCountRequest memberFollowingCountRequest) {
		User user;
		Seller seller;
		MemberFollowingCountResponse memberFollowingCountResponse = null;
		Long memberId = memberFollowingCountRequest.getMemberId();
		String memberRole = memberFollowingCountRequest.getMemberRole();

		if (memberRole.equals("user")) {
			user = userRepository.findById(memberId).get();

			memberFollowingCountResponse = MemberFollowingCountResponse.builder().
					memberId(user.getUserId())
					.followingCount(user.getUserFollowingCount())
					.followerCount(user.getUserFollowerCount()).
					build();
			
		} else if (memberRole.equals("seller")) {
			seller = sellerRepository.findById(memberId).get();

			memberFollowingCountResponse = MemberFollowingCountResponse.builder().
					memberId(seller.getSellerId())
					.followingCount(seller.getSellerFollowingCount())
					.followerCount(seller.getSellerFollowerCount()).
					build();
		}
		
		return memberFollowingCountResponse;	
	}
}
