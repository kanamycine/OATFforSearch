package com.team6.onandthefarm.service.user;

import com.team6.onandthefarm.dto.user.*;
import com.team6.onandthefarm.security.jwt.Token;
import com.team6.onandthefarm.vo.product.ProductQnAResultResponse;
import com.team6.onandthefarm.vo.user.*;
import com.team6.onandthefarm.vo.product.ProductQnAResponse;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface UserService {
    Boolean createProductQnA(UserQnaDto userQnaDto);

    UserTokenResponse login(UserLoginDto userLoginDto);

    Boolean logout(HttpServletRequest request, Long userId);

    Boolean loginPhoneConfirm(String phone);

    UserTokenResponse reIssueToken(UserReIssueDto userReIssueDto);

    Long updateUserInfo(UserInfoDto userInfoDto) throws IOException;

    ProductQnAResultResponse findUserQna(Long userId, Integer pageNum);

    UserInfoResponse findUserInfo(Long userId);

    Boolean updateUserQna(UserQnaUpdateDto userQnaUpdateDto);

    Boolean deleteUserQna(Long productQnaId);

    Long addFollowList(MemberFollowingDto memberFollowingDto);

    Long cancelFollowList(MemberFollowingDto memberFollowingDto);

    MemberFollowResult getFollowerList(MemberFollowerListRequest memberFollowerListRequest);

    MemberFollowResult getFollowingList(MemberFollowingListRequest memberFollowingListRequest);

    MemberProfileResponse getMemberProfile(MemberProfileDto memberProfileDto);
}
