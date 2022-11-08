package com.team6.onandthefarm.vo.exhibition;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExhibitionAccountDetailResponse {
    private Long exhibitionAccountId;

    private String exhibitionAccountName;

    private String exhibitionAccountDetail;

    private String exhibitionAccountStartTime;

    private String exhibitionAccountEndTime;

    private List<ExhibitionAccountItemsDetailResponse> exhibitionAccountItemsDetailResponseList;

}
