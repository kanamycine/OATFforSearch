package com.team6.onandthefarm.service.order;

import com.team6.onandthefarm.dto.order.*;
import com.team6.onandthefarm.entity.cart.Cart;
import com.team6.onandthefarm.entity.order.OrderProduct;
import com.team6.onandthefarm.entity.order.Orders;
import com.team6.onandthefarm.entity.order.Payment;
import com.team6.onandthefarm.entity.order.Refund;
import com.team6.onandthefarm.entity.product.Product;
import com.team6.onandthefarm.entity.user.User;
import com.team6.onandthefarm.repository.cart.CartRepository;
import com.team6.onandthefarm.repository.order.*;
import com.team6.onandthefarm.repository.product.ProductRepository;
import com.team6.onandthefarm.repository.user.UserRepository;
import com.team6.onandthefarm.util.DateUtils;
import com.team6.onandthefarm.vo.order.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.*;

@Service
@Transactional
@Slf4j
/**
 * 주문 상태
 * activated(os0) : 주문완료
 * canceled(os1) : 주문취소
 * refundRequest(os2) : 반품신청
 * refundCompleted(os3) : 반품확정
 * deliveryProgress(os4) : 배송 중
 * deliveryCompleted(os5) : 배송 완료
 */
public class OrderServiceImp implements OrderService{

    private final int pageContentNumber = 8;

    private OrderRepository orderRepository;

    private OrderProductRepository orderProductRepository;

    private PaymentRepository paymentRepository;

    private RefundRepository refundRepository;

    private ProductRepository productRepository;

    private UserRepository userRepository;

    private CartRepository cartRepository;

    private DateUtils dateUtils;

    private Environment env;

    @Autowired
    public OrderServiceImp(OrderRepository orderRepository,
                           OrderProductRepository orderProductRepository,
                           DateUtils dateUtils,
                           Environment env,
                           PaymentRepository paymentRepository,
                           RefundRepository refundRepository,
                           ProductRepository productRepository,
                           UserRepository userRepository,
                           CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.orderProductRepository=orderProductRepository;
        this.dateUtils=dateUtils;
        this.env=env;
        this.paymentRepository = paymentRepository;
        this.refundRepository = refundRepository;
        this.productRepository=productRepository;
        this.userRepository=userRepository;
        this.cartRepository=cartRepository;
    }

    /**
     * 단건 주문서 조회시 사용되는 메서드
     * @param orderSheetDto
     * @return
     */
    public OrderSheetResponse findOneByProductId(OrderSheetDto orderSheetDto){
        Optional<Product> product = productRepository.findById(orderSheetDto.getProductId());
        Optional<User> user = userRepository.findById(orderSheetDto.getUserId());
        log.info("제품 정보  =>  "+ product.get().toString());
        OrderSheetResponse response = OrderSheetResponse.builder()
                .productId(orderSheetDto.getProductId())
                .productImg(product.get().getProductMainImgSrc())
                .productName(product.get().getProductName())
                .productPrice(product.get().getProductPrice())
                .sellerId(product.get().getSeller().getSellerId())
                .productQty(orderSheetDto.getProductQty())
                .productTotalPrice(orderSheetDto.getProductQty()*product.get().getProductPrice())
                .userAddress(user.get().getUserAddress())
                .userName(user.get().getUserName())
                .userPhone(user.get().getUserPhone())
                .build();
        return response;
    }

    /**
     * 다건 주문서 조회 시 사용되는 메서드
     * @param userId
     * @return
     */

    public List<OrderFindOneResponse> findCartByUserId(Long userId){
        /**
         * 유저id로 카트 정보 + 상품 정보 가져오기
         */

        Optional<User> user = userRepository.findById(userId);

        List<Cart> carts = cartRepository.findByUser(user.get());

        List<OrderFindOneResponse> list = new ArrayList<>();

        for(Cart cart :carts){
            Product product = cart.getProduct();
            OrderFindOneResponse response = OrderFindOneResponse.builder()
                    .productQty(cart.getCartQty())
                    .productName(product.getProductName())
                    .productImg(product.getProductMainImgSrc())
                    .productPrice(product.getProductPrice())
                    .productId(product.getProductId())
                    .sellerId(product.getSeller().getSellerId())
                    .build();
            list.add(response);
        }

        return list;
    }

