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
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user/orders")
@Api(value = "주문",description = "주문 상태\n" +
        " * activated(os0) : 주문완료\n" +
        " * canceled(os1) : 주문취소\n" +
        " * refundRequest(os2) : 반품신청\n" +
        " * refundCompleted(os3) : 반품확정\n" +
        " * deliveryProgress(os4) : 배송 중\n" +
        " * deliveryCompleted(os5) : 배송 완료")
public class UserOrderController {
    private OrderService orderService;

    @Autowired
    public UserOrderController(OrderServiceImp orderServiceImp) {
        this.orderService = orderServiceImp;
    }

    @PostMapping("/sheet")
    @ApiOperation(value = "단건 주문서 조회")
    public ResponseEntity<BaseResponse<OrderSheetResponse>> findOneOrder(
            @ApiIgnore Principal principal, @RequestBody OrderSheetRequest orderSheetRequest){

        String[] principalInfo = principal.getName().split(" ");
        Long userId = Long.parseLong(principalInfo[0]);

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        OrderSheetDto orderSheetDto = modelMapper.map(orderSheetRequest,OrderSheetDto.class);
        orderSheetDto.setUserId(userId);

        OrderSheetResponse result = orderService.findOneByProductId(orderSheetDto);

        BaseResponse<OrderSheetResponse> response = BaseResponse.<OrderSheetResponse>builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(result)
                .build();
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/carts")
    @ApiOperation(value = "다건 주문서 조회")
    public ResponseEntity<List<OrderFindOneResponse>> findOrders(@ApiIgnore Principal principal){

        String[] principalInfo = principal.getName().split(" ");
        Long userId = Long.parseLong(principalInfo[0]);

        List<OrderFindOneResponse> responseList
                = orderService.findCartByUserId(userId);

        return new ResponseEntity(responseList,HttpStatus.OK);
    }

    /**
     * 주문 생성할때 product엔티티에서 product상태가 p0 인경우만 판매되게 하는 코드 넣어야 함
     * @param orderRequest
     * @return
     */
    @PostMapping()
    @ApiOperation(value = "주문 생성")
    public ResponseEntity<BaseResponse> createOrder(
            @ApiIgnore Principal principal,@RequestBody OrderRequest orderRequest){

        String[] principalInfo = principal.getName().split(" ");
        Long userId = Long.parseLong(principalInfo[0]);

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        OrderDto orderDto = OrderDto.builder()
                .orderRecipientName(orderRequest.getOrderRecipientName())
                .orderRequest(orderRequest.getOrderRequest())
                .orderPhone(orderRequest.getOrderPhone())
                .orderAddress(orderRequest.getOrderAddress())
                .userId(userId)
                .productList(new ArrayList<>())
                .build();

        for(OrderProductRequest order : orderRequest.getProductList()){
            OrderProductDto orderProductDto = OrderProductDto.builder()
                    .productQty(order.getProductQty())
                    .productId(order.getProductId())
                    .build();
            orderDto.getProductList().add(orderProductDto);
        }

        orderService.createOrder(orderDto);

        BaseResponse response = BaseResponse.builder().httpStatus(HttpStatus.OK).message("OK").build();

        return new ResponseEntity(response,HttpStatus.OK);
    }


    @PostMapping("/list")
    @ApiOperation(value = "유저 주문 내역 조회")
    public ResponseEntity<BaseResponse<OrderUserResponseListResponse>> findUserAllOrders(
            @ApiIgnore Principal principal, @RequestBody OrderUserRequest orderUserRequest){

        String[] principalInfo = principal.getName().split(" ");
        String userId = principalInfo[0];

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        OrderUserFindDto orderUserFindDto = modelMapper.map(orderUserRequest, OrderUserFindDto.class);
        orderUserFindDto.setUserId(userId);
        OrderUserResponseListResponse responses  = orderService.findUserOrders(orderUserFindDto);
        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(responses)
                .build();
        return new ResponseEntity(response,HttpStatus.OK);
    }

    @GetMapping("/list/{order-no}")
    @ApiOperation(value = "유저 주문 상세 조회")
    public ResponseEntity<BaseResponse<OrderUserDetailResponse>> findSellerOrderDetail(
            @PathVariable(name = "order-no") String orderSerial){
        OrderUserDetailResponse detailResponse = orderService.findUserOrderDetail(orderSerial);
        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(detailResponse)
                .build();
        return new ResponseEntity(response,HttpStatus.OK);
    }


    @PostMapping("/claim/cancel")
    @ApiOperation(value = "취소 생성" )
    public ResponseEntity<BaseResponse<Boolean>> createCancel(
            @ApiIgnore Principal principal, @RequestBody RefundRequest refundRequest){

        String[] principalInfo = principal.getName().split(" ");
        Long userId = Long.parseLong(principalInfo[0]);

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        RefundDto refundDto = modelMapper.map(refundRequest, RefundDto.class);
        refundDto.setUserId(userId);
        Boolean result = orderService.createCancel(refundDto);
        if(result.booleanValue()){
            BaseResponse response = BaseResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("OK")
                    .data(result)
                    .build();
            return new ResponseEntity(response,HttpStatus.OK);
        }
        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message("BAD_REQUEST")
                .data(result)
                .build();
        return new ResponseEntity(response,HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/claim/refund")
    @ApiOperation(value = "반품 생성" )
    public ResponseEntity<BaseResponse<Boolean>> createRefund(
            @ApiIgnore Principal principal, @RequestBody RefundRequest refundRequest){

        String[] principalInfo = principal.getName().split(" ");
        Long userId = Long.parseLong(principalInfo[0]);

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        RefundDto refundDto = modelMapper.map(refundRequest, RefundDto.class);
        refundDto.setUserId(userId);
        boolean result = orderService.createRefund(refundDto);
        BaseResponse response = BaseResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(Boolean.valueOf(result))
                .build();
        return new ResponseEntity(response,HttpStatus.OK);
    }

    @PostMapping("/claim/list")
    @ApiOperation(value = "유저 취소/반품 내역 조회")
    public ResponseEntity<BaseResponse<OrderRefundResultResponse>> findUserClaims(
            @ApiIgnore Principal principal, @RequestBody OrderUserRequest orderUserRequest){

        String[] principalInfo = principal.getName().split(" ");
        String userId = principalInfo[0];

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        OrderUserFindDto orderUserFindDto = modelMapper.map(orderUserRequest,OrderUserFindDto.class);
        orderUserFindDto.setUserId(userId);
        OrderRefundResultResponse responseList = orderService.findUserClaims(orderUserFindDto);
        BaseResponse<OrderRefundResultResponse> response = BaseResponse.<OrderRefundResultResponse>builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(responseList)
                .build();
        return new ResponseEntity(response,HttpStatus.OK);
    }

}
