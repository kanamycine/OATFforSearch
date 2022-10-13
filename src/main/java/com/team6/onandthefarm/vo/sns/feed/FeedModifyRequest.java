package com.team6.onandthefarm.vo.sns.feed;

import lombok.Data;

import java.util.List;

@Data
public class FeedModifyRequest {

    private Long feedId;
    private String feedTitle;
    private String feedContent;
    private List<String> feedTag;
    private List<Long> deleteImg;
}
