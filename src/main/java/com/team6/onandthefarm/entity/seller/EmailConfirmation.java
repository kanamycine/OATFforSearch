package com.team6.onandthefarm.entity.seller;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailConfirmation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long confirmId;
    private String emailId;
    private String authKey;
    private String confirmDate;
}