    /**
     * 재고 수량 확인하는 메서드
     * @param orderDto
     * @return true : 주문 가능 / false : 주문 불가능
     */
    public boolean checkStock(OrderDto orderDto){
        List<OrderProductDto> orderProducts = orderDto.getProductList();
        for(OrderProductDto orderProduct : orderProducts){
            Optional<Product> product = productRepository.findById(orderProduct.getProductId());
            if(product.get().getProductTotalStock()>=orderProduct.getProductQty()){
                return true;
            }
            if(!product.get().getProductStatus().equals("selling")){
                return false;
            }
        }
        return false;
    }

    /**
     * 주문 생성 메서드
     * 주문 생성 시 product 판매 수 늘리기 코드 짜기
     * @param orderDto
     */
    public Boolean createOrder(OrderDto orderDto){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        boolean stockCheck = checkStock(orderDto);
        if(!stockCheck){ // 재고 체크
            return Boolean.FALSE;
        }

        // productId->sellerId를 찾기
        /*
            key : productId
            value : sellerId
            prodSeller는 담을 공간
         */

        Optional<User> user = userRepository.findById(orderDto.getUserId());
        Orders orders = Orders.builder()
                .ordersPhone(orderDto.getOrderPhone())
                .ordersAddress(orderDto.getOrderAddress())
                .ordersRequest(orderDto.getOrderRequest())
                .ordersRecipientName(orderDto.getOrderRecipientName())
                .ordersSerial(UUID.randomUUID().toString())
                .ordersDate(dateUtils.transDate(env.getProperty("dateutils.format")))
                .ordersStatus("activated")
                .user(user.get())
                .build(); // 주문 엔티티 생성
        for(OrderProductDto orderProductDto : orderDto.getProductList()){
            Optional<Product> product = productRepository.findById(orderProductDto.getProductId());
            orderProductDto.setProductName(product.get().getProductName());
            orderProductDto.setProductPrice(product.get().getProductPrice());
            orderProductDto.setSellerId(product.get().getSeller().getSellerId());
            orders.setOrdersSellerId(product.get().getSeller().getSellerId());
            orderProductDto.setProductImg(product.get().getProductMainImgSrc()); // 이미지는 나중에
        }

        Orders ordersEntity = orderRepository.save(orders); // 주문 생성

        int totalPrice = 0;
        for(OrderProductDto order : orderDto.getProductList()){
            totalPrice+=order.getProductPrice()* order.getProductQty();
            OrderProduct orderProduct = OrderProduct.builder()
                    .orderProductMainImg(order.getProductImg())
                    .orderProductQty(order.getProductQty())
                    .orderProductPrice(order.getProductPrice())
                    .orderProductName(order.getProductName())
                    .orders(ordersEntity)
                    .productId(order.getProductId())
                    .sellerId(order.getSellerId())
                    .orderProductStatus(orders.getOrdersStatus())
                    .orderProductDate(orders.getOrdersDate())
                    .build();
            orderProductRepository.save(orderProduct); // 각각의 주문 상품 생성
            Optional<Product> product = productRepository.findById(order.getProductId());
            product.get().setProductTotalStock(product.get().getProductTotalStock()-order.getProductQty());
            product.get().setProductSoldCount(product.get().getProductSoldCount()+1);
        }
        orderRepository.findById(ordersEntity.getOrdersId()).get().setOrdersTotalPrice(totalPrice); // 총 주문액 set
        createPayment(ordersEntity.getOrdersSerial()); // 결제 생성
        return Boolean.TRUE;
    }

