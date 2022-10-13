package com.team6.onandthefarm.service.user;

import com.team6.onandthefarm.dto.user.MemberFollowingDto;
import com.team6.onandthefarm.dto.user.MemberProfileDto;
import com.team6.onandthefarm.dto.user.UserLoginDto;
import com.team6.onandthefarm.dto.user.UserQnaDto;
import com.team6.onandthefarm.dto.user.UserInfoDto;
import com.team6.onandthefarm.dto.user.UserQnaUpdateDto;
import com.team6.onandthefarm.security.jwt.Token;
import com.team6.onandthefarm.vo.user.MemberFollowCountRequest;
import com.team6.onandthefarm.vo.user.MemberFollowCountResponse;
import com.team6.onandthefarm.vo.user.MemberFollowerListRequest;
import com.team6.onandthefarm.vo.user.MemberFollowerListResponse;
import com.team6.onandthefarm.vo.user.MemberFollowingListRequest;
import com.team6.onandthefarm.vo.user.MemberFollowingListResponse;
import com.team6.onandthefarm.vo.user.MemberProfileResponse;
import com.team6.onandthefarm.vo.user.UserInfoResponse;
import com.team6.onandthefarm.vo.user.UserTokenResponse;
import com.team6.onandthefarm.vo.product.ProductQnAResponse;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface UserService {
    Boolean createProductQnA(UserQnaDto userQnaDto);

    UserTokenResponse login(UserLoginDto userLoginDto);

    Boolean logout(Long userId);

    Long registerUserInfo(UserInfoDto userInfoDto);

    Token reIssueToken(String refreshToken, HttpServletRequest request, HttpServletResponse response);

    Long updateUserInfo(UserInfoDto userInfoDto) throws IOException;

    List<ProductQnAResponse> findUserQna(Long userId);

    UserInfoResponse findUserInfo(Long userId);

    Boolean updateUserQna(UserQnaUpdateDto userQnaUpdateDto);

    Boolean deleteUserQna(Long productQnaId);

    Long addFollowList(MemberFollowingDto memberFollowingDto);

    Long cancelFollowList(MemberFollowingDto memberFollowingDto);

    MemberFollowCountResponse getFollowingCount(MemberFollowCountRequest memberFollowCountRequest);

    List<MemberFollowerListResponse> getFollowerList(MemberFollowerListRequest memberFollowerListRequest);

    List<MemberFollowingListResponse> getFollowingList(MemberFollowingListRequest memberFollowingListRequest);

    MemberProfileResponse getMemberProfile(MemberProfileDto memberProfileDto);
}
