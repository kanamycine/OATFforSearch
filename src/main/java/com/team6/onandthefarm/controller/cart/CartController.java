package com.team6.onandthefarm.controller.cart;

import com.team6.onandthefarm.dto.cart.CartDto;
import com.team6.onandthefarm.dto.cart.CartIsActivatedDto;
import com.team6.onandthefarm.dto.product.ProductFormDto;
import com.team6.onandthefarm.service.cart.CartService;
import com.team6.onandthefarm.util.BaseResponse;
import com.team6.onandthefarm.vo.cart.CartIsActivatedRequest;
import com.team6.onandthefarm.vo.cart.CartRequest;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService){
        this.cartService = cartService;
    }

    @PostMapping
    @ApiOperation(value = "장바구니 추가")
    public ResponseEntity<BaseResponse> addCart(@RequestBody CartRequest cartRequest){

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        CartDto cartDto = modelMapper.map(cartRequest, CartDto.class);

        Long cartId = cartService.addCart(cartDto);

        BaseResponse baseResponse = BaseResponse.builder()
                .httpStatus(HttpStatus.CREATED)
                .message("cart add completed")
                .data(cartId)
                .build();

        if(cartId == null){
            baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("cart add failed")
                    .build();
        }

        return new ResponseEntity(baseResponse, HttpStatus.CREATED);
    }

    @PutMapping
    @ApiOperation(value = "장바구니 유지 여부 수정")
    public ResponseEntity<BaseResponse> updateCartIsActivated(@RequestBody CartIsActivatedRequest cartIsActivatedRequest){

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        CartIsActivatedDto cartIsActivatedDto = modelMapper.map(cartIsActivatedRequest, CartIsActivatedDto.class);

        Long cartId = cartService.updateCartIsActivated(cartIsActivatedDto);

        BaseResponse baseResponse = BaseResponse.builder()
                .httpStatus(HttpStatus.CREATED)
                .message("cartIsActivated update completed")
                .data(cartId)
                .build();

        return new ResponseEntity(baseResponse, HttpStatus.CREATED);
    }

}