    /**
     * 셀러의 주문 내역 조회(최근 정렬)를 위해 사용되는 메서드
     * @param orderSellerFindDto : 셀러 ID와 조회할 기간을 가진 DTO
     * @return
     */
    public OrderSellerResponseListResponse findSellerOrders(OrderSellerFindDto orderSellerFindDto){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        List<OrderSellerResponseList> responseList = new ArrayList<>();

        List<OrderProduct> orderProductList = orderProductRepository.findBySellerId(Long.valueOf(orderSellerFindDto.getSellerId()));

        /*
                sellerId : orderId
         */
        Map<Long,Set<Long>> matching = new HashMap<>();
        for(OrderProduct orderProduct : orderProductList){
            if(Long.valueOf(orderSellerFindDto.getSellerId())==orderProduct.getSellerId()){
                if(!matching.containsKey(orderProduct.getSellerId())){
                    matching.put(orderProduct.getSellerId(),new HashSet<>());
                }
                matching.get(orderProduct.getSellerId()).add(orderProduct.getOrders().getOrdersId());
            }
        }
        if(matching.size()==0)
            return OrderSellerResponseListResponse.builder().responses(new ArrayList<>()).currentPageNum(0).totalPageNum(0).build();
        for(Long orderId : matching.get(Long.valueOf(orderSellerFindDto.getSellerId()))){
            List<OrderSellerResponse> orderResponse = new ArrayList<>();
            int totalPrice = 0;
            Optional<Orders> order = orderRepository.findById(orderId);
            Optional<User> user = userRepository.findById(order.get().getUser().getUserId());

            for(OrderProduct orderProduct : orderProductList){
                if(orderProduct.getOrders().getOrdersId()!=orderId) continue;
                OrderSellerResponse orderSellerResponse = OrderSellerResponse.builder()
                        .userName(user.get().getUserName())
                        .orderProductName(orderProduct.getOrderProductName())
                        .orderProductMainImg(orderProduct.getOrderProductMainImg())
                        .orderProductPrice(orderProduct.getOrderProductPrice())
                        .orderProductQty(orderProduct.getOrderProductQty())
                        .ordersDate(order.get().getOrdersDate())
                        .ordersSerial(order.get().getOrdersSerial())
                        .orderProductStatus(orderProduct.getOrderProductStatus())
                        .build();
                if(orderProduct.getOrderProductStatus().equals("deliveryProgress")||
                        orderProduct.getOrderProductStatus().equals("deliveryCompleted")){
                    orderSellerResponse.setOrderProductDeliveryWaybillNumber(order.get().getOrdersDeliveryWaybillNumber());
                    orderSellerResponse.setOrderProductDeliveryCompany(order.get().getOrdersDeliveryCompany());
                    orderSellerResponse.setOrderProductDeliveryDate(order.get().getOrdersDeliveryDate());
                }
                totalPrice+=orderProduct.getOrderProductPrice()*orderProduct.getOrderProductQty();
                orderResponse.add(orderSellerResponse);
            }
            OrderSellerResponseList orderSellerResponseList = new OrderSellerResponseList();
            orderSellerResponseList.setOrderSellerResponses(orderResponse);
            orderSellerResponseList.setOrderTotalPrice(totalPrice);
            orderSellerResponseList.setOrderDate(order.get().getOrdersDate());
            if(orderSellerResponseList.getOrderSellerResponses().size()!=0){
                responseList.add(orderSellerResponseList);
            }
        }



        /**
         * 아래가 정렬 및 페이징처리 코드
         */

        OrderSellerResponseListResponse resultResponse = new OrderSellerResponseListResponse();
        responseList.sort((o1, o2) -> {
            int result = o2.getOrderDate().compareTo(o1.getOrderDate());
            return result;
        });

        int startIndex = orderSellerFindDto.getPageNumber()*pageContentNumber;

        int size = responseList.size();


        if(size<startIndex+pageContentNumber){
            resultResponse.setResponses(responseList.subList(startIndex,size));
            resultResponse.setCurrentPageNum(orderSellerFindDto.getPageNumber());
            if(size%pageContentNumber!=0){
                resultResponse.setTotalPageNum((size/pageContentNumber)+1);
            }
            else{
                resultResponse.setTotalPageNum(size/pageContentNumber);
            }
            return resultResponse;
        }

        resultResponse.setResponses(responseList.subList(startIndex,startIndex+pageContentNumber));
        resultResponse.setCurrentPageNum(orderSellerFindDto.getPageNumber());
        if(size%pageContentNumber!=0){
            resultResponse.setTotalPageNum((size/pageContentNumber)+1);
        }
        else{
            resultResponse.setTotalPageNum(size/pageContentNumber);
        }
        return resultResponse;
    }

