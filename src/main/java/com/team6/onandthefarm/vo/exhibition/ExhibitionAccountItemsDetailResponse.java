package com.team6.onandthefarm.vo.exhibition;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 아이템 리스트 상세정보
public class ExhibitionAccountItemsDetailResponse {
    private Long exhibitionItemsId;

    private String exhibitionItemsName;

    private String exhibitionItemsDetail;

    private List<ExhibitionAccountItemDetailResponse> exhibitionAccountItemDetailResponseList;
}
