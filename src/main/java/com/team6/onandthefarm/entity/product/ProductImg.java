package com.team6.onandthefarm.entity.product;

import com.team6.onandthefarm.entity.seller.Seller;
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
public class ProductImg {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productImgId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId")
    private Product product;

    private String productImgSrc;

    @Builder
    public ProductImg(String productImgSrc, Product product){
        this.productImgSrc = productImgSrc;
        this.product = product;
    }

    public void updateProductImg(String productImgSrc) {
        this.productImgSrc = productImgSrc;
    }
}