    /**
     * 유저가 주문 내역을 조회 할때 사용되는 메서드
     * userId로 자신의 주문 다 가져오고 -> orderProduct테이블에서 orderId로 묶은 뒤 보내기
     * @param orderUserFindDto
     * @return
     */
    public OrderUserResponseListResponse findUserOrders(OrderUserFindDto orderUserFindDto){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        List<OrderUserResponseList> response = new ArrayList<>();

        Optional<User> user = userRepository.findById(Long.valueOf(orderUserFindDto.getUserId()));

        List<Orders> myOrders = orderRepository.findByUser(user.get());

        for(Orders order : myOrders){
            int totalPrice = 0;

            OrderUserResponseList orderUserResponseList = new OrderUserResponseList();
            List<OrderProduct> userOrders = orderProductRepository.findByOrders(order);
            List<OrderSellerResponse> orderSellerResponses = new ArrayList<>();

            for(OrderProduct orderProduct : userOrders){
                OrderSellerResponse orderSellerResponse = OrderSellerResponse.builder()
                        .orderProductStatus(orderProduct.getOrderProductStatus())
                        .ordersSerial(order.getOrdersSerial())
                        .orderProductPrice(orderProduct.getOrderProductPrice())
                        .orderProductQty(orderProduct.getOrderProductQty())
                        .orderProductName(orderProduct.getOrderProductName())
                        .orderProductMainImg(orderProduct.getOrderProductMainImg())
                        .build();
                totalPrice+=orderProduct.getOrderProductPrice()*orderProduct.getOrderProductQty();
                orderSellerResponses.add(orderSellerResponse);
            }

            orderUserResponseList.setOrderSellerResponses(orderSellerResponses);
            orderUserResponseList.setOrderTotalPrice(totalPrice);
            orderUserResponseList.setOrderDate(order.getOrdersDate());
            response.add(orderUserResponseList);
        }

        OrderUserResponseListResponse resultResponse = new OrderUserResponseListResponse();

        response.sort((o1, o2) -> {
            int result = o2.getOrderDate().compareTo(o1.getOrderDate());
            return result;
        });

        int startIndex = orderUserFindDto.getPageNumber()*pageContentNumber;

        int size = response.size();


        if(size<startIndex+pageContentNumber){
            resultResponse.setResponses(response.subList(startIndex,size));
            resultResponse.setCurrentPageNum(orderUserFindDto.getPageNumber());
            if(size%pageContentNumber!=0){
                resultResponse.setTotalPageNum((size/pageContentNumber)+1);
            }
            else{
                resultResponse.setTotalPageNum(size/pageContentNumber);
            }
            return resultResponse;
        }

        resultResponse.setResponses(response.subList(startIndex,startIndex+pageContentNumber));
        resultResponse.setCurrentPageNum(orderUserFindDto.getPageNumber());
        if(size%pageContentNumber!=0){
            resultResponse.setTotalPageNum((size/pageContentNumber)+1);
        }
        else{
            resultResponse.setTotalPageNum(size/pageContentNumber);
        }
        return resultResponse;
    }

