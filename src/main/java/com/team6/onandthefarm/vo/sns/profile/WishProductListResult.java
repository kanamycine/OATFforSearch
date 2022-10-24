package com.team6.onandthefarm.vo.sns.profile;

import com.team6.onandthefarm.vo.sns.feed.FeedResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WishProductListResult {

    private List<WishProductListResponse> wishProductListResponse;

    private Integer currentPageNum;

    private Integer totalPageNum;

    private Integer totalElementNum;
}
