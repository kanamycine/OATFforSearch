package com.team6.onandthefarm.entity.sns;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Builder
@Slf4j
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Feed {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long feedId;

    private Long memberId;

    private String memberRole;

    private String feedTitle;

    private String feedContent;

    private Integer feedViewCount;

    private Integer feedLikeCount;

    private String feedCreateAt;

    private String feedUpdateAt;

    private Integer feedShareCount;

    private Boolean feedStatus; // true : 피드게시중 / false : 피드 삭제

    private Integer feedScrapCount;

    @Column(length=1000)
    private Integer feedCommentCount;

}
