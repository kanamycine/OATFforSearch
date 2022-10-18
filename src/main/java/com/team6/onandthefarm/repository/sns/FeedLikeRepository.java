package com.team6.onandthefarm.repository.sns;

import com.team6.onandthefarm.entity.sns.Feed;
import com.team6.onandthefarm.entity.sns.FeedLike;
import org.springframework.data.repository.CrudRepository;

public interface FeedLikeRepository extends CrudRepository<FeedLike,Long> {
    FeedLike findByFeedAndMemberId(Feed feed,Long memberId);
}
