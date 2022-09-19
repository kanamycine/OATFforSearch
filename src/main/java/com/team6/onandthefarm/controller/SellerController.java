package com.team6.onandthefarm.controller;

import com.team6.onandthefarm.dto.EmailDto;
import com.team6.onandthefarm.dto.SellerDto;
import com.team6.onandthefarm.service.MailService;
import com.team6.onandthefarm.service.SellerService;
import com.team6.onandthefarm.util.DateUtils;
import com.team6.onandthefarm.vo.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/seller")
public class SellerController {

    private MailService mailService;

    private SellerService sellerService;

    private DateUtils dateUtils;


    @Autowired
    public SellerController(SellerService sellerService, MailService mailService, DateUtils dateUtils) {
        this.sellerService = sellerService;
        this.mailService=mailService;
        this.dateUtils=dateUtils;
    }

    @GetMapping("/{user-no}")
    @ApiOperation(value = "셀러 회원 정보 조회")
    public ResponseEntity<SellerInfoResponse> findBySellerId(@PathVariable("user-no") String userNo){
        SellerInfoResponse response = sellerService.findByUserId(Long.valueOf(userNo));
        return new ResponseEntity(response,HttpStatus.OK);
    }

    @PutMapping("/{user-no}")
    @ApiOperation(value = "셀러 회원 정보 수정")
    public ResponseEntity updateSeller(@PathVariable("user-no") String userNo, @RequestBody SellerUpdateRequest sellerUpdateRequest){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        SellerDto sellerDto = modelMapper.map(sellerUpdateRequest,SellerDto.class);
        sellerService.updateByUserId(Long.valueOf(userNo),sellerDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/signup")
    @ApiOperation(value = "셀러 회원가입")
    public ResponseEntity signup(@RequestBody SellerRequest sellerRequest){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        SellerDto sellerDto = modelMapper.map(sellerRequest,SellerDto.class);
        if(!sellerService.sellerSignup(sellerDto)){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/passwd")
    @ApiOperation(value = "셀러 비밀번호 변경")
    public ResponseEntity changePassword(@RequestBody SellerPasswordRequest sellerPasswordRequest){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        SellerDto sellerDto = modelMapper.map(sellerPasswordRequest,SellerDto.class);
        sellerService.updatePassword(sellerDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/email") // 인증버튼 누름
    @ApiOperation(value = "이메일 인증")
    public void emailAuth(@RequestBody EmailRequest emailRequest){
        if(!sellerService.sellerIdCheck(emailRequest.getEmail())){ // email 중복확인
            return;
        }
        String authKey = mailService.sendAuthMail(emailRequest.getEmail());
        String date = dateUtils.transDate("yyyy.MM.dd HH:mm:ss");
        EmailDto email = EmailDto.builder()
                .email(emailRequest.getEmail())
                .authKey(authKey)
                .date(date)
                .build();
        mailService.save(email);
    }

    @GetMapping("/emailConfirm") // 인증번호 확인
    @ApiOperation(value = "이메일 인증확인")
    public void signUpConfirm(@RequestParam Map<String, String> map){
        boolean result = mailService.checkAuthKey(map);
        log.info("checkout result : "+result);
    }

}
