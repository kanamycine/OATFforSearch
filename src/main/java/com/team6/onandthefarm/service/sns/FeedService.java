package com.team6.onandthefarm.service.sns;


import com.team6.onandthefarm.dto.sns.*;
import com.team6.onandthefarm.dto.user.MemberProfileDto;
import com.team6.onandthefarm.vo.sns.feed.AddableProductResponse;
import com.team6.onandthefarm.vo.sns.feed.FeedDetailResponse;
import com.team6.onandthefarm.vo.sns.feed.FeedResponse;
import com.team6.onandthefarm.vo.sns.feed.FeedResponseResult;
import com.team6.onandthefarm.vo.sns.profile.*;
import com.team6.onandthefarm.vo.user.MemberProfileCountResponse;

import java.io.IOException;
import java.util.List;

public interface FeedService {

    Long uploadFeed(Long memberId, String memberRole, FeedInfoDto feedInfoDto) throws IOException;

    List<AddableProductResponse> findAddableProducts(Long memberId, String memberRole);

    FeedDetailResponse findFeedDetail(Long feedId, Long loginMemberId);

    Long modifyFeed(Long memberId, FeedInfoDto feedInfoDto);

    Long deleteFeed(Long userId, Long feedId);

    Boolean upViewCount(Long feedId);

    Boolean upShareCount(Long feedId);

    FeedResponseResult findByFeedTag(String feedTagName, Integer pageNumber, Long loginMemberId);

    Boolean createFeedLike(Long feedId, Long userId);

    Boolean deleteFeedLike(Long feedId, Long memberId);

    Boolean createFeedScrap(Long feedId, Long userId);

    Boolean deleteFeedScrap(Long feedId, Long memberId);

    FeedResponseResult findByRecentFeedList(Integer pageNumer, Long loginMemberId);

    FeedResponseResult findByLikeFeedList(Integer pageNumber, Long loginMemberId);

    FeedResponseResult findByFollowFeedList(Long loginMemberId, Integer pageNumber);

    FeedResponseResult findByViewCountFeedList(Integer pageNumber, Long memberId);

    List<ProfileMainFeedResponse> findByMemberFeedList(ProfileMainFeedDto profileMainFeedDto);

    List<ProfileMainScrapResponse> findByMemberScrapList(ProfileMainScrapDto profileMainScrapDto);

    List<ProfileMainWishResponse> findByMemberWishList(ProfileMainWishDto profileMainWishDto);

    WishProductListResult findByMemberWishDetailList(ProfileMainWishDto profileMainWishDto);

    FeedResponseResult findByRecentFeedListAndMemberId(ProfileFeedDto profileFeedDto);

    FeedResponseResult findByRecentScrapFeedListAndMemberId(ProfileFeedDto profileFeedDto);

    MemberProfileCountResponse getFeedScrapWishCount(MemberProfileDto memberProfileDto);

}
