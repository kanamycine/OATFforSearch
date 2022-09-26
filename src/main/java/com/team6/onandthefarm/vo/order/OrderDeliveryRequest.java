package com.team6.onandthefarm.vo.order;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDeliveryRequest {
    private String orderSerial;

    private String orderDeliveryCompany;

    private String orderDeliveryWaybillNumber;

}
