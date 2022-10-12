package com.team6.onandthefarm.repository.sns;

import com.team6.onandthefarm.entity.sns.FeedImage;
import com.team6.onandthefarm.entity.sns.FeedImageProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedImageProductRepository extends JpaRepository<FeedImageProduct, Long> {

    List<FeedImageProduct> findByFeedImage(FeedImage feedImage);
}
