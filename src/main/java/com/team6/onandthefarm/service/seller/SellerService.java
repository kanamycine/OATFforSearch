package com.team6.onandthefarm.service.seller;

import com.team6.onandthefarm.dto.seller.SellerDto;
import com.team6.onandthefarm.dto.seller.SellerQnaDto;
import com.team6.onandthefarm.entity.product.ProductQna;
import com.team6.onandthefarm.vo.seller.SellerInfoResponse;
import com.team6.onandthefarm.vo.seller.SellerProductQnaResponse;

import java.util.List;

public interface SellerService {
    boolean updateByUserId(Long userId, SellerDto sellerDto);
    SellerInfoResponse findByUserId(Long userId);
    void updatePassword(SellerDto sellerDto);
    boolean sellerSignup(SellerDto sellerDto);
    boolean sellerIdCheck(String sellerEmail);
    List<SellerProductQnaResponse> findSellerQnA(Long sellerId);
    Boolean createQnaAnswer(SellerQnaDto sellerQnaDto);
}
