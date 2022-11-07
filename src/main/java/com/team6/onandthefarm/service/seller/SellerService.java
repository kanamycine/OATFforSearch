package com.team6.onandthefarm.service.seller;

import com.team6.onandthefarm.dto.seller.SellerDto;
import com.team6.onandthefarm.dto.seller.SellerMypageDto;
import com.team6.onandthefarm.dto.seller.SellerQnaDto;
import com.team6.onandthefarm.entity.product.ProductQna;
import com.team6.onandthefarm.security.jwt.Token;
import com.team6.onandthefarm.vo.seller.SellerInfoResponse;
import com.team6.onandthefarm.vo.seller.SellerMypageResponse;
import com.team6.onandthefarm.vo.seller.SellerProductQnaResponse;
import com.team6.onandthefarm.vo.seller.SellerProductQnaResponseResult;

import java.io.IOException;
import java.util.List;

public interface SellerService {
    boolean updateByUserId(Long userId, SellerDto sellerDto) throws IOException;
    SellerInfoResponse findByUserId(Long userId);
    Boolean updatePassword(SellerDto sellerDto);
    boolean sellerSignup(SellerDto sellerDto);
    boolean sellerIdCheck(String sellerEmail);

    String searchSellerId(String name, String phone);
    Boolean searchSellerpasswd(String sellerEmail,String name);
    SellerProductQnaResponseResult findSellerQnA(Long sellerId, Integer pageNumber);
    Boolean createQnaAnswer(SellerQnaDto sellerQnaDto);
    Token login(SellerDto sellerDto);
    SellerMypageResponse findSellerMypage(SellerMypageDto sellerMypageDto);
}
