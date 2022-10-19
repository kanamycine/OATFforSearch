package com.team6.onandthefarm.entity.product;

import com.team6.onandthefarm.entity.seller.Seller;
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
        name="PRODUCT_QNA_SEQ_GENERATOR",
        sequenceName = "PRODUCT_QNA_SEQ",
        initialValue = 100000, allocationSize = 1
)
public class ProductQna {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "PRODUCT_QNA_SEQ_GENERATOR")
    private Long productQnaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sellerId")
    private Seller seller;

    private String productQnaContent;

    private String productQnaCreatedAt;

    private String productQnaModifiedAt;

    private String productQnaStatus;

}
