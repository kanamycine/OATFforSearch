package com.team6.onandthefarm.dto.order;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSellerFindDto {
    private String sellerId;

    private String startDate;

    private String endDate;

    private Integer pageNumber;
}
