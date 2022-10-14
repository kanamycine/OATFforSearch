package com.team6.onandthefarm.service.sns;


import com.team6.onandthefarm.dto.sns.*;
import com.team6.onandthefarm.vo.sns.feed.AddableProductResponse;
import com.team6.onandthefarm.vo.sns.feed.FeedDetailResponse;
import com.team6.onandthefarm.vo.sns.feed.FeedResponse;
import com.team6.onandthefarm.vo.sns.profile.ProfileMainFeedResponse;
import com.team6.onandthefarm.vo.sns.profile.ProfileMainScrapResponse;
import com.team6.onandthefarm.vo.sns.profile.ProfileMainWishResponse;
import com.team6.onandthefarm.vo.sns.profile.WishProductListResponse;

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

    Boolean createFeedLike(Long feedId, Long userId);

    Boolean createFeedScrap(Long feedId, Long userId);

    List<FeedResponse> findByRecentFeedList(Integer pageNumer);

    List<FeedResponse> findByLikeFeedList(Integer pageNumber);

    List<FeedResponse> findByFollowFeedList(Long memberId,Integer pageNumber);

    List<FeedResponse> findByViewCountFeedList(Integer pageNumber);

    List<ProfileMainFeedResponse> findByMemberFeedList(ProfileMainFeedDto profileMainFeedDto);

    List<ProfileMainScrapResponse> findByMemberScrapList(ProfileMainScrapDto profileMainScrapDto);

    List<ProfileMainWishResponse> findByMemberWishList(ProfileMainWishDto profileMainWishDto);

    List<WishProductListResponse> findByMemberWishDetailList(ProfileMainWishDto profileMainWishDto);

    List<FeedResponse> findByRecentFeedListAndMemberId(ProfileFeedDto profileFeedDto);

    List<FeedResponse> findByRecentScrapFeedListAndMemberId(ProfileFeedDto profileFeedDto);

}
