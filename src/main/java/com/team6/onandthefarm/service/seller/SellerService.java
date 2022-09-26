package com.team6.onandthefarm.service.seller;

import com.team6.onandthefarm.dto.seller.SellerDto;
import com.team6.onandthefarm.entity.seller.Seller;
import com.team6.onandthefarm.repository.seller.SellerRepository;
import com.team6.onandthefarm.util.DateUtils;
import com.team6.onandthefarm.vo.seller.SellerInfoResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class SellerService {

    private SellerRepository sellerRepository;

    private DateUtils dateUtils;

    private Environment env;


    @Autowired
    public SellerService(SellerRepository sellerRepository,DateUtils dateUtils,Environment env) {
        this.sellerRepository = sellerRepository;
        this.dateUtils=dateUtils;
        this.env=env;
    }

    public boolean updateByUserId(Long userId,SellerDto sellerDto){
        Optional<Seller> sellerEntity = sellerRepository.findById(userId);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        sellerEntity.get().setSellerZipcode(sellerDto.getZipcode());
        sellerEntity.get().setSellerAddress(sellerDto.getAddress());
        sellerEntity.get().setSellerAddressDetail(sellerDto.getAddressDetail());
        sellerEntity.get().setSellerShopName(sellerDto.getShopName());
        sellerEntity.get().setSellerPassword(sellerDto.getPassword());
        sellerEntity.get().setSellerPhone(sellerDto.getPhone());
        return true;
    }

    /**
     * 셀러 유저의 정보를 조회하는 메서드
     * @param userId
     * @return
     */
    public SellerInfoResponse findByUserId(Long userId){
        Optional<Seller> sellerEntity = sellerRepository.findById(userId);
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
        Seller seller = sellerRepository.findBySellerEmail(sellerDto.getEmail());
        seller.setSellerPassword(sellerDto.getPassword());
    }

    /**
     *
     * @param sellerDto
     * @return true: 회원가입 됨  false: 회원가입 실패
     */
    public boolean sellerSignup(SellerDto sellerDto){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Seller seller = modelMapper.map(sellerDto, Seller.class);
        String date = dateUtils.transDate(env.getProperty("dateutils.format"));
        seller.setSellerRegisterDate(date);
        sellerRepository.save(seller);
        return true;
    }

    /**
     *
     * @param sellerEmail
     * @return true: 중복안됨 / false: 중복됨
     */
    public boolean sellerIdCheck(String sellerEmail){
        Seller email = sellerRepository.findBySellerEmail(sellerEmail);
        if(email == null){
            return true;
        }
        return false;
    }
}
