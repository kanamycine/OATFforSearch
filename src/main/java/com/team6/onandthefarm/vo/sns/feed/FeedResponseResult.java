package com.team6.onandthefarm.vo.sns.feed;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedResponseResult {
    private List<FeedResponse> feedResponseList;

    private Integer currentPageNum;

    private Integer totalPageNum;
}
