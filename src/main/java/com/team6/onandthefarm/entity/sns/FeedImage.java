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
public class FeedImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long feedImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedId")
    private Feed feed;

    @Column(length=1000)
    private String feedImageSrc;
}
