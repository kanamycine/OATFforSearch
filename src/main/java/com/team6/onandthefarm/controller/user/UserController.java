package com.team6.onandthefarm.controller.user;

import com.team6.onandthefarm.dto.user.*;
import com.team6.onandthefarm.entity.user.Following;
import com.team6.onandthefarm.service.product.ProductService;
import com.team6.onandthefarm.service.user.UserService;
import com.team6.onandthefarm.util.BaseResponse;
import com.team6.onandthefarm.util.RedisUtil;
import com.team6.onandthefarm.vo.product.*;
import com.team6.onandthefarm.vo.user.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public UserController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @PostMapping("/login")
    @ApiOperation(value = "유저 소셜 로그인")
    public ResponseEntity<BaseResponse<UserTokenResponse>> login(@RequestBody UserLoginRequest userLoginRequest) {

        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setProvider(userLoginRequest.getProvider());
        userLoginDto.setCode(userLoginRequest.getCode());
        userLoginDto.setState(userLoginRequest.getState());

        UserTokenResponse userTokenResponse = userService.login(userLoginDto);

        BaseResponse response = null;
        if (userTokenResponse.getToken() != null) {
            response = BaseResponse.builder().httpStatus(HttpStatus.OK).message("성공").data(userTokenResponse).build();
        } else {
            log.error("oauth 접근 토큰 발급 실패");
            BaseResponse badResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("잘못된 로그인 요청입니다.")
                    .build();
            return new ResponseEntity<>(badResponse, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping("/refresh")
    @ApiOperation(value = "refresh 토큰으로 access 토큰 재발급")
    public ResponseEntity<BaseResponse<UserTokenResponse>> refresh(@RequestBody UserReIssueRequest userReIssueRequest){

        UserReIssueDto userReIssueDto = new UserReIssueDto();
        userReIssueDto.setAccessToken(userReIssueRequest.getAccessToken());
        userReIssueDto.setRefreshToken(userReIssueRequest.getRefreshToken());

        UserTokenResponse userTokenResponse = userService.reIssueToken(userReIssueDto);

        if(userTokenResponse == null){
            BaseResponse response = BaseResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("실패")
                    .build();

            return new ResponseEntity(response,HttpStatus.BAD_REQUEST);
        }

        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("성공")
                .data(userTokenResponse)
                .build();
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/logout")
    @ApiOperation(value = "유저 로그아웃")
    public ResponseEntity<BaseResponse> logout(@ApiIgnore Principal principal, HttpServletRequest request) {

        if(principal == null){
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("no authorization")
                    .build();
            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        String[] principalInfo = principal.getName().split(" ");
        Long userId = Long.parseLong(principalInfo[0]);

        Boolean logoutStatus = userService.logout(request, userId);

        if(!logoutStatus){
            BaseResponse response = BaseResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("실패")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("성공")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/login/phone")
    @ApiOperation(value = "유저 핸드폰 중복확인")
    public ResponseEntity<BaseResponse> loginPhoneConfirm(@RequestParam String phone) {

        Boolean result = userService.loginPhoneConfirm(phone);

        if(result.booleanValue()){
            BaseResponse response = BaseResponse.builder().httpStatus(HttpStatus.OK).message("성공").build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        BaseResponse response = BaseResponse.builder().httpStatus(HttpStatus.BAD_REQUEST).message("실패").build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @PutMapping("/update")
    @ApiOperation(value = "유저 정보 수정")
    public ResponseEntity<BaseResponse> updateUserInfo(
            @ApiIgnore Principal principal,
            @RequestPart(value = "images", required = false) List<MultipartFile> profile,
            @RequestPart(value = "data", required = false) UserInfoRequest userInfoRequest)
            throws Exception{

        if(principal == null){
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("no authorization")
                    .build();
            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        String[] principalInfo = principal.getName().split(" ");
        Long userId = Long.parseLong(principalInfo[0]);

        BaseResponse response = null;

        UserInfoDto userInfoDto = UserInfoDto.builder()
                .userId(userId)
                .userZipcode(userInfoRequest.getUserZipcode())
                .userAddress(userInfoRequest.getUserAddress())
                .userAddressDetail(userInfoRequest.getUserAddressDetail())
                .userPhone(userInfoRequest.getUserPhone())
                .userBirthday(userInfoRequest.getUserBirthday())
                .userName(userInfoRequest.getUserName())
                .userSex(userInfoRequest.getUserSex())
                .build();
        if(profile!=null){
            userInfoDto.setProfile(profile.get(0));
        }
        Long savedUserId = userService.updateUserInfo(userInfoDto);
        if (savedUserId != -1L) {
            response = BaseResponse.builder().httpStatus(HttpStatus.OK).message("성공").build();
        } else {
            response = BaseResponse.builder().httpStatus(HttpStatus.FORBIDDEN).message("실패").build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/mypage/info")
    @ApiOperation(value = "유저 정보 조회")
    public ResponseEntity<BaseResponse<UserInfoResponse>> findUserInfo(@ApiIgnore Principal principal) {

        if(principal == null){
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("no authorization")
                    .build();
            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        String[] principalInfo = principal.getName().split(" ");
        Long userId = Long.parseLong(principalInfo[0]);

        UserInfoResponse userInfoResponse = userService.findUserInfo(userId);
        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(userInfoResponse)
                .build();
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/mypage/wish")
    @ApiOperation(value = "사용자 별 위시리스트 조회")
    public ResponseEntity<BaseResponse<ProductWishResult>> getWishList(@ApiIgnore Principal principal, @RequestParam Integer pageNumber) {

        if(principal == null){
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("no authorization")
                    .build();
            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        String[] principalInfo = principal.getName().split(" ");
        Long userId = Long.parseLong(principalInfo[0]);

        ProductWishResult productInfos = productService.getWishList(userId, pageNumber);

        BaseResponse baseResponse = BaseResponse.builder()
                .httpStatus(HttpStatus.CREATED)
                .message("get wish list by user completed")
                .data(productInfos)
                .build();

        return new ResponseEntity(baseResponse, HttpStatus.OK);
    }

    @GetMapping("/mypage/review")
    @ApiOperation(value = "사용자 별로 작성 가능한 리뷰 조회")
    public ResponseEntity<BaseResponse<ProductReviewResult>> getWritableReviewList(
            @ApiIgnore Principal principal, @RequestParam Integer pageNumber) {

        if(principal == null){
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("no authorization")
                    .build();
            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        String[] principalInfo = principal.getName().split(" ");
        Long userId = Long.parseLong(principalInfo[0]);

        ProductReviewResult productsWithoutReview = productService.getProductsWithoutReview(userId, pageNumber);

        BaseResponse baseResponse = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("get writable reviews completed")
                .data(productsWithoutReview)
                .build();

        return new ResponseEntity(baseResponse, HttpStatus.OK);
    }

    @PostMapping("/QnA")
    @ApiOperation(value = "유저 질의 생성")
    public ResponseEntity<BaseResponse> createQnA(@ApiIgnore Principal principal,
            @RequestBody UserQnaRequest userQnaRequest) {

        if(principal == null){
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("no authorization")
                    .build();
            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        String[] principalInfo = principal.getName().split(" ");
        Long userId = Long.parseLong(principalInfo[0]);

        UserQnaDto userQnaDto = modelMapper.map(userQnaRequest, UserQnaDto.class);
        userQnaDto.setUserId(userId);

        Boolean result = userService.createProductQnA(userQnaDto);
        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(result)
                .build();
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    /**
     * 10/24 상품 이미지 , 상품 이름 추가 지금은 null값
     * @param principal
     * @param pageNum
     * @return
     */
    @GetMapping("/mypage/QnA/{page-num}")
    @ApiOperation(value = "유저 질의 조회")
    public ResponseEntity<BaseResponse<ProductQnAResultResponse>> findUserQnA(
            @ApiIgnore Principal principal, @PathVariable("page-num") String pageNum) {

        if(principal == null){
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("no authorization")
                    .build();
            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        String[] principalInfo = principal.getName().split(" ");
        Long userId = Long.parseLong(principalInfo[0]);

        ProductQnAResultResponse responses = userService.findUserQna(userId,Integer.valueOf(pageNum));
        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("유저 QNA 조회")
                .data(responses)
                .build();
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @PutMapping("/QnA")
    @ApiOperation(value = "유저 질의 수정")
    public ResponseEntity<BaseResponse<Boolean>> updateUserQnA(
            @ApiIgnore Principal principal, @RequestBody UserQnaUpdateRequest userQnaUpdateRequest) {

        if(principal == null){
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("no authorization")
                    .build();
            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        String[] principalInfo = principal.getName().split(" ");
        Long userId = Long.parseLong(principalInfo[0]);

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserQnaUpdateDto userQnaUpdateDto = modelMapper.map(userQnaUpdateRequest, UserQnaUpdateDto.class);
        userQnaUpdateDto.setUserId(userId);
        Boolean result = userService.updateUserQna(userQnaUpdateDto);
        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(result)
                .build();
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PutMapping("/QnA/delete")
    @ApiOperation(value = "유저 질의 삭제")
    public ResponseEntity<BaseResponse<Boolean>> deleteUserQnA(@RequestParam Long productQnaId) {
        Boolean result = userService.deleteUserQna(productQnaId);

        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(result)
                .build();

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping("/follow/add")
    @ApiOperation(value = "다른 유저 팔로우")
    public ResponseEntity<BaseResponse> addFollow(@ApiIgnore Principal principal,
            @RequestBody MemberFollowRequest memberFollowRequest) {

        String[] principalInfo = principal.getName().split(" ");
        Long memberId = Long.parseLong(principalInfo[0]);
        String memberRole = principalInfo[1];

        MemberFollowingDto memberFollowingDto = MemberFollowingDto.builder()
                .followingMemberId(memberId)
                .followingMemberRole(memberRole)
                .followerMemberId(memberFollowRequest.getFollowerMemberId())
                .followerMemberRole(memberFollowRequest.getFollowerMemberRole())
                .build();

        Long followingId = userService.addFollowList(memberFollowingDto);

        if(followingId == null){
            BaseResponse response = BaseResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("OK")
                    .build();

            return new ResponseEntity(response, HttpStatus.FORBIDDEN);
        }

        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(followingId)
                .build();

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PutMapping("/follow/cancel")
    @ApiOperation(value = "다른 유저 팔로우 취소")
    public ResponseEntity<BaseResponse> cancelFollow(@ApiIgnore Principal principal,
            @RequestBody MemberFollowRequest memberFollowRequest) {

        String[] principalInfo = principal.getName().split(" ");
        Long memberId = Long.parseLong(principalInfo[0]);
        String memberRole = principalInfo[1];

        MemberFollowingDto memberFollowingDto = MemberFollowingDto.builder()
                .followingMemberId(memberId)
                .followingMemberRole(memberRole)
                .followerMemberId(memberFollowRequest.getFollowerMemberId())
                .followerMemberRole(memberFollowRequest.getFollowerMemberRole())
                .build();
        Long followingId = userService.cancelFollowList(memberFollowingDto);

        if(followingId == null){
            BaseResponse response = BaseResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("OK")
                    .build();

            return new ResponseEntity(response, HttpStatus.FORBIDDEN);
        }

        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(followingId)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/follow/follower-list")
    @ApiOperation(value = "멤버의 팔로워 유저 리스트 조회")
    public ResponseEntity<BaseResponse<MemberFollowResult>> getFollowerList(
            @ApiIgnore Principal principal,
            @RequestParam Map<String, String> request){

        String[] principalInfo = principal.getName().split(" ");
        Long loginMemberId = Long.parseLong(principalInfo[0]);
        String loginMemberRole = principalInfo[1];

        MemberFollowerListRequest memberFollowerListRequest = new MemberFollowerListRequest();
        memberFollowerListRequest.setPageNumber(Integer.parseInt(request.get("pageNumber")));
        memberFollowerListRequest.setLoginMemberId(loginMemberId);
        memberFollowerListRequest.setLoginMemberRole(loginMemberRole);

        if(request.containsKey("memberId")) {
            memberFollowerListRequest.setMemberId(Long.parseLong(request.get("memberId")));
            memberFollowerListRequest.setMemberRole(request.get("memberRole"));
        }
        else{
            memberFollowerListRequest.setMemberId(loginMemberId);
            memberFollowerListRequest.setMemberRole(loginMemberRole);
        }

        MemberFollowResult followerList = userService.getFollowerList(memberFollowerListRequest);

        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(followerList)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/follow/following-list")
    @ApiOperation(value = "멤버의 팔로잉 유저 리스트 조회")
    public ResponseEntity<BaseResponse<MemberFollowResult>> getFollowingList(
            @ApiIgnore Principal principal,
            @RequestParam Map<String, String> request){

        String[] principalInfo = principal.getName().split(" ");
        Long loginMemberId = Long.parseLong(principalInfo[0]);
        String loginMemberRole = principalInfo[1];

        MemberFollowingListRequest memberFollowingListRequest = new MemberFollowingListRequest();
        memberFollowingListRequest.setPageNumber(Integer.parseInt(request.get("pageNumber")));
        memberFollowingListRequest.setLoginMemberId(loginMemberId);
        memberFollowingListRequest.setLoginMemberRole(loginMemberRole);

        if(request.containsKey("memberId")) {
            memberFollowingListRequest.setMemberId(Long.parseLong(request.get("memberId")));
            memberFollowingListRequest.setMemberRole(request.get("memberRole"));
        }
        else {
            memberFollowingListRequest.setMemberId(loginMemberId);
            memberFollowingListRequest.setMemberRole(loginMemberRole);
        }

        MemberFollowResult followingList = userService.getFollowingList(memberFollowingListRequest);

        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(followingList)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/profile")
    @ApiOperation(value = "멤버의 프로필 조회")
    public ResponseEntity<BaseResponse<MemberProfileResponse>> getUserProfile(@ApiIgnore Principal principal,
                                                                              @RequestParam Map<String, String> request){

        MemberProfileDto memberProfileDto = new MemberProfileDto();

        String[] principalInfo = principal.getName().split(" ");
        Long loginId = Long.parseLong(principalInfo[0]);
        String loginRole = principalInfo[1];

        memberProfileDto.setLoginMemberId(loginId);
        memberProfileDto.setLoginMemberRole(loginRole);

        if(request.containsKey("memberId")) {
            memberProfileDto.setMemberId(Long.parseLong(request.get("memberId")));
            memberProfileDto.setMemberRole(request.get("memberRole"));
        }else{
            memberProfileDto.setMemberId(loginId);
            memberProfileDto.setMemberRole(loginRole);
        }

        MemberProfileResponse memberProfileResponse = userService.getMemberProfile(memberProfileDto);

        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(memberProfileResponse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}