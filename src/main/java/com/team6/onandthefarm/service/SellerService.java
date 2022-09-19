package com.team6.onandthefarm.service;

import com.team6.onandthefarm.dto.SellerDto;
import com.team6.onandthefarm.entity.SellerEntity;
import com.team6.onandthefarm.repository.SellerRepository;
import com.team6.onandthefarm.util.DateUtils;
import com.team6.onandthefarm.vo.SellerInfoResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
public class SellerService {

    private SellerRepository sellerRepository;

    private DateUtils dateUtils;


    @Autowired
    public SellerService(SellerRepository sellerRepository,DateUtils dateUtils) {
        this.sellerRepository = sellerRepository;
        this.dateUtils=dateUtils;
    }

    public boolean updateByUserId(Long userId,SellerDto sellerDto){
        Optional<SellerEntity> sellerEntity = sellerRepository.findById(userId);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        sellerEntity.get().setZipcode(sellerDto.getZipcode());
        sellerEntity.get().setAddress(sellerDto.getAddress());
        sellerEntity.get().setAddressDetail(sellerDto.getAddressDetail());
        sellerEntity.get().setShopName(sellerDto.getShopName());
        sellerEntity.get().setPassword(sellerDto.getPassword());
        sellerEntity.get().setPhone(sellerDto.getPhone());
        sellerRepository.save(sellerEntity.get());
        return true;
    }

    /**
     * 셀러 유저의 정보를 조회하는 메서드
     * @param userId
     * @return
     */
    public SellerInfoResponse findByUserId(Long userId){
        Optional<SellerEntity> sellerEntity = sellerRepository.findById(userId);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        SellerInfoResponse response = modelMapper.map(sellerEntity.get(),SellerInfoResponse.class);
        return response;
    }

    /**
     * 회원의 비밀번호값을 변경해주는 메서드
     * @param sellerDto
     */
    public void updatePassword(SellerDto sellerDto){
        SellerEntity seller = sellerRepository.findByEmail(sellerDto.getEmail());
        seller.setPassword(sellerDto.getPassword());
        sellerRepository.save(seller);
    }

    /**
     *
     * @param sellerDto
     * @return true: 회원가입 됨  false: 회원가입 실패
     */
    public boolean sellerSignup(SellerDto sellerDto){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        SellerEntity sellerEntity = modelMapper.map(sellerDto,SellerEntity.class);
        String date = dateUtils.transDate("yyyy.MM.dd HH:mm:ss");
        sellerEntity.setRegisterDate(date);
        sellerRepository.save(sellerEntity);
        return true;
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
