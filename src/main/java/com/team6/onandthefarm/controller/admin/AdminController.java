package com.team6.onandthefarm.controller.admin;

import com.team6.onandthefarm.dto.admin.AdminReIssueDto;
import com.team6.onandthefarm.dto.seller.SellerReIssueDto;
import com.team6.onandthefarm.service.admin.AdminSerive;
import com.team6.onandthefarm.util.BaseResponse;
import com.team6.onandthefarm.vo.admin.AdminLoginResponse;
import com.team6.onandthefarm.vo.admin.AdminReIssueRequest;
import com.team6.onandthefarm.vo.seller.SellerLoginResponse;
import com.team6.onandthefarm.vo.seller.SellerReIssueRequest;
import com.team6.onandthefarm.vo.user.UserTokenResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@Slf4j
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminSerive adminSerive;

    @Autowired
    public AdminController(AdminSerive adminSerive){
        this.adminSerive = adminSerive;
    }

    @PostMapping("/refresh")
    @ApiOperation(value = "refresh 토큰으로 access 토큰 재발급")
    public ResponseEntity<BaseResponse<UserTokenResponse>> refresh(@RequestBody AdminReIssueRequest adminReIssueRequest){

        AdminReIssueDto adminReIssueDto = new AdminReIssueDto();
        adminReIssueDto.setAccessToken(adminReIssueRequest.getAccessToken());
        adminReIssueDto.setRefreshToken(adminReIssueRequest.getRefreshToken());

        AdminLoginResponse adminLoginResponse = adminSerive.reIssueToken(adminReIssueDto);

        if(adminLoginResponse == null){
            BaseResponse response = BaseResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("실패")
                    .build();

            return new ResponseEntity(response,HttpStatus.BAD_REQUEST);
        }

        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("성공")
                .data(adminLoginResponse)
                .build();
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/logout")
    @ApiOperation(value = "셀러 로그아웃")
    public ResponseEntity<BaseResponse> logout(@ApiIgnore Principal principal, HttpServletRequest request){

        if(principal == null){
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("no authorization")
                    .build();
            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        Boolean logoutStatus = adminSerive.logout(request);

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

        return new ResponseEntity(response,HttpStatus.OK);
    }
}
