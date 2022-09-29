package com.team6.onandthefarm.dto.user;

import lombok.Data;

@Data
public class UserLoginDto {

    private String provider;

    private String code;

    private String state;

}
