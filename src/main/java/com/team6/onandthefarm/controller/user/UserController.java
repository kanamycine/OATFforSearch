package com.team6.onandthefarm.controller.user;

import com.team6.onandthefarm.dto.user.UserQnaDto;
import com.team6.onandthefarm.service.user.UserService;
import com.team6.onandthefarm.util.BaseResponse;
import com.team6.onandthefarm.vo.user.UserQnaRequest;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/QnA")
    @ApiOperation(value = "유저 질의 생성")
    public ResponseEntity<BaseResponse> createQnA(@RequestBody UserQnaRequest userQnaRequest){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserQnaDto userQnaDto = modelMapper.map(userQnaRequest, UserQnaDto.class);
        Boolean result = userService.createProductQnA(userQnaDto);
        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(result)
                .build();
        return new ResponseEntity(response,HttpStatus.CREATED);
    }

}
