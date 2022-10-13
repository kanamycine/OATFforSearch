package com.team6.onandthefarm.service.sns;

import com.team6.onandthefarm.dto.sns.CommentInfoDto;
import com.team6.onandthefarm.vo.sns.comment.CommentDetailResponse;

import java.util.List;

public interface CommentService {

    List<CommentDetailResponse> findCommentDetail(Long feedId, Long memberId);

    Long addComment(CommentInfoDto commentInfoDto);

    Long modifyComment(CommentInfoDto commentInfoDto);

    Long deleteComment(CommentInfoDto commentInfoDto);
}
