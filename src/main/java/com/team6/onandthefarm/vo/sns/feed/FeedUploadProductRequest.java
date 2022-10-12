package com.team6.onandthefarm.vo.sns.feed;

import com.team6.onandthefarm.vo.sns.feed.imageProduct.ImageProductInfo;
import lombok.Data;

import java.util.List;

@Data
public class FeedUploadProductRequest {

    private List<ImageProductInfo> feedProductIdList;
}
