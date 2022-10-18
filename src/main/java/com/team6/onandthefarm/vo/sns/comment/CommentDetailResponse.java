package com.team6.onandthefarm.vo.sns.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class CommentDetailResponse {

    private Long memberId;

    private String memberRole;
    
    private String memberName;

    private String memberProfileImg;

    private Long feedCommentId;

    private String feedCommentContent;

    private String feedCommentCreateAt;

    private String feedCommentModifiedAt;

    private Boolean isModifiable;

}