    public OrderUserDetailResponse findSellerOrderDetail(OrderSellerDetailDto orderSellerDetailDto){
        Orders order = orderRepository.findByOrdersSerial(orderSellerDetailDto.getOrderSerial());

        int totalPrice = 0;

        OrderUserDetailResponse orderUserDetailResponse = OrderUserDetailResponse.builder()
                .orderAddress(order.getOrdersAddress())
                .orderDate(order.getOrdersDate())
                .orderName(order.getOrdersRecipientName())
                .orderPhone(order.getOrdersPhone())
                .orderRequest(order.getOrdersRequest())
                .orderStatus(order.getOrdersStatus())
                .orderProducts(new ArrayList<>())
                .build();

        List<OrderProduct> orderProducts = orderProductRepository.findByOrders(order);

        for(OrderProduct orderProduct : orderProducts){
            if(orderProduct.getSellerId()!=Long.valueOf(orderSellerDetailDto.getSellerId())) continue;
            OrderFindOneDetailResponse orderFindOneDetailResponse = OrderFindOneDetailResponse.builder()
                    .orderProductId(orderProduct.getOrderProductId())
                    .productImg(orderProduct.getOrderProductMainImg())
                    .productName(orderProduct.getOrderProductName())
                    .productPrice(orderProduct.getOrderProductPrice())
                    .productQty(orderProduct.getOrderProductQty())
                    .build();
            totalPrice+=orderFindOneDetailResponse.getProductPrice()
                    *orderFindOneDetailResponse.getProductQty();
            orderUserDetailResponse.getOrderProducts().add(orderFindOneDetailResponse);
        }

        orderUserDetailResponse.setOrderTotalPrice(totalPrice);

        return orderUserDetailResponse;
    }

    /**
     * 유저가 주문 상세 조회를 할때 사용되는 메서드
     * @param orderSerial
     * @return
     */
    public OrderUserDetailResponse findUserOrderDetail(String orderSerial){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Orders orders = orderRepository.findByOrdersSerial(orderSerial);

        Optional<User> user = userRepository.findById(orders.getUser().getUserId());

        List<OrderProduct> orderProducts = orderProductRepository.findByOrders(orders); // 주문에 대한 모든 제품가져옴

        int totalPrice = 0;

        OrderUserDetailResponse orderUserDetailResponse =
                OrderUserDetailResponse.builder()
                        .orderName(user.get().getUserName())
                        .orderAddress(orders.getOrdersAddress())
                        .orderDate(orders.getOrdersDate())
                        .orderPhone(orders.getOrdersPhone())
                        .orderRequest(orders.getOrdersRequest())
                        .orderStatus(orders.getOrdersStatus())
                        .orderProducts(new ArrayList<>())
                        .build();

        for(OrderProduct orderProduct : orderProducts){
            OrderFindOneDetailResponse orderFindOneResponse = OrderFindOneDetailResponse.builder()
                    .productPrice(orderProduct.getOrderProductPrice())
                    .productName(orderProduct.getOrderProductName())
                    .productImg(orderProduct.getOrderProductMainImg())
                    .productQty(orderProduct.getOrderProductQty())
                    .orderProductId(orderProduct.getOrderProductId())
                    .build();
            totalPrice+=orderFindOneResponse.getProductPrice()*orderFindOneResponse.getProductQty();
            orderUserDetailResponse.getOrderProducts().add(orderFindOneResponse);
        }

        orderUserDetailResponse.setOrderTotalPrice(totalPrice);

        return orderUserDetailResponse;
    }

    public void createPayment(String orderSerial){
        Orders orders = orderRepository.findByOrdersSerial(orderSerial);
        Payment payment = Payment.builder()
                .orders(orders)
                .paymentDate(dateUtils.transDate(env.getProperty("dateutils.format")))
                .paymentStatus(true)
                .paymentDepositAmount(orders.getOrdersTotalPrice())
                .paymentDepositBank("신한은행")
                .paymentDepositName("김성환")
                .paymentMethod("card")
                .build();
        paymentRepository.save(payment);
    }

