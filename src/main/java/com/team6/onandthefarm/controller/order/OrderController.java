package com.team6.onandthefarm.controller.order;

import com.team6.onandthefarm.dto.order.*;
import com.team6.onandthefarm.service.order.OrderService;
import com.team6.onandthefarm.service.order.OrderServiceImp;
import com.team6.onandthefarm.util.BaseResponse;
import com.team6.onandthefarm.vo.order.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/orders")
@Api(value = "주문",description = "주문 상태\n" +
        " * os0 : 주문완료\n" +
        " * os1 : 주문취소\n" +
        " * os2 : 반품신청\n" +
        " * os3 : 반품확정\n" +
        " * os4 : 배송 중\n" +
        " * os5 : 배송 완료")
public class OrderController {
    private OrderService orderService;

    @Autowired
    public OrderController(OrderServiceImp orderServiceImp) {
        this.orderService = orderServiceImp;
    }

    @PostMapping("/sheet")
    @ApiOperation(value = "단건 주문서 조회")
    public ResponseEntity<BaseResponse<OrderSheetResponse>> findOneOrder(@RequestBody OrderSheetRequest orderSheetRequest){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        OrderSheetDto orderSheetDto = modelMapper.map(orderSheetRequest,OrderSheetDto.class);
        OrderSheetResponse result = orderService.findOneByProductId(orderSheetDto);
        BaseResponse<OrderSheetResponse> response = BaseResponse.<OrderSheetResponse>builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(result)
                .build();
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
    public ResponseEntity<BaseResponse> createOrder(@RequestBody OrderRequest orderRequest){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        OrderDto orderDto = OrderDto.builder()
                .orderRecipientName(orderRequest.getOrderRecipientName())
                .orderRequest(orderRequest.getOrderRequest())
                .orderPhone(orderRequest.getOrderPhone())
                .orderAddress(orderRequest.getOrderAddress())
                .userId(orderRequest.getUserId())
                .productList(new ArrayList<>())
                .build();
        // productId->sellerId를 찾기
        /*
            key : productId
            value : sellerId
            prodSeller는 담을 공간
         */
        Map<Long,Long> prodSeller = new HashMap<>();
        for(OrderProductRequest order : orderRequest.getProductList()){
            OrderProductDto orderProductDto = OrderProductDto.builder()
                    .productQty(order.getProductQty())
                    .productId(order.getProductId())
                    .build();
            orderDto.getProductList().add(orderProductDto);
            prodSeller.put(order.getProductId(), orderProductDto.getSellerId());
        }
        orderDto.setProdSeller(prodSeller);
        orderService.createOrder(orderDto);

        BaseResponse response = BaseResponse.builder().httpStatus(HttpStatus.OK).message("OK").build();

        return new ResponseEntity(response,HttpStatus.OK);
    }

    /**
     * 셀러의 경우 주문당 여러 제품을 한번에 보여주어야 함
     * orderId : ListProduct정보
     * @param orderSellerRequest
     * @return
     */
    @PostMapping("/seller/list")
    @ApiOperation(value = "주문 내역 조회")
    public ResponseEntity<BaseResponse<List<OrderSellerResponseList>>> findSellerAllOrders(@RequestBody OrderSellerRequest orderSellerRequest){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        OrderSellerFindDto orderSellerFindDto = modelMapper.map(orderSellerRequest, OrderSellerFindDto.class);
        List<OrderSellerResponseList> responses  = orderService.findSellerOrders(orderSellerFindDto);
        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(responses)
                .build();
        return new ResponseEntity(response,HttpStatus.OK);
    }

    @GetMapping("/list/{order-no}")
    @ApiOperation(value = "주문 상세 조회")
    public ResponseEntity<BaseResponse<OrderSellerDetailResponse>> findSellerOrderDetail(@PathVariable(name = "order-no") String orderSerial){
        OrderSellerDetailResponse detailResponse = orderService.findSellerOrderDetail(orderSerial);
        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(detailResponse)
                .build();
        return new ResponseEntity(response,HttpStatus.OK);
    }

    @PostMapping("/claim/cancel")
    @ApiOperation(value = "취소 생성" )
    public ResponseEntity<BaseResponse<Boolean>> createCancel(@RequestBody RefundRequest refundRequest){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        RefundDto refundDto = modelMapper.map(refundRequest, RefundDto.class);
        boolean result = orderService.createCancel(refundDto);
        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(Boolean.valueOf(result))
                .build();
        return new ResponseEntity(response,HttpStatus.OK);
    }

    @PostMapping("/claim/refund")
    @ApiOperation(value = "반품 생성" )
    public ResponseEntity<BaseResponse<Boolean>> createRefund(@RequestBody RefundRequest refundRequest){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        RefundDto refundDto = modelMapper.map(refundRequest, RefundDto.class);
        boolean result = orderService.createRefund(refundDto);
        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(Boolean.valueOf(result))
                .build();
        return new ResponseEntity(response,HttpStatus.OK);
    }

    @PostMapping("/claim/list")
    @ApiOperation(value = "취소/반품 내역 조회")
    public ResponseEntity<BaseResponse<List<OrderSellerResponse>>> findSellerClaims(@RequestBody OrderSellerRequest orderSellerRequest){
        List<OrderSellerResponse> responseList = orderService.findSellerClaims(orderSellerRequest);
        BaseResponse<List<OrderSellerResponse>> response = BaseResponse.<List<OrderSellerResponse>>builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(responseList)
                .build();
        return new ResponseEntity(response,HttpStatus.OK);
    }

    @GetMapping("/claim/list/{orderProduct-no}")
    @ApiOperation(value = "취소/반품 상세 조회")
    public ResponseEntity<BaseResponse<RefundDetailResponse>> findSellerClaimDetail(@PathVariable(name = "orderProduct-no") String orderProductId){
        RefundDetailResponse refundDetailResponse = orderService.findRefundDetail(Long.valueOf(orderProductId));
        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(refundDetailResponse)
                .build();
        return new ResponseEntity(response,HttpStatus.OK);
    }

    @PostMapping("/claim/list/{orderProduct-no}")
    @ApiOperation(value = "반품 확정")
    public ResponseEntity<BaseResponse> claimConform(@PathVariable(name = "orderProduct-no") String orderProductId){
        Boolean result = orderService.conformRefund(Long.valueOf(orderProductId));
        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(result.booleanValue())
                .build();
        return new ResponseEntity(response,HttpStatus.OK);
    }

    @PostMapping("/delivery")
    @ApiOperation(value = "배송 시작 처리")
    public ResponseEntity<BaseResponse> deliveryStart(@RequestBody OrderDeliveryRequest orderDeliveryRequest){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        OrderDeliveryDto orderDeliveryDto = modelMapper.map(orderDeliveryRequest, OrderDeliveryDto.class);
        Boolean result = orderService.deliveryStart(orderDeliveryDto);
        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(result.booleanValue())
                .build();
        return new ResponseEntity(response,HttpStatus.OK);
    }

    @PostMapping("/delivery/{order-no}")
    @ApiOperation(value = "배송 완료 처리")
    public ResponseEntity<BaseResponse> deliveryConform(@PathVariable(name = "order-no") String orderSerial){
        Boolean result = orderService.deliveryConform(orderSerial);
        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(result.booleanValue())
                .build();
        return new ResponseEntity(response,HttpStatus.OK);
    }


}
