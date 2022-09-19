package com.team6.onandthefarm.service;

import com.team6.onandthefarm.dto.SellerDto;
import com.team6.onandthefarm.entity.SellerEntity;
import com.team6.onandthefarm.repository.SellerRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerService {

    private SellerRepository sellerRepository;

    private ModelMapper modelMapper;

    @Autowired
    public SellerService(SellerRepository sellerRepository, ModelMapper modelMapper) {
        this.sellerRepository = sellerRepository;
        this.modelMapper=modelMapper;
    }

    /**
     *
     * @param sellerDto
     * @return true: 회원가입 됨  false: 회원가입 실패
     */
    public boolean sellerSignup(SellerDto sellerDto){
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        SellerEntity sellerEntity = modelMapper.map(sellerDto,SellerEntity.class);
        boolean emailCheck = sellerIdCheck(sellerEntity.getEmail());
        return emailCheck;
    }

    /**
     *
     * @param sellerEmail
     * @return true: 중복안됨 / false: 중복됨
     */
    public boolean sellerIdCheck(String sellerEmail){
        SellerEntity email = sellerRepository.findByEmail(sellerEmail);
        if(email == null){
            return true;
        }
        return false;
    }
}
