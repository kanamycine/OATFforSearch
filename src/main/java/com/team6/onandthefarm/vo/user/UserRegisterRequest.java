package com.team6.onandthefarm.vo.user;

import lombok.Data;

@Data
public class UserRegisterRequest {

    private Integer userZipcode;

    private String userAddress;

    private String userAddressDetail;

    private String userPhone;

    private String userBirthday;

    private Integer userSex;

    private String userName;
}
