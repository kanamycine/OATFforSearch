package com.team6.onandthefarm.controller.seller;

import com.team6.onandthefarm.dto.seller.EmailDto;
import com.team6.onandthefarm.dto.seller.SellerDto;
import com.team6.onandthefarm.dto.seller.SellerQnaDto;
import com.team6.onandthefarm.entity.product.ProductQna;
import com.team6.onandthefarm.service.seller.MailService;
import com.team6.onandthefarm.service.seller.SellerService;
import com.team6.onandthefarm.service.seller.SellerServiceImp;
import com.team6.onandthefarm.util.BaseResponse;
import com.team6.onandthefarm.util.DateUtils;
import com.team6.onandthefarm.vo.seller.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/seller")
@Api(value = "셀러",description = "QNA status\n" +
        "     * qna0 : 답변 대기\n" +
        "     * qna1 : 답변 완료\n" +
        "     * qna2 : qna 삭제")
public class SellerController {

    private MailService mailService;

    private SellerService sellerService;

    private DateUtils dateUtils;

    private Environment env;


    @Autowired
    public SellerController(SellerService sellerService, MailService mailService, DateUtils dateUtils, Environment env) {
        this.sellerService = sellerService;
        this.mailService=mailService;
        this.dateUtils=dateUtils;
        this.env=env;
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
        String date = dateUtils.transDate(env.getProperty("dateutils.format"));
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

    @GetMapping("/QnA/{seller-no}")
    @ApiOperation(value = "셀러의 전체 질의 조회")
    public ResponseEntity<BaseResponse<List<SellerProductQnaResponse>>> findSellerQnA (@RequestParam(value = "seller-no") String sellerId){
        List<SellerProductQnaResponse> productQnas = sellerService.findSellerQnA(Long.valueOf(sellerId));
        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(productQnas)
                .build();
        return new ResponseEntity(response,HttpStatus.OK);
    }

    @PostMapping("/QnA")
    @ApiOperation(value = "셀러의 질의 처리")
    public ResponseEntity<BaseResponse> createSellerQnaAnswer (@RequestBody SellerProductQnaAnswerRequest request){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        SellerQnaDto sellerQnaDto = modelMapper.map(request, SellerQnaDto.class);
        Boolean result = sellerService.createQnaAnswer(sellerQnaDto);
        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(result)
                .build();
        return new ResponseEntity(response,HttpStatus.OK);
    }

}
