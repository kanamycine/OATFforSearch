package com.team6.onandthefarm.dto.sns;

import com.team6.onandthefarm.vo.sns.feed.imageProduct.ImageProductInfo;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class FeedInfoDto {

    private Long feedId;

    private String feedTitle;

    private String feedContent;

    private List<String> feedTag;

    private List<MultipartFile> feedImgSrcList;

    private List<ImageProductInfo> feedProductIdList;

    private List<Long> deleteImg;

}
