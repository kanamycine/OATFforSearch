package com.team6.onandthefarm.vo.seller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SellerInfoResponse {
    private String email;
    private String zipcode;
    private String address;
    private String addressDetail;
    private String phone;
    private String name;
    private String businessNumber;
    private String registerDate;
}
