package com.team6.onandthefarm.service.user;

import com.team6.onandthefarm.dto.user.UserLoginDto;
import com.team6.onandthefarm.dto.user.UserQnaDto;
import com.team6.onandthefarm.dto.user.UserRegisterDto;
import com.team6.onandthefarm.dto.user.UserUpdateDto;
import com.team6.onandthefarm.security.jwt.Token;
import com.team6.onandthefarm.vo.product.ProductQnAResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface UserService {
    Boolean createProductQnA(UserQnaDto userQnaDto);

    Token login(UserLoginDto userLoginDto);

    Long registerUserInfo(UserRegisterDto userRegisterDto);

    Token reIssueToken(String refreshToken, HttpServletRequest request, HttpServletResponse response);

    Long updateUserInfo(UserUpdateDto userUpdateDto);

    List<ProductQnAResponse> findUserQna(Long userId);
}
