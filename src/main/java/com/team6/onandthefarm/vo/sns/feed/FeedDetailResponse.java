package com.team6.onandthefarm.vo.sns.feed;

import com.team6.onandthefarm.entity.sns.FeedTag;
import com.team6.onandthefarm.vo.sns.feed.imageProduct.ImageInfo;
import com.team6.onandthefarm.vo.sns.feed.imageProduct.ImageProductInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class FeedDetailResponse {

    private Long memberId;

    private String memberRole;

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

    private List<ImageProductInfo> feedImageProductList;

    private List<FeedTag> feedTag;

    private Boolean isModifiable;

    public FeedDetailResponse(){
        this.isModifiable = false;
    }

}
