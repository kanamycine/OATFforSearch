package com.team6.onandthefarm.entity.product;

import com.team6.onandthefarm.entity.user.User;
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
public class ProductQnaAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productQnaAnswerId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productQnaId")
    private ProductQna productQna;

    private String productQnaAnswerContent;

    private String productQnaAnswerCreatedAt;

    private String productQnaAnswerModifiedAt;
}
