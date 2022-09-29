package com.team6.onandthefarm.vo.user;

import com.team6.onandthefarm.security.jwt.Token;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginResponse {

    Token token;

}
