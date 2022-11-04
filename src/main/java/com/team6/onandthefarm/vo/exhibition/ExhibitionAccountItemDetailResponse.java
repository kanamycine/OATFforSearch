package com.team6.onandthefarm.vo.exhibition;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 아이템 상세정보
public class ExhibitionAccountItemDetailResponse {
    private Long exhibitionItemId;

    private Long exhibitionItemProductId;

    private Integer exhibitionItemPriority;
}
