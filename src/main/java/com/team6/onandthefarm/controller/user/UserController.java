package com.team6.onandthefarm.controller.user;

import com.team6.onandthefarm.dto.user.UserLoginDto;
import com.team6.onandthefarm.dto.user.UserQnaDto;
import com.team6.onandthefarm.dto.user.UserInfoDto;
import com.team6.onandthefarm.service.user.UserService;
import com.team6.onandthefarm.util.BaseResponse;
import com.team6.onandthefarm.vo.product.ProductQnAResponse;
import com.team6.onandthefarm.vo.user.*;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public ResponseEntity<BaseResponse<UserTokenResponse>> login(@RequestParam String provider, @RequestParam String code, @RequestParam String state){

        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setProvider(provider);
        userLoginDto.setCode(code);
        userLoginDto.setState(state);

        UserTokenResponse userTokenResponse = userService.login(userLoginDto);

        BaseResponse response = null;
        if(userTokenResponse.getToken() != null){
            response = BaseResponse.builder().httpStatus(HttpStatus.OK).message("성공").data(userTokenResponse).build();
        }
        else{
            log.error("oauth 접근 토큰 발급 실패");
            BaseResponse badResponse = BaseResponse.builder().httpStatus(HttpStatus.BAD_REQUEST).message("잘못된 로그인 요청입니다.").build();
            return new ResponseEntity<>(badResponse, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<BaseResponse> logout(@ApiIgnore Principal principal){

        Long userId = Long.parseLong(principal.getName());
        userService.logout(userId);

        BaseResponse response = BaseResponse.builder().httpStatus(HttpStatus.OK).message("성공").build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 처음 소셜로그인 시 추가 정보 저장
    @PostMapping("/register")
    public ResponseEntity<BaseResponse> join(@ApiIgnore Principal principal, @RequestBody UserInfoRequest userInfoRequest) {

        BaseResponse response = null;

        UserInfoDto userInfoDto = UserInfoDto.builder()
                .userId(Long.parseLong(principal.getName()))
                .userZipcode(userInfoRequest.getUserZipcode())
                .userAddress(userInfoRequest.getUserAddress())
                .userAddressDetail(userInfoRequest.getUserAddressDetail())
                .userName(userInfoRequest.getUserName())
                .userBirthday(userInfoRequest.getUserBirthday())
                .userPhone(userInfoRequest.getUserPhone())
                .userSex(userInfoRequest.getUserSex())
                .build();

        Long userId = userService.registerUserInfo(userInfoDto);
        if (userId != -1L) {
            response = BaseResponse.builder().httpStatus(HttpStatus.OK).message("성공").build();
        } else {
            response = BaseResponse.builder().httpStatus(HttpStatus.FORBIDDEN).message("실패").build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/update")
    public ResponseEntity<BaseResponse> updateUserInfo(@ApiIgnore Principal principal, @RequestBody UserInfoRequest userInfoRequest) {

        BaseResponse response = null;

        UserInfoDto userInfoDto = UserInfoDto.builder()
                .userId(Long.parseLong(principal.getName()))
                .userZipcode(userInfoRequest.getUserZipcode())
                .userAddress(userInfoRequest.getUserAddress())
                .userAddressDetail(userInfoRequest.getUserAddressDetail())
                .userPhone(userInfoRequest.getUserPhone())
                .userBirthday(userInfoRequest.getUserBirthday())
                .userName(userInfoRequest.getUserName())
                .userSex(userInfoRequest.getUserSex())
                .build();

        Long userId = userService.updateUserInfo(userInfoDto);
        if (userId != -1L) {
            response = BaseResponse.builder().httpStatus(HttpStatus.OK).message("성공").build();
        } else {
            response = BaseResponse.builder().httpStatus(HttpStatus.FORBIDDEN).message("실패").build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/mypage/info")
    @ApiOperation(value = "유저 정보 조회")
    public ResponseEntity<BaseResponse<UserInfoResponse>> findUserInfo(@ApiIgnore Principal principal){
        UserInfoResponse userInfoResponse = userService.findUserInfo(Long.valueOf(principal.getName()));
        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(userInfoResponse)
                .build();
        return new ResponseEntity(response,HttpStatus.OK);
    }

    @PostMapping("/QnA")
    @ApiOperation(value = "유저 질의 생성")
    public ResponseEntity<BaseResponse> createQnA(@ApiIgnore Principal principal, @RequestBody UserQnaRequest userQnaRequest){

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Long userId = Long.parseLong(principal.getName());
        UserQnaDto userQnaDto = modelMapper.map(userQnaRequest, UserQnaDto.class);
        userQnaDto.setUserId(userId);

        Boolean result = userService.createProductQnA(userQnaDto);
        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(result)
                .build();
        return new ResponseEntity(response,HttpStatus.CREATED);
    }

    @GetMapping("/QnA")
    @ApiOperation(value = "유저 질의 조회")
    public ResponseEntity<BaseResponse<List<ProductQnAResponse>>> findUserQnA(
            @ApiIgnore Principal principal){

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Long userId = Long.parseLong(principal.getName());

        List<ProductQnAResponse> responses = userService.findUserQna(userId);
        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(responses)
                .build();
        return new ResponseEntity(response,HttpStatus.CREATED);
    }

}
