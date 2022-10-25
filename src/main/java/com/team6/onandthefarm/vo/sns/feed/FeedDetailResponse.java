package com.team6.onandthefarm.vo.sns.feed;

import com.team6.onandthefarm.entity.sns.FeedTag;
import com.team6.onandthefarm.vo.sns.feed.imageProduct.ImageInfo;
import com.team6.onandthefarm.vo.sns.feed.imageProduct.ImageProductResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedDetailResponse {

    private Long memberId;

    private String memberRole;

    private String memberName;

    private String memberProfileImg;

    private Long feedId;

    private String feedTitle;

    private String feedContent;

    private Integer feedViewCount;

    private Integer feedLikeCount;

    private Integer feedShareCount;

    private Integer feedScrapCount;

    private Integer feedCommentCount;

    private String feedCreateAt;

    private String feedUpdateAt;

    private List<ImageInfo> feedImageList;

    private List<ImageProductResponse> feedImageProductList;

    private List<FeedTag> feedTag;

    private Boolean feedLikeStatus;

    private Boolean scrapStatus;

    private Boolean isModifiable;

    private Boolean followStatus;

}
