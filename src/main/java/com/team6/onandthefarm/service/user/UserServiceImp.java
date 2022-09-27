package com.team6.onandthefarm.service.user;

import com.team6.onandthefarm.dto.user.UserQnaDto;
import com.team6.onandthefarm.entity.product.Product;
import com.team6.onandthefarm.entity.product.ProductQna;
import com.team6.onandthefarm.entity.seller.Seller;
import com.team6.onandthefarm.entity.user.User;
import com.team6.onandthefarm.repository.category.CategoryRepository;
import com.team6.onandthefarm.repository.product.ProductQnaRepository;
import com.team6.onandthefarm.repository.product.ProductRepository;
import com.team6.onandthefarm.repository.user.UserRepository;
import com.team6.onandthefarm.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@Slf4j
public class UserServiceImp implements UserService{

    private UserRepository userRepository;

    private ProductQnaRepository productQnaRepository;

    private ProductRepository productRepository;

    private DateUtils dateUtils;

    private Environment env;

    @Autowired
    public UserServiceImp(UserRepository userRepository,
                          DateUtils dateUtils,
                          Environment env,
                          ProductQnaRepository productQnaRepository,
                          ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.dateUtils = dateUtils;
        this.env = env;
        this.productQnaRepository=productQnaRepository;
        this.productRepository=productRepository;
    }

    public Boolean createProductQnA(UserQnaDto userQnaDto){
        Optional<User> user = userRepository.findById(userQnaDto.getUserId());
        Optional<Product> product = productRepository.findById(userQnaDto.getProductId());
        log.info("product 정보  :  "+product.get().toString());
        ProductQna productQna = ProductQna.builder()
                .product(product.get())
                .user(user.get())
                .productQnaContent(userQnaDto.getProductQnaContent())
                .productQnaCreatedAt(dateUtils.transDate(env.getProperty("dateutils.format")))
                .productQnaStatus("qna0")
                .seller(product.get().getSeller())
                .build();
        ProductQna newQna = productQnaRepository.save(productQna);
        if(newQna==null){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
