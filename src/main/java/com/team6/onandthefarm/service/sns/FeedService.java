package com.team6.onandthefarm.service.sns;


import com.team6.onandthefarm.dto.sns.*;
import com.team6.onandthefarm.dto.user.MemberProfileDto;
import com.team6.onandthefarm.vo.sns.feed.AddableProductResponse;
import com.team6.onandthefarm.vo.sns.feed.FeedDetailResponse;
import com.team6.onandthefarm.vo.sns.feed.FeedResponse;
import com.team6.onandthefarm.vo.sns.profile.ProfileMainFeedResponse;
import com.team6.onandthefarm.vo.sns.profile.ProfileMainScrapResponse;
import com.team6.onandthefarm.vo.sns.profile.ProfileMainWishResponse;
import com.team6.onandthefarm.vo.sns.profile.WishProductListResponse;
import com.team6.onandthefarm.vo.user.MemberProfileCountResponse;

import java.io.IOException;
import java.util.List;

public interface FeedService {

    Long uploadFeed(Long memberId, String memberRole, FeedInfoDto feedInfoDto) throws IOException;

    List<AddableProductResponse> findAddableProducts(Long memberId, String memberRole);

    FeedDetailResponse findFeedDetail(Long feedId, Long memberId);

    Long modifyFeed(Long memberId, FeedInfoDto feedInfoDto);

    Long deleteFeed(Long userId, Long feedId);

    Boolean upViewCount(Long feedId);

    Boolean upShareCount(Long feedId);

    List<FeedResponse> findByFeedTag(String feedTagName, Integer pageNumber, Long memberId);

    Boolean createFeedLike(Long feedId, Long userId);

    Boolean deleteFeedLike(Long feedId, Long memberId);

    Boolean createFeedScrap(Long feedId, Long userId);

    Boolean deleteFeedScrap(Long feedId, Long memberId);

    List<FeedResponse> findByRecentFeedList(Integer pageNumer, Long memberId);

    List<FeedResponse> findByLikeFeedList(Integer pageNumber, Long memberId);

    List<FeedResponse> findByFollowFeedList(Long memberId,Integer pageNumber);

    List<FeedResponse> findByViewCountFeedList(Integer pageNumber, Long memberId);

    List<ProfileMainFeedResponse> findByMemberFeedList(ProfileMainFeedDto profileMainFeedDto);

    List<ProfileMainScrapResponse> findByMemberScrapList(ProfileMainScrapDto profileMainScrapDto);

    List<ProfileMainWishResponse> findByMemberWishList(ProfileMainWishDto profileMainWishDto);

    List<WishProductListResponse> findByMemberWishDetailList(ProfileMainWishDto profileMainWishDto);

    List<FeedResponse> findByRecentFeedListAndMemberId(ProfileFeedDto profileFeedDto);

    List<FeedResponse> findByRecentScrapFeedListAndMemberId(ProfileFeedDto profileFeedDto);

    MemberProfileCountResponse getScrapLikeCount(MemberProfileDto memberProfileDto);

}
