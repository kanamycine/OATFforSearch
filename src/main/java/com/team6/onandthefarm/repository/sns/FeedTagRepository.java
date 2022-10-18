package com.team6.onandthefarm.repository.sns;

import com.team6.onandthefarm.entity.sns.Feed;
import com.team6.onandthefarm.entity.sns.FeedTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedTagRepository extends JpaRepository<FeedTag, Long> {

    List<FeedTag> findByFeed(Feed feed);

    List<FeedTag> findByFeedTagName(String feedTagName);
}