    /**
     * 취소 생성해주는 메서드
     * @param refundDto
     * @return
     */
    public Boolean createCancel(RefundDto refundDto){
        Optional<OrderProduct> orderProduct = orderProductRepository.findById(refundDto.getOrderProductId());
        orderProduct.get().setOrderProductStatus("canceled"); // 취소상태

        // 모든 주문상품의 상태가 취소처리되면 주문상태 또한 취소상태로 바꾸는 부분
        Orders orders = orderProduct.get().getOrders();
        Optional<Orders> savedOrders = orderRepository.findById(orders.getOrdersId());

        int canceledCount = 0;
        int orderProductsCount = 0;
        List<OrderProduct> productOfSameOrders = orderProductRepository.findOrderProductsByOrders(orders);
        for(OrderProduct oProduct : productOfSameOrders){
            if(oProduct.getOrderProductStatus().equals("canceled")){
                canceledCount++;
            }
            orderProductsCount++;
        }
        if(canceledCount == orderProductsCount) {
            savedOrders.get().setOrdersStatus("canceled");
        }

        /**
         * 재고 추가하는 코드 작성
         */
        Refund refund = Refund.builder()
                .refundContent(refundDto.getRefundDetail())
                .orderProductId(refundDto.getOrderProductId())
                .refundImage(refundDto.getRefundImage())
                .userId(refundDto.getUserId())
                .build();
        refundRepository.save(refund);

        if(orderProduct.get().getOrderProductStatus().equals("canceled")){
            return true;
        }
        return false;
    }

