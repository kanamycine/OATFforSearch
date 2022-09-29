package com.team6.onandthefarm.dto.order;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderUserFindDto {
    private String userId;

    private String startDate;

    private String endDate;

    private Integer pageNumber;
}
