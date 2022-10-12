package com.team6.onandthefarm.vo.sns.feed;

import lombok.Data;

import java.util.List;

@Data
public class FeedUploadRequest {

    private String feedTitle;
    private String feedContent;
    private List<String> feedTag;

}
