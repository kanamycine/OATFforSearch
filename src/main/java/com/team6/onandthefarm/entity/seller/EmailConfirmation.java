package com.team6.onandthefarm.entity.seller;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class EmailConfirmation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long confirmId;
    private String emailId;
    private String authKey;
    private String confirmDate;
}
