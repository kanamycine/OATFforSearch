package com.team6.onandthefarm.controller.user;

import com.team6.onandthefarm.dto.user.UserLoginDto;
import com.team6.onandthefarm.dto.user.UserQnaDto;
import com.team6.onandthefarm.dto.user.UserRegisterDto;
import com.team6.onandthefarm.dto.user.UserUpdateDto;
import com.team6.onandthefarm.security.jwt.Token;
import com.team6.onandthefarm.service.user.UserService;
import com.team6.onandthefarm.util.BaseResponse;
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
    public ResponseEntity<BaseResponse<UserLoginResponse>> login(@RequestBody UserLoginRequest userLoginRequest){

        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setProvider(userLoginRequest.getProvider());
        userLoginDto.setCode(userLoginRequest.getCode());
        userLoginDto.setState(userLoginRequest.getState());

        Token accessToken = userService.login(userLoginDto);

        BaseResponse response = null;
        if(accessToken != null){
            UserLoginResponse loginResponse = UserLoginResponse.builder().token(accessToken).build();
            response = BaseResponse.builder().httpStatus(HttpStatus.OK).message("성공").data(loginResponse).build();
        }
        else{
            log.error("oauth 접근 토큰 발급 실패");
            BaseResponse badResponse = BaseResponse.builder().httpStatus(HttpStatus.BAD_REQUEST).message("잘못된 로그인 요청입니다.").build();
            return new ResponseEntity<>(badResponse, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    // 처음 소셜로그인 시 추가 정보 저장
    @PostMapping("/register")
    public ResponseEntity<BaseResponse> join(@ApiIgnore Principal principal, @RequestBody UserRegisterRequest userRegisterRequest) {

        BaseResponse response = null;

        UserRegisterDto userRegisterDto = UserRegisterDto.builder()
                .userId(Long.parseLong(principal.getName()))
                .userZipcode(userRegisterRequest.getUserZipcode())
                .userAddress(userRegisterRequest.getUserAddress())
                .userAddressDetail(userRegisterRequest.getUserAddressDetail())
                .userName(userRegisterRequest.getUserName())
                .userBirthday(userRegisterRequest.getUserBirthday())
                .userPhone(userRegisterRequest.getUserPhone())
                .userSex(userRegisterRequest.getUserSex())
                .build();

        Long userId = userService.registerUserInfo(userRegisterDto);
        if (userId != -1L) {
            response = BaseResponse.builder().httpStatus(HttpStatus.OK).message("성공").build();
        } else {
            response = BaseResponse.builder().httpStatus(HttpStatus.FORBIDDEN).message("실패").build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/update")
    public ResponseEntity<BaseResponse> updateUserInfo(@ApiIgnore Principal principal, @RequestBody UserUpdateRequest userUpdateRequest) {

        BaseResponse response = null;

        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .userId(Long.parseLong(principal.getName()))
                .userZipcode(userUpdateRequest.getUserZipcode())
                .userAddress(userUpdateRequest.getUserAddress())
                .userAddressDetail(userUpdateRequest.getUserAddressDetail())
                .userPhone(userUpdateRequest.getUserPhone())
                .userBirthday(userUpdateRequest.getUserBirthday())
                .userName(userUpdateRequest.getUserName())
                .userSex(userUpdateRequest.getUserSex())
                .build();

        Long userId = userService.updateUserInfo(userUpdateDto);
        if (userId != -1L) {
            response = BaseResponse.builder().httpStatus(HttpStatus.OK).message("성공").build();
        } else {
            response = BaseResponse.builder().httpStatus(HttpStatus.FORBIDDEN).message("실패").build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
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

}
