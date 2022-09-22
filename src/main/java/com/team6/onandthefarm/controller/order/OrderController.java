package com.team6.onandthefarm.controller.order;

import com.team6.onandthefarm.dto.order.OrderDto;
import com.team6.onandthefarm.service.order.OrderService;
import com.team6.onandthefarm.vo.order.*;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{product-no}")
    @ApiOperation(value = "단건 주문서 조회")
    public ResponseEntity<OrderFindOneResponse> findOneOrder(@PathVariable(name = "product-no") String productId){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        /* Product product = orderService.findOneByProductId(Long.valueOf(productId));
        OrderFindOneResponse response = modelMapper.map(product,OrderFindOneResponse.class);
        */
        OrderFindOneResponse response = orderService.findOneByProductId(Long.valueOf(productId));
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/carts/{user-no}")
    @ApiOperation(value = "다건 주문서 조회")
    public ResponseEntity<List<OrderFindOneResponse>> findOrders(@PathVariable(name = "user-no") String userId){
        List<OrderFindOneResponse> responseList = orderService.findCartByUserId(Long.valueOf(userId));
        return new ResponseEntity(responseList,HttpStatus.OK);
    }

    @PostMapping()
    @ApiOperation(value = "주문 생성")
    public ResponseEntity createOrder(@RequestBody OrderRequest orderRequest){
        HashSet<Long> sellerIds = new HashSet<>();
        for(OrderFindOneResponse order : orderRequest.getProductList()){
            sellerIds.add(order.getSellerId());
        }
        for(Long sellerId : sellerIds){
            OrderDto orderDto = OrderDto.builder()
                    .sellerId(sellerId)
                    .orderAddress(orderRequest.getOrderAddress())
                    .orderPhone(orderRequest.getOrderPhone())
                    .orderRequest(orderRequest.getOrderRequest())
                    .orderRecipientName(orderRequest.getOrderRecipientName())
                    .productList(orderRequest.getProductList())
                    .build();
            orderService.createOrder(orderDto);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/list")
    @ApiOperation(value = "주문 내역 조회")
    public ResponseEntity<List<OrderSellerResponse>> findSellerAllOrders(@RequestBody OrderSellerRequest orderSellerRequest){
        return new ResponseEntity(orderService.findSellerOrders(orderSellerRequest),HttpStatus.OK);
    }

    @GetMapping("/list/{order-no}")
    @ApiOperation(value = "주문 상세 조회")
    public ResponseEntity<OrderSellerDetailRequest> findSellerOrderDetail(@PathVariable(name = "order-no") String orderSerial){
        OrderSellerDetailRequest orderSellerDetailRequest = orderService.findSellerOrderDetail(orderSerial);
        return new ResponseEntity(orderSellerDetailRequest,HttpStatus.OK);
    }

    @GetMapping("/list/{order-no}")
    @ApiOperation(value = "주문 상세 조회")

}
