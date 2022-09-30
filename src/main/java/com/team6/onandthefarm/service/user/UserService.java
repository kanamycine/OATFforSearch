package com.team6.onandthefarm.service.user;

import com.team6.onandthefarm.dto.user.UserLoginDto;
import com.team6.onandthefarm.dto.user.UserQnaDto;
import com.team6.onandthefarm.dto.user.UserInfoDto;
import com.team6.onandthefarm.security.jwt.Token;
import com.team6.onandthefarm.vo.user.UserTokenResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService {
    Boolean createProductQnA(UserQnaDto userQnaDto);

    UserTokenResponse login(UserLoginDto userLoginDto);

    Long registerUserInfo(UserInfoDto userRegisterDto);

    Token reIssueToken(String refreshToken, HttpServletRequest request, HttpServletResponse response);

    Long updateUserInfo(UserInfoDto userInfoDto);
}
