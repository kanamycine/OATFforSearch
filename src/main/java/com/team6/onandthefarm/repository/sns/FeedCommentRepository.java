package com.team6.onandthefarm.repository.sns;

import com.team6.onandthefarm.entity.sns.FeedComment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedCommentRepository extends CrudRepository<FeedComment,Long> {

    @Query("select c from FeedComment c where c.feed.feedId=:feedId and c.feedCommentStatus=true ORDER BY c.feedCommentCreateAt DESC")
    List<FeedComment> findByFeedId(@Param("feedId") Long feedId);
}
