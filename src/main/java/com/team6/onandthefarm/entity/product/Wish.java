package com.team6.onandthefarm.entity.product;

import com.team6.onandthefarm.entity.product.Product;
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
        name="WISH_SEQ_GENERATOR",
        sequenceName = "WISH_SEQ",
        initialValue = 100000, allocationSize = 1
)
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "WISH_SEQ_GENERATOR")
    private Long wishId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    private Boolean wishStatus;
}
