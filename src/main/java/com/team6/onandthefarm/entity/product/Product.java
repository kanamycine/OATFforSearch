package com.team6.onandthefarm.entity.product;

import com.team6.onandthefarm.entity.category.Category;
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
@ToString
public class Product{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sellerId")
    private Seller seller;

    private String productName;

    private Integer productPrice;

    private Integer productTotalStock;

    private String productMainImgSrc;

    private String productDetail;

    private String productDetailShort;

    private String productOriginPlace;

    private String productDeliveryCompany;

    private String productRegisterDate;

    private String productUpdateDate;

    private String productStatus;

    private Integer productWishCount;

    private Integer productSoldCount;

    public Long updateProduct(Category category, String productName, Integer productPrice,
            Integer productTotalStock, String productMainImgSrc, String productDetail,
            String productDetailShort, String productOriginPlace, String productDeliveryCompany,
            String productStatus, Integer productWishCount, Integer productSoldCount){

        this.category = category;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productTotalStock = productTotalStock;
        this.productMainImgSrc = productMainImgSrc;
        this.productDetail = productDetail;
        this.productDetailShort = productDetailShort;
        this.productOriginPlace = productOriginPlace;
        this.productDeliveryCompany = productDeliveryCompany;
        this.productStatus = productStatus;
        this.productWishCount = productWishCount;
        this.productSoldCount = productSoldCount;

        return productId;
    }
}
