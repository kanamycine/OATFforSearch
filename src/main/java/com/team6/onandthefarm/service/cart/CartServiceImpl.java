package com.team6.onandthefarm.service.cart;

import com.team6.onandthefarm.dto.cart.CartDeleteDto;
import com.team6.onandthefarm.dto.cart.CartDto;
import com.team6.onandthefarm.dto.cart.CartIsActivatedDto;
import com.team6.onandthefarm.entity.cart.Cart;
import com.team6.onandthefarm.entity.product.Product;
import com.team6.onandthefarm.entity.user.User;
import com.team6.onandthefarm.repository.cart.CartRepository;
import com.team6.onandthefarm.repository.product.ProductRepository;
import com.team6.onandthefarm.repository.user.UserRepository;
import com.team6.onandthefarm.util.DateUtils;
import com.team6.onandthefarm.vo.cart.CartRequest;
import com.team6.onandthefarm.vo.cart.CartResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final DateUtils dateUtils;
    private final Environment env;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository, DateUtils dateUtils, Environment env){
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.dateUtils = dateUtils;
        this.env = env;
    }

    /**
     * 장바구니에 추가하는 메소드
     * @param cartDto
     * @return cartId
     */
    @Override
    public Long addCart(CartDto cartDto, Long userId) {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Optional<Product> product = productRepository.findById(cartDto.getProductId());
        Optional<User> user = userRepository.findById(userId);

        Cart cart = modelMapper.map(cartDto, Cart.class);
        cart.setUser(user.get());
        cart.setProduct(product.get());
        cart.setCartStatus(true);
        cart.setCartIsActivated(false);
        cart.setCartCreatedAt(dateUtils.transDate(env.getProperty("dateutils.format")));

        Cart savedCart = cartRepository.save(cart);

        return savedCart.getCartId();
    }

    /**
     * 장바구니의 유지 여부를 변경하는 메소드
     * @param cartIsActivatedDto
     * @return cartId
     */
    @Override
    public Long updateCartIsActivated(CartIsActivatedDto cartIsActivatedDto){

        Optional<Cart> cart = cartRepository.findById(cartIsActivatedDto.getCartId());
        cart.get().setCartIsActivated(cartIsActivatedDto.getCartIsActivated());

        return cart.get().getCartId();
    }

    /**
     * 장바구니를 삭제하는 메소드
     * @param cartDeleteDto
     * @return cartId
     */
    @Override
    public Long deleteCart(CartDeleteDto cartDeleteDto) {
        Optional<Cart> cart = cartRepository.findById(cartDeleteDto.getCartId());
        cart.get().setCartStatus(false);

        return cart.get().getCartId();
    }

    @Override
    public List<CartResponse> selectCart(Long userId) {
        List<Cart> carts = cartRepository.findNotDeletedCartByUserId(userId);

        List<CartResponse> cartResponses = new ArrayList<>();
        for(Cart c : carts){
            CartResponse cartResponse = CartResponse.builder()
                    .cartId(c.getCartId())
                    .cartIsActivated(c.getCartIsActivated())
                    .cartQty(c.getCartQty())
                    .productId(c.getProduct().getProductId())
                    .productName(c.getProduct().getProductName())
                    .productDeliveryCompany(c.getProduct().getProductDeliveryCompany())
                    .productMainImgSrc(c.getProduct().getProductMainImgSrc())
                    .productOriginPlace(c.getProduct().getProductOriginPlace())
                    .productPrice(c.getProduct().getProductPrice())
                    .productStatus(c.getProduct().getProductStatus())
                    .build();
            cartResponses.add(cartResponse);
        }
        return cartResponses;
    }
}
