package com.team6.onandthefarm.repository.sns;

import com.team6.onandthefarm.entity.sns.Feed;
import com.team6.onandthefarm.entity.sns.FeedImage;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FeedImageRepository extends CrudRepository<FeedImage,Long> {
    List<FeedImage> findByFeed(Feed feed);
}
