package com.team6.onandthefarm.service.order;

import com.team6.onandthefarm.dto.order.*;
import com.team6.onandthefarm.entity.order.OrderProduct;
import com.team6.onandthefarm.entity.order.Orders;
import com.team6.onandthefarm.entity.order.Payment;
import com.team6.onandthefarm.entity.order.Refund;
import com.team6.onandthefarm.entity.product.Product;
import com.team6.onandthefarm.entity.user.User;
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

import java.util.*;

@Service
@Transactional
@Slf4j
/**
 * 주문 상태
 * os0 : 주문완료
 * os1 : 주문취소
 * os2 : 반품신청
 * os3 : 반품확정
 * os4 : 배송 중
 * os5 : 배송 완료
 */
public class OrderServiceImp implements OrderService{

    private final int pageContentNumber = 8;

    private OrderRepository orderRepository;

    private OrderProductRepository orderProductRepository;

    private PaymentRepository paymentRepository;

    private RefundRepository refundRepository;

    private ProductRepository productRepository;

    private UserRepository userRepository;

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
                           UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderProductRepository=orderProductRepository;
        this.dateUtils=dateUtils;
        this.env=env;
        this.paymentRepository = paymentRepository;
        this.refundRepository = refundRepository;
        this.productRepository=productRepository;
        this.userRepository=userRepository;
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
        List<OrderFindOneResponse> list = new ArrayList<>();
        OrderFindOneResponse response1 = OrderFindOneResponse.builder()
                .productId(Long.valueOf(12))
                .productImg("sdasdaads")
                .productName("sadsadsads")
                .productPrice(10000)
                .sellerId(Long.valueOf(1))
                .productQty(10)
                .build();
        OrderFindOneResponse response2 = OrderFindOneResponse.builder()
                .productId(Long.valueOf(12))
                .productImg("sdasdaads")
                .productName("sadsadsads")
                .productPrice(10000)
                .sellerId(Long.valueOf(1))
                .productQty(10)
                .build();
        list.add(response1);
        list.add(response2);
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
        Optional<User> user = userRepository.findById(orderDto.getUserId());
        Orders orders = Orders.builder()
                .ordersPhone(orderDto.getOrderPhone())
                .ordersAddress(orderDto.getOrderAddress())
                .ordersRequest(orderDto.getOrderRequest())
                .ordersRecipientName(orderDto.getOrderRecipientName())
                .ordersSerial(UUID.randomUUID().toString())
                .ordersDate(dateUtils.transDate(env.getProperty("dateutils.format")))
                .ordersStatus("os0")
                .ordersSellerId(orderDto.getSellerId())
                .userId(user.get())
                .build(); // 주문 엔티티 생성
        for(OrderProductDto orderProductDto : orderDto.getProductList()){
            Optional<Product> product = productRepository.findById(orderProductDto.getProductId());
            orderProductDto.setProductName(product.get().getProductName());
            orderProductDto.setProductPrice(product.get().getProductPrice());
            orderProductDto.setSellerId(product.get().getSeller().getSellerId());
            orderProductDto.setProductImg(product.get().getProductMainImgSrc()); // 이미지는 나중에
        }
        Orders ordersEntity = orderRepository.save(orders); // 주문 생성
        boolean stockCheck = checkStock(orderDto);
        if(!stockCheck){
            return Boolean.FALSE;
        }
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
    public List<OrderSellerResponseList> findSellerOrders(OrderSellerFindDto orderSellerFindDto){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        List<Orders> ordersListOs0 = orderRepository.findByOrdersStatusAndOrdersDateBetween(
                "os0",
                orderSellerFindDto.getStartDate(),
                orderSellerFindDto.getEndDate()); // 모든 so0인 order가져오기
        List<Orders> ordersListOs4 = orderRepository.findByOrdersStatusAndOrdersDateBetween(
                "os4",
                orderSellerFindDto.getStartDate(),
                orderSellerFindDto.getEndDate()); // 모든 so4인 order가져오기
        List<Orders> ordersListOs5 = orderRepository.findByOrdersStatusAndOrdersDateBetween(
                "os5",
                orderSellerFindDto.getStartDate(),
                orderSellerFindDto.getEndDate()); // 모든 so5인 order가져오기

        List<Orders> ordersList = new ArrayList<>();
        for(Orders orders : ordersListOs0){
            ordersList.add(orders);
        }
        for(Orders orders : ordersListOs4){
            ordersList.add(orders);
        }
        for(Orders orders : ordersListOs5){
            ordersList.add(orders);
        }

        List<OrderSellerResponseList> responseList = new ArrayList<>();
        for(Orders order : ordersList){
            int totalPrice = 0;
            // 주문 한 묶음 가져오기
            List<OrderProduct> orderProductListSo0 = orderProductRepository.findByOrdersAndSellerIdAndOrderProductStatus(order,Long.valueOf(orderSellerFindDto.getSellerId()),"os0");
            List<OrderProduct> orderProductListSo4 = orderProductRepository.findByOrdersAndSellerIdAndOrderProductStatus(order,Long.valueOf(orderSellerFindDto.getSellerId()),"os4");
            List<OrderProduct> orderProductListSo5 = orderProductRepository.findByOrdersAndSellerIdAndOrderProductStatus(order,Long.valueOf(orderSellerFindDto.getSellerId()),"os5");
            List<OrderProduct> orderProductList = new ArrayList<>();

            Optional<User> user = userRepository.findById(order.getUserId().getUserId());

            for(OrderProduct orderProduct : orderProductListSo0){
                orderProductList.add(orderProduct);
            }
            for(OrderProduct orderProduct : orderProductListSo4){
                orderProductList.add(orderProduct);
            }
            for(OrderProduct orderProduct : orderProductListSo5){
                orderProductList.add(orderProduct);
            }

            List<OrderSellerResponse> orderResponse = new ArrayList<>();
            for(OrderProduct orderProduct : orderProductList){
                OrderSellerResponse orderSellerResponse = OrderSellerResponse.builder()
                        .userName(user.get().getUserName())
                        .orderProductName(orderProduct.getOrderProductName())
                        .orderProductMainImg(orderProduct.getOrderProductMainImg())
                        .orderProductPrice(orderProduct.getOrderProductPrice())
                        .orderProductQty(orderProduct.getOrderProductQty())
                        .ordersDate(order.getOrdersDate())
                        .ordersSerial(order.getOrdersSerial())
                        .orderProductStatus(order.getOrdersStatus())
                        .build();
                if(orderProduct.getOrderProductStatus().equals("os4")||
                        orderProduct.getOrderProductStatus().equals("os5")){
                    orderSellerResponse.setOrderProductDeliveryWaybillNumber(order.getOrdersDeliveryWaybillNumber());
                    orderSellerResponse.setOrderProductDeliveryCompany(order.getOrdersDeliveryCompany());
                    orderSellerResponse.setOrderProductDeliveryDate(order.getOrdersDeliveryDate());
                }
                totalPrice+=orderProduct.getOrderProductPrice()*orderProduct.getOrderProductQty();
                orderResponse.add(orderSellerResponse);
            }
            OrderSellerResponseList orderSellerResponseList = new OrderSellerResponseList();
            orderSellerResponseList.setOrderSellerResponses(orderResponse);
            orderSellerResponseList.setOrderTotalPrice(totalPrice);
            orderSellerResponseList.setOrderDate(order.getOrdersDate());
            if(orderSellerResponseList.getOrderSellerResponses().size()!=0){
                responseList.add(orderSellerResponseList);
            }
        }
        /**
         * 아래가 정렬 및 페이징처리 코드
         */
        responseList.sort((o1, o2) -> {
            int result = o2.getOrderDate().compareTo(o1.getOrderDate());
            return result;
        });

        int startIndex = orderSellerFindDto.getPageNumber()*pageContentNumber;

        int size = responseList.size();


        if(size<startIndex+pageContentNumber){
            return responseList.subList(startIndex,size);
        }

        return responseList.subList(startIndex,startIndex+pageContentNumber);
    }

