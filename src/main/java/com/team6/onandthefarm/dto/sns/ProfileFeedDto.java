package com.team6.onandthefarm.dto.sns;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileFeedDto {
    private Long memberId;
    private Integer pageNumber;
}
