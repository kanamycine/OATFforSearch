package com.team6.onandthefarm.entity;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Builder
@Slf4j
@Entity
@Table(name = "seller")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SellerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String email;
    private String password;
    private String zipcode;
    private String address;
    private String addressDetail;
    private String phone;
    private String name;
    private String shopName;
    private String businessNumber;
    private String registerDate;
    private boolean isActived;

}
