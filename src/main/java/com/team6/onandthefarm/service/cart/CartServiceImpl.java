package com.team6.onandthefarm.service.cart;

import com.team6.onandthefarm.dto.cart.CartDto;
import com.team6.onandthefarm.entity.cart.Cart;
import com.team6.onandthefarm.entity.product.Product;
import com.team6.onandthefarm.repository.cart.CartRepository;
import com.team6.onandthefarm.repository.product.ProductRepository;
import com.team6.onandthefarm.util.DateUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final DateUtils dateUtils;
    private final Environment env;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository, DateUtils dateUtils, Environment env){
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.dateUtils = dateUtils;
        this.env = env;
    }

    /**
     * 장바구니에 추가하는 메소드
     * @param cartDto
     * @return cartId
     */
    @Override
    public Long addCart(CartDto cartDto) {

        Optional<Product> product = productRepository.findById(cartDto.getProductId());

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        //현재 로그인한 사용자 추가해야함!
        Cart cart = modelMapper.map(cartDto, Cart.class);
        cart.setProduct(product.get());
        cart.setCartStatus(true);
        cart.setCartIsActivated(false);
        cart.setCartCreatedAt(dateUtils.transDate(env.getProperty("dateutils.format")));

        Cart savedCart = cartRepository.save(cart);

        return savedCart.getCartId();
    }
}
