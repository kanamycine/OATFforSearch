package com.team6.onandthefarm.repository;


import com.team6.onandthefarm.entity.EmailEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailRepository extends CrudRepository<EmailEntity,Long> {

    /**
     * 모든 이메일 불러오기
     * @param email : 이메일주소
     * @return
     */
    List<EmailEntity> findAllByEmail(String email);

    /**
     * 주어진 이메일과 인증키와 알맞는 값을 주는 메서드
     * @param email : 이메일 주소
     * @param AuthKey : 인증 키 값
     * @return
     */
    EmailEntity findByEmailAndAuthKey(String email, String AuthKey);

    /**
     * 이메일 최근 1건을 불러오는 메서드
     * @param email : 이메일 주소
     * @return
     */
    EmailEntity findTopByEmailOrderByDateDesc(String email);
}
