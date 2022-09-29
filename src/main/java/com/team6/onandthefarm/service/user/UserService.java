package com.team6.onandthefarm.service.user;

import com.team6.onandthefarm.dto.user.UserLoginDto;
import com.team6.onandthefarm.dto.user.UserQnaDto;
import com.team6.onandthefarm.dto.user.UserRegisterDto;
import com.team6.onandthefarm.dto.user.UserUpdateDto;
import com.team6.onandthefarm.security.jwt.Token;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService {
    Boolean createProductQnA(UserQnaDto userQnaDto);

    Token login(UserLoginDto userLoginDto);

    Long registerUserInfo(UserRegisterDto userRegisterDto);

    Token reIssueToken(String refreshToken, HttpServletRequest request, HttpServletResponse response);

    Long updateUserInfo(UserUpdateDto userUpdateDto);
}
