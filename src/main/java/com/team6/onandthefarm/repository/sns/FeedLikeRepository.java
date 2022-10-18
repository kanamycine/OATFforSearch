package com.team6.onandthefarm.repository.sns;

import com.team6.onandthefarm.entity.sns.Feed;
import com.team6.onandthefarm.entity.sns.FeedLike;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FeedLikeRepository extends CrudRepository<FeedLike,Long> {
    Optional<FeedLike> findByFeedAndMemberId(Feed feed,Long memberId);

}
