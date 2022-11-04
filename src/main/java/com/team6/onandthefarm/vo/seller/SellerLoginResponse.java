package com.team6.onandthefarm.vo.seller;

import com.team6.onandthefarm.security.jwt.Token;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "셀러 로그인 성공 시 반환하는 객체")
public class SellerLoginResponse {

    private Token token;

    private String role;
}
