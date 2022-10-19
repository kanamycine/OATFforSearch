package com.team6.onandthefarm.entity.cart;

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
        name="CART_SEQ_GENERATOR",
        sequenceName = "CART_SEQ",
        initialValue = 100000, allocationSize = 1
)
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "CART_SEQ_GENERATOR")
    private Long cartId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    private Integer cartQty;
    private Boolean cartIsActivated;
    private Boolean cartStatus;
    private String cartCreatedAt;
}
