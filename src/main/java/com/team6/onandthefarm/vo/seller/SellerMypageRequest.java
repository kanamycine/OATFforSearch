package com.team6.onandthefarm.vo.seller;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerMypageRequest {
    private String startDate;
    private String endDate;
}
