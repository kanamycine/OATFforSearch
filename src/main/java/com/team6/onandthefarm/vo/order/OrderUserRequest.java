package com.team6.onandthefarm.vo.order;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderUserRequest {
    private String userId;

    private String startDate;

    private String endDate;

    private Integer pageNumber;
}
