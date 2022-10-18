package com.team6.onandthefarm.vo.sns.feed;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedResponse {
    private Long feedId;

    private Long memberId;

    private String memberRole;

    private String memberName;

    private String feedImageSrc;

    private Integer feedViewCount;

    private String feedTitle;

    private Integer feedLikeCount;

    private Integer feedShareCount;

    private Integer feedScrapCount;

    private Integer feedCommentCount;

    private String feedContent;

}
