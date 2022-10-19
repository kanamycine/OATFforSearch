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
@SequenceGenerator(
        name="PRODUCT_QNA_ANSWER_SEQ_GENERATOR",
        sequenceName = "PRODUCT_QNA_ANSWER_SEQ",
        initialValue = 100000, allocationSize = 1
)
public class ProductQnaAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "PRODUCT_QNA_ANSWER_SEQ_GENERATOR")
    private Long productQnaAnswerId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productQnaId")
    private ProductQna productQna;

    private String productQnaAnswerContent;

    private String productQnaAnswerCreatedAt;

    private String productQnaAnswerModifiedAt;
}
