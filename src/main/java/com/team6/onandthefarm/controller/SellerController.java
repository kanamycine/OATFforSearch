package com.team6.onandthefarm.controller;

import com.team6.onandthefarm.dto.EmailDto;
import com.team6.onandthefarm.dto.SellerDto;
import com.team6.onandthefarm.service.MailService;
import com.team6.onandthefarm.service.SellerService;
import com.team6.onandthefarm.vo.EmailRequest;
import com.team6.onandthefarm.vo.SellerRequest;
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

    private ModelMapper modelMapper;

    @Autowired
    public SellerController(SellerService sellerService, ModelMapper modelMapper, MailService mailService) {
        this.sellerService = sellerService;
        this.modelMapper = modelMapper;
        this.mailService=mailService;
    }

    @PostMapping("/signup")
    @ApiOperation(value = "회원가입")
    public ResponseEntity signup(@RequestBody SellerRequest sellerRequest){
        /**
         * Mapper로 변환
         */
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        SellerDto sellerDto = modelMapper.map(sellerRequest,SellerDto.class);
        /**
         * to do : service로 넘기는 작업
         */
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/email") // 인증버튼 누름
    @ApiOperation(value = "이메일 인증")
    public void emailAuth(@RequestBody EmailRequest emailRequest){
        if(!sellerService.sellerIdCheck(emailRequest.getEmail())){ // email 중복확인
            return;
        }
        String authKey = mailService.sendAuthMail(emailRequest.getEmail());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
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
