package com.team6.onandthefarm.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmailDto {
    private String email;
    private String authKey;
    private String date;
}
