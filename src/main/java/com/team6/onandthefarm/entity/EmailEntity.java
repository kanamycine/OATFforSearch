package com.team6.onandthefarm.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "email_confirmation")
@Getter
@Setter
public class EmailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String email;
    private String authKey;
    private String date;
}
