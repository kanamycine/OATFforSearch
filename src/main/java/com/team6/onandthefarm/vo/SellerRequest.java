package com.team6.onandthefarm.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@ApiModel(description = "셀러 상세 정보를 위한 객체")
public class SellerRequest {
    private String email;
    private String password;
    private String zipcode;
    private String address;
    private String addressDetail;
    private String phone;
    private String name;

}
