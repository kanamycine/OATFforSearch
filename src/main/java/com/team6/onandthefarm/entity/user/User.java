package com.team6.onandthefarm.entity.user;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Builder
@Slf4j
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    private String userEmail;

    //private String userPassword;

    private String userZipcode;

    private String userAddress;

    private String userAddressDetail;

    private String userPhone;

    private String userBirthday;

    private Integer userSex;

    private String userName;

    private String userRegisterDate;

    private Boolean userIsActivated;

    private String role;

    private String provider;

    private Long userKakaoNumber;

    private String userNaverNumber;

    private String userAppleNumber;

    private String userGoogleNumber;

}
