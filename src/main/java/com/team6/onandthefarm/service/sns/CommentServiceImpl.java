package com.team6.onandthefarm.service.sns;

import com.team6.onandthefarm.dto.sns.CommentInfoDto;
import com.team6.onandthefarm.entity.seller.Seller;
import com.team6.onandthefarm.entity.sns.Feed;
import com.team6.onandthefarm.entity.sns.FeedComment;
import com.team6.onandthefarm.entity.user.User;
import com.team6.onandthefarm.repository.seller.SellerRepository;
import com.team6.onandthefarm.repository.sns.FeedCommentRepository;
import com.team6.onandthefarm.repository.sns.FeedRepository;
import com.team6.onandthefarm.repository.user.UserRepository;
import com.team6.onandthefarm.util.DateUtils;
import com.team6.onandthefarm.vo.sns.comment.CommentDetailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final FeedRepository feedRepository;
    private final FeedCommentRepository feedCommentRepository;
    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;

    private DateUtils dateUtils;
    Environment env;

    @Autowired
    public CommentServiceImpl(FeedRepository feedRepository,
                              FeedCommentRepository feedCommentRepository,
                              UserRepository userRepository,
                              SellerRepository sellerRepository,
                              DateUtils dateUtils,
                              Environment env){
        this.feedRepository = feedRepository;
        this.feedCommentRepository = feedCommentRepository;
        this.userRepository = userRepository;
        this.sellerRepository = sellerRepository;
        this.dateUtils = dateUtils;
        this.env = env;
    }

    @Override
    public List<CommentDetailResponse> findCommentDetail(Long feedId, Long memberId) {

        List<CommentDetailResponse> commentDetailList = new ArrayList<>();

        List<FeedComment> feedCommentList = feedCommentRepository.findByFeedId(feedId);
        for(FeedComment feedComment : feedCommentList){

            CommentDetailResponse commentDetail = CommentDetailResponse.builder()
                    .memberId(feedComment.getMemberId())
                    .memberRole(feedComment.getMemberRole())
                    .feedCommentId(feedComment.getFeedCommnetId())
                    .feedCommentContent(feedComment.getFeedCommentContent())
                    .feedCommentCreateAt(feedComment.getFeedCommentCreateAt())
                    .feedCommentModifiedAt(feedComment.getFeedCommentModifiedAt())
                    .isModifiable(false)
                    .build();

            if(feedComment.getMemberId().equals(memberId)){
                commentDetail.setIsModifiable(true);
            }

            if(feedComment.getMemberRole().equals("user")){
                Optional<User> user = userRepository.findById(feedComment.getMemberId());
                commentDetail.setMemberName(user.get().getUserName());
                commentDetail.setMemberProfileImg(user.get().getUserProfileImg());
            }
            else if(feedComment.getMemberRole().equals("seller")){
                Optional<Seller> seller = sellerRepository.findById(feedComment.getMemberId());
                commentDetail.setMemberName(seller.get().getSellerName());
                commentDetail.setMemberProfileImg(seller.get().getSellerProfileImg());
            }

            commentDetailList.add(commentDetail);
        }

        return commentDetailList;
    }

    @Override
    public Long addComment(CommentInfoDto commentInfoDto) {

        Optional<Feed> feed = feedRepository.findById(commentInfoDto.getFeedId());
        feed.get().setFeedCommentCount(feed.get().getFeedCommentCount()+1);

        FeedComment feedComment = new FeedComment();
        feedComment.setMemberId(commentInfoDto.getMemberId());
        feedComment.setMemberRole(commentInfoDto.getMemberRole());
        feedComment.setFeed(feed.get());
        feedComment.setFeedCommentContent(commentInfoDto.getFeedCommentContent());
        feedComment.setFeedCommentCreateAt(dateUtils.transDate(env.getProperty("dateutils.format")));
        feedComment.setFeedCommentStatus(true);

        FeedComment savedFeedComment = feedCommentRepository.save(feedComment);
        return savedFeedComment.getFeedCommnetId();
    }

    @Override
    public Long modifyComment(CommentInfoDto commentInfoDto) {

        Optional<FeedComment> feedComment = feedCommentRepository.findById(commentInfoDto.getFeedCommentId());

        if(feedComment.isPresent()){
            if(feedComment.get().getMemberId() == commentInfoDto.getMemberId()) {
                feedComment.get().setFeedCommentContent(commentInfoDto.getFeedCommentContent());
                feedComment.get().setFeedCommentModifiedAt(dateUtils.transDate(env.getProperty("dateutils.format")));

                return feedComment.get().getFeedCommnetId();
            }
        }

        return null;
    }

    @Override
    public Long deleteComment(CommentInfoDto commentInfoDto) {

        Optional<FeedComment> feedComment = feedCommentRepository.findById(commentInfoDto.getFeedCommentId());

        if(feedComment.isPresent()){
            if(feedComment.get().getMemberId().equals(commentInfoDto.getMemberId())) {
                feedComment.get().setFeedCommentStatus(false);

                return feedComment.get().getFeedCommnetId();
            }
        }
        return null;
    }
}
