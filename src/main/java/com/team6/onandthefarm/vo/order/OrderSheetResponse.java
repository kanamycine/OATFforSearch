package com.team6.onandthefarm.vo.order;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSheetResponse {
    private Long productId;

    private Long sellerId;

    private String productName;

    private String productImg;

    private Integer productPrice;

    private Integer productTotalPrice;

    private Integer productQty;

    private String userName;

    private String userAddress;

    private String userPhone;

}
