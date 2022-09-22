package com.team6.onandthefarm.service.order;

import com.team6.onandthefarm.dto.order.OrderDto;
import com.team6.onandthefarm.entity.order.OrderProduct;
import com.team6.onandthefarm.entity.order.Orders;
import com.team6.onandthefarm.entity.order.Payment;
import com.team6.onandthefarm.repository.order.OrderProductRepository;
import com.team6.onandthefarm.repository.order.OrderRepository;
import com.team6.onandthefarm.repository.order.PaymentRepository;
import com.team6.onandthefarm.util.DateUtils;
import com.team6.onandthefarm.vo.order.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class OrderService {

    private OrderRepository orderRepository;

    private OrderProductRepository orderProductRepository;

    private PaymentRepository paymentRepository;

    private DateUtils dateUtils;

    private Environment env;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        OrderProductRepository orderProductRepository,
                        DateUtils dateUtils,
                        Environment env,
                        PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.orderProductRepository=orderProductRepository;
        this.dateUtils=dateUtils;
        this.env=env;
        this.paymentRepository = paymentRepository;
    }

    public OrderFindOneResponse findOneByProductId(Long productId){
        /**
         * productrepository로 상품 정보 가져오기
         */
        OrderFindOneResponse response = OrderFindOneResponse.builder()
                .productId(Long.valueOf(productId))
                .productImg("sdasdaads")
                .productName("sadsadsads")
                .productPrice(10000)
                .sellerId(Long.valueOf(1))
                .productQty(10)
                .build();
        return response;
    }

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

    public void createOrder(OrderDto orderDto){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        int totalPrice = 0;
        List<Long> orderProductsIds = new ArrayList<>();
        for(OrderFindOneResponse orderProducts : orderDto.getProductList()){
            OrderProduct orderProduct = OrderProduct.builder()
                    .orderProductName(orderProducts.getProductName())
                    .orderProductPrice(orderProducts.getProductPrice())
                    .orderProductQty(orderProducts.getProductQty())
                    .orderProductMainImg(orderProducts.getProductImg())
                    .sellerId(orderProducts.getSellerId())
                    .productId(orderProducts.getProductId())
                    .build();
            totalPrice+=orderProducts.getProductPrice()* orderProducts.getProductQty();
            /**
             * product테이블에서 sellerId와 같은거 찾기
             */
            int tmp = 1;
            if(orderProducts.getSellerId()==tmp){ // 1 대신 찾은거 넣기
                orderProductsIds.add(orderProductRepository.save(orderProduct).getOrderProductId());
            }
        }
        /**
         *  user엔티티 가져와서 order에 넣어주는 코드 작성해야함
         */
        Orders order = Orders.builder()
                .ordersAddress(orderDto.getOrderAddress())
                .ordersDate(dateUtils.transDate(env.getProperty("dateutils.format")))
                .ordersPhone(orderDto.getOrderPhone())
                .ordersRequest(orderDto.getOrderRequest())
                .ordersSerial(UUID.randomUUID().toString())
                .ordersSellerId(orderDto.getSellerId())
                .ordersRecipientName(orderDto.getOrderRecipientName())
                .ordersTotalPrice(totalPrice)
                .build();
        Orders orders = orderRepository.save(order);
        for(Long orderProductIds : orderProductsIds){
            Optional<OrderProduct> orderProduct = orderProductRepository.findById(orderProductIds);
            orderProduct.get().setOrders(orders);
            orderProductRepository.save(orderProduct.get());
        }
        createPayment(order.getOrdersSerial()); // 결제 생성
    }

    /**
     * 셀러의 주문 내역 조회를 위해 사용되는 메서드
     * @param orderSellerRequest : 프론트에서 보내준 주문내역 정보
     * @return
     */
    public List<OrderSellerResponse> findSellerOrders(OrderSellerRequest orderSellerRequest){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<Orders> ordersList = orderRepository.findByOrdersSellerIdAndOrdersDateBetween(
                Long.valueOf(orderSellerRequest.getUserId()),
                orderSellerRequest.getStartDate(),
                orderSellerRequest.getEndDate());
        List<OrderSellerResponse> responseList = new ArrayList<>();
        for(Orders order : ordersList){
            OrderSellerResponse orderSellerResponse = modelMapper.map(order,OrderSellerResponse.class);
            responseList.add(orderSellerResponse);
        }
        return responseList;
    }

    /**
     * 셀러가 주문 상세 조회를 할때 사용되는 메서드
     * @param orderSerial
     * @return
     */
    public OrderSellerDetailRequest findSellerOrderDetail(String orderSerial){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        OrderSellerDetailRequest orderSellerDetailRequest = new OrderSellerDetailRequest();
        Orders orders = orderRepository.findByOrdersSerial(orderSerial);
        orderSellerDetailRequest.setOrdersId(String.valueOf(orders.getOrdersId()));
        orderSellerDetailRequest.setOrdersDate(orders.getOrdersDate());
        orderSellerDetailRequest.setOrdersStatus(orders.getOrdersStatus());
        orderSellerDetailRequest.setOrdersSerial(orderSerial);
        orderSellerDetailRequest.setOrdersTotalPrice(orders.getOrdersTotalPrice());
        orderSellerDetailRequest.setOrdersProductList(new ArrayList<>());

        List<OrderProduct> orderProductList = orderProductRepository.findByOrders(orders);
        for(OrderProduct orderProduct : orderProductList){
            orderSellerDetailRequest.getOrdersProductList().add(
                    modelMapper.map(orderProduct, OrderProductDetailResponse.class));
        }

        return orderSellerDetailRequest;
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
}
