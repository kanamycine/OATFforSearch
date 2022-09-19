package com.team6.onandthefarm.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ApiModel(description = "이메일 인증을 위한 객체")
public class EmailRequest {
    private String email;
}
