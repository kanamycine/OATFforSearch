package com.team6.onandthefarm.vo.user;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserQnaUpdateRequest {
    private Long productQnaId;

    private String productQnaContent;
}
