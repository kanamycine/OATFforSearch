package com.team6.onandthefarm.dto.order;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDeliveryDto {
    private String orderSerial;

    private String orderDeliveryCompany;

    private String orderDeliveryWaybillNumber;
}