    /**
     * 반품 생성해주는 메서드
     * @param refundDto
     * @return
     */
    public Boolean createRefund(RefundDto refundDto){
        Optional<OrderProduct> orderProduct = orderProductRepository.findById(refundDto.getOrderProductId());
        orderProduct.get().setOrderProductStatus("refundRequest"); // 반품신청상태

        // 모든 주문상품의 상태가 환불처리되면 주문상태 또한 환불상태로 바꾸는 부분
        Orders orders = orderProduct.get().getOrders();
        Optional<Orders> savedOrders = orderRepository.findById(orders.getOrdersId());

        int refundCount = 0;
        int orderProductsCount = 0;
        List<OrderProduct> productOfSameOrders = orderProductRepository.findOrderProductsByOrders(orders);
        for(OrderProduct oProduct : productOfSameOrders){
            if(oProduct.getOrderProductStatus().equals("refundRequest")){
                refundCount++;
            }
            orderProductsCount++;
        }
        if(refundCount == orderProductsCount) {
            savedOrders.get().setOrdersStatus("refundRequest");
        }

        Refund refund = Refund.builder()
                .refundContent(refundDto.getRefundDetail())
                .orderProductId(refundDto.getOrderProductId())
                .refundImage(refundDto.getRefundImage())
                .userId(refundDto.getUserId())
                .orders(orders)
                .build();
        refundRepository.save(refund);

        if(orderProduct.get().getOrderProductStatus().equals("refundRequest")){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 반품/취소 내역 조회해주는 메서드
     * 날짜 조정하는 코드 넣기
     * @param orderSellerRequest
     * @return
     */
    public OrderSellerResultResponse findSellerClaims(OrderSellerRequest orderSellerRequest){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        /*
            sellerId로 orderProduct에서 제품 하나씩 찾는다. canceled(os1) refundRequest(os2) 인 상태의 제품들을
         */
        List<OrderProduct> orderProductsSO1 = orderProductRepository.findBySellerIdAndOrderProductStatus(Long.valueOf(orderSellerRequest.getSellerId()),"canceled");
        List<OrderProduct> orderProductsSO2 = orderProductRepository.findBySellerIdAndOrderProductStatus(Long.valueOf(orderSellerRequest.getSellerId()),"refundRequest");
        List<OrderProduct> orderProductsSO3 = orderProductRepository.findBySellerIdAndOrderProductStatus(Long.valueOf(orderSellerRequest.getSellerId()),"refundCompleted");
        List<OrderProduct> orderProducts = new ArrayList<>();
        for(OrderProduct orderProduct : orderProductsSO1){
            orderProducts.add(orderProduct);
        }
        for(OrderProduct orderProduct : orderProductsSO2){
            orderProducts.add(orderProduct);
        }
        for(OrderProduct orderProduct : orderProductsSO3){
            orderProducts.add(orderProduct);
        }


        List<OrderSellerResponse> responseList = new ArrayList<>();
        for(OrderProduct orderProduct : orderProducts){
            OrderSellerResponse orderSellerResponse = modelMapper.map(orderProduct,OrderSellerResponse.class);
            Optional<Orders> orders = orderRepository.findById(orderProduct.getOrders().getOrdersId());
            Optional<User> user = userRepository.findById(orders.get().getUser().getUserId());

            orderSellerResponse.setOrdersDate(orders.get().getOrdersDate());
            orderSellerResponse.setOrdersSerial(orders.get().getOrdersSerial());
            orderSellerResponse.setUserName(user.get().getUserName());
            orderSellerResponse.setOrderProductId(orderProduct.getOrderProductId());
            orderSellerResponse.setOrderTotalPrice(
                    orderProduct.getOrderProductPrice()*orderProduct.getOrderProductQty());
            responseList.add(orderSellerResponse);
        }

        OrderSellerResultResponse resultResponse = new OrderSellerResultResponse();

        responseList.sort((o1, o2) -> {
            int result = o2.getOrdersDate().compareTo(o1.getOrdersDate());
            return result;
        });

        int startIndex = orderSellerRequest.getPageNumber()*pageContentNumber;

        int size = responseList.size();

        if(size<startIndex+pageContentNumber){
            resultResponse.setResponses(responseList.subList(startIndex,size));
            resultResponse.setCurrentPageNum(orderSellerRequest.getPageNumber());
            if(size%pageContentNumber!=0){
                resultResponse.setTotalPageNum((size/pageContentNumber)+1);
            }
            else{
                resultResponse.setTotalPageNum(size/pageContentNumber);
            }
            return resultResponse;
        }

        resultResponse.setResponses(responseList.subList(startIndex,startIndex+pageContentNumber));
        resultResponse.setCurrentPageNum(orderSellerRequest.getPageNumber());
        if(size%pageContentNumber!=0){
            resultResponse.setTotalPageNum((size/pageContentNumber)+1);
        }
        else{
            resultResponse.setTotalPageNum(size/pageContentNumber);
        }
        return resultResponse;
    }

    public OrderSellerResultResponse findUserClaims(OrderUserFindDto orderUserFindDto){
        List<OrderSellerResponse> responses = new ArrayList<>();

        List<Refund> refunds = refundRepository.findByUserId(Long.valueOf(orderUserFindDto.getUserId()));

        for(Refund refund : refunds){
            Optional<OrderProduct> orderProduct
                    = orderProductRepository.findById(refund.getOrderProductId());
            OrderSellerResponse response = OrderSellerResponse.builder()
                    .orderProductMainImg(orderProduct.get().getOrderProductMainImg())
                    .orderProductName(orderProduct.get().getOrderProductName())
                    .orderProductQty(orderProduct.get().getOrderProductQty())
                    .orderTotalPrice(orderProduct.get().getOrderProductPrice()
                            *orderProduct.get().getOrderProductQty())
                    .ordersDate(orderProduct.get().getOrders().getOrdersDate())
                    .orderProductStatus(orderProduct.get().getOrderProductStatus())
                    .build();
            responses.add(response);
        }

        OrderSellerResultResponse resultResponse = new OrderSellerResultResponse();

        responses.sort((o1, o2) -> {
            int result = o2.getOrdersDate().compareTo(o1.getOrdersDate());
            return result;
        });

        int startIndex = orderUserFindDto.getPageNumber()*pageContentNumber;

        int size = responses.size();

        if(size<startIndex+pageContentNumber){
            resultResponse.setResponses(responses.subList(startIndex,size));
            resultResponse.setCurrentPageNum(orderUserFindDto.getPageNumber());
            if(size%pageContentNumber!=0){
                resultResponse.setTotalPageNum((size/pageContentNumber)+1);
            }
            else{
                resultResponse.setTotalPageNum(size/pageContentNumber);
            }
            return resultResponse;
        }

        resultResponse.setResponses(responses.subList(startIndex,startIndex+pageContentNumber));
        resultResponse.setCurrentPageNum(orderUserFindDto.getPageNumber());
        if(size%pageContentNumber!=0){
            resultResponse.setTotalPageNum((size/pageContentNumber)+1);
        }
        else{
            resultResponse.setTotalPageNum(size/pageContentNumber);
        }
        return resultResponse;
    }

    /**
     * 취소/반품 상세 내역 조회
     * @param orderProductId
     * @return
     */
    public RefundDetailResponse findRefundDetail(Long orderProductId){
        Refund refund = refundRepository.findByOrderProductId(orderProductId);
        Optional<OrderProduct> orderProduct = orderProductRepository.findById(orderProductId);
        User user = orderProduct.get().getOrders().getUser();
        RefundDetailResponse response = RefundDetailResponse.builder()
                .cancelDetail(refund.getRefundContent())
                .productName(orderProduct.get().getOrderProductName())
                .productPrice(orderProduct.get().getOrderProductPrice())
                .productQty(orderProduct.get().getOrderProductQty())
                .productStatus(orderProduct.get().getOrderProductStatus())
                .refundImage(refund.getRefundImage())
                .productImage(orderProduct.get().getOrderProductMainImg())
                .userAddress(user.getUserAddress())
                .userPhone(user.getUserPhone())
                .userName(user.getUserName())
                .productTotalPrice(
                        orderProduct.get().getOrderProductPrice() *
                                orderProduct.get().getOrderProductQty())
                .build();
        return response;
    }

    /**
     * 반품 처리해주는 메서드
     * @param orderProductId
     * @return
     */
    public Boolean conformRefund(Long orderProductId){
        Optional<OrderProduct> orderProduct = orderProductRepository.findById(orderProductId);
        orderProduct.get().setOrderProductStatus("refundCompleted"); // 반품 확정
        if(orderProduct.get().getOrderProductStatus().equals("refundCompleted")){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 배송 시작을 해주는 메서드
     * @param orderDeliveryDto
     * @return
     */
    public Boolean deliveryStart(OrderDeliveryDto orderDeliveryDto){
        Orders orders = orderRepository.findByOrdersSerial(orderDeliveryDto.getOrderSerial());
        orders.setOrdersDeliveryCompany(orderDeliveryDto.getOrderDeliveryCompany());
        orders.setOrdersDeliveryDate(dateUtils.transDate(env.getProperty("dateutils.format")));
        orders.setOrdersDeliveryWaybillNumber(orderDeliveryDto.getOrderDeliveryWaybillNumber());
        orders.setOrdersStatus("deliveryProgress");

        List<OrderProduct> orderProducts = orderProductRepository.findByOrders(orders);
        for(OrderProduct orderProduct : orderProducts){
            if(!orderProduct.getOrderProductStatus().equals("canceled")) {
                orderProduct.setOrderProductStatus("deliveryProgress");
            }
        }
        return Boolean.TRUE;
    }

    /**
     * 배송을 완료처리해주는 메서드
     * @param orderSerial
     * @return
     */
    public Boolean deliveryConform(String orderSerial){
        Orders orders = orderRepository.findByOrdersSerial(orderSerial);
        orders.setOrdersStatus("deliveryCompleted");

        List<OrderProduct> orderProducts = orderProductRepository.findByOrders(orders);
        for(OrderProduct orderProduct : orderProducts){
            orderProduct.setOrderProductStatus("deliveryCompleted");
        }
        return Boolean.TRUE;
    }

}