    /**
     * 셀러가 주문 상세 조회를 할때 사용되는 메서드
     * @param orderSerial
     * @return
     */
    public OrderSellerDetailResponse findSellerOrderDetail(String orderSerial){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Orders orders = orderRepository.findByOrdersSerial(orderSerial);

        Optional<User> user = userRepository.findById(orders.getUserId().getUserId());

        List<OrderProduct> orderProducts = orderProductRepository.findByOrders(orders); // 주문에 대한 모든 제품가져옴

        int totalPrice = 0;

        OrderSellerDetailResponse orderSellerDetailResponse =
                OrderSellerDetailResponse.builder()
                        .orderName(user.get().getUserName())
                        .orderAddress(orders.getOrdersAddress())
                        .orderDate(orders.getOrdersDate())
                        .orderPhone(orders.getOrdersPhone())
                        .orderRequest(orders.getOrdersRequest())
                        .orderStatus(orders.getOrdersStatus())
                        .orderProducts(new ArrayList<>())
                        .build();

        for(OrderProduct orderProduct : orderProducts){
            if(orderProduct.getOrderProductStatus().equals("os1")){
                continue;
            }
            if(orderProduct.getOrderProductStatus().equals("os2")){
                continue;
            }
            OrderFindOneResponse orderFindOneResponse = OrderFindOneResponse.builder()
                    .productPrice(orderProduct.getOrderProductPrice())
                    .productName(orderProduct.getOrderProductName())
                    .productImg(orderProduct.getOrderProductMainImg())
                    .productQty(orderProduct.getOrderProductQty())
                    .build();
            totalPrice+=orderFindOneResponse.getProductPrice()*orderFindOneResponse.getProductQty();
            orderSellerDetailResponse.getOrderProducts().add(orderFindOneResponse);
        }

        orderSellerDetailResponse.setOrderTotalPrice(totalPrice);

        return orderSellerDetailResponse;
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
    public boolean createCancel(RefundDto refundDto){
        Optional<OrderProduct> orderProduct = orderProductRepository.findById(refundDto.getOrderProductId());
        orderProduct.get().setOrderProductStatus("os1"); // 취소상태
        Refund refund = Refund.builder()
                .refundContent(refundDto.getRefundDetail())
                .orderProductId(refundDto.getOrderProductId())
                .build();
        refundRepository.save(refund);

        if(orderProduct.get().getOrderProductStatus().equals("os1")){
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
        orderProduct.get().setOrderProductStatus("os2"); // 반품신청상태
        Refund refund = Refund.builder()
                .refundContent(refundDto.getRefundDetail())
                .orderProductId(refundDto.getOrderProductId())
                .refundImage(refundDto.getRefundImage())
                .build();
        refundRepository.save(refund);

        if(orderProduct.get().getOrderProductStatus().equals("os2")){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 반품/취소 내역 조회해주는 메서드
     * @param orderSellerRequest
     * @return
     */
    public List<OrderSellerResponse> findSellerClaims(OrderSellerRequest orderSellerRequest){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        /*
            sellerId로 orderProduct에서 제품 하나씩 찾는다. os1 os2 인 상태의 제품들을
         */
        List<OrderProduct> orderProductsSO1 = orderProductRepository.findBySellerIdAndOrderProductStatus(Long.valueOf(orderSellerRequest.getSellerId()),"os1");
        List<OrderProduct> orderProductsSO2 = orderProductRepository.findBySellerIdAndOrderProductStatus(Long.valueOf(orderSellerRequest.getSellerId()),"os2");
        List<OrderProduct> orderProductsSO3 = orderProductRepository.findBySellerIdAndOrderProductStatus(Long.valueOf(orderSellerRequest.getSellerId()),"os3");
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
            Optional<User> user = userRepository.findById(orders.get().getUserId().getUserId());

            orderSellerResponse.setOrdersDate(orders.get().getOrdersDate());
            orderSellerResponse.setOrdersSerial(orders.get().getOrdersSerial());
            orderSellerResponse.setUserName(user.get().getUserName());
            orderSellerResponse.setOrderProductId(orderProduct.getOrderProductId());
            orderSellerResponse.setOrderTotalPrice(
                    orderProduct.getOrderProductPrice()*orderProduct.getOrderProductQty());
            responseList.add(orderSellerResponse);
        }

        responseList.sort((o1, o2) -> {
            int result = o2.getOrdersDate().compareTo(o1.getOrdersDate());
            return result;
        });

        int startIndex = orderSellerRequest.getPageNumber()*pageContentNumber;

        int size = responseList.size();

        if(size<startIndex+pageContentNumber){
            return responseList.subList(startIndex,size);
        }

        return responseList.subList(startIndex,startIndex+pageContentNumber);
    }

    /**
     * 취소/반품 상세 내역 조회
     * @param orderProductId
     * @return
     */
    public RefundDetailResponse findRefundDetail(Long orderProductId){
        Refund refund = refundRepository.findByOrderProductId(orderProductId);
        Optional<OrderProduct> orderProduct = orderProductRepository.findById(orderProductId);
        User user = orderProduct.get().getOrders().getUserId();
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
        orderProduct.get().setOrderProductStatus("os3"); // 반품 확정
        if(orderProduct.get().getOrderProductStatus().equals("os3")){
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
        orders.setOrdersStatus("os4");

        List<OrderProduct> orderProducts = orderProductRepository.findByOrders(orders);
        for(OrderProduct orderProduct : orderProducts){
            orderProduct.setOrderProductStatus("os4");
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
        orders.setOrdersStatus("os5");

        List<OrderProduct> orderProducts = orderProductRepository.findByOrders(orders);
        for(OrderProduct orderProduct : orderProducts){
            orderProduct.setOrderProductStatus("os5");
        }
        return Boolean.TRUE;
    }

}
