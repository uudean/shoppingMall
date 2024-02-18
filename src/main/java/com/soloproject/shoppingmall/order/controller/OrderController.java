package com.soloproject.shoppingmall.order.controller;

import com.soloproject.shoppingmall.order.dto.OrderPostDto;
import com.soloproject.shoppingmall.order.dto.OrderResponseDto;
import com.soloproject.shoppingmall.order.entity.Order;
import com.soloproject.shoppingmall.order.mapper.OrderMapper;
import com.soloproject.shoppingmall.order.service.OrderService;
import com.soloproject.shoppingmall.response.MultiResponseDto;
import com.soloproject.shoppingmall.response.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RequestMapping("/order")
@RestController
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @PostMapping
    public ResponseEntity createOrder(@RequestBody OrderPostDto orderPostDto) {

        Order order = orderService.createOrder(orderMapper.orderPostDtoToOrder(orderPostDto));
        OrderResponseDto response = orderMapper.orderToOrderResponseDto(order);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.CREATED);
    }

    @PostMapping("/cart/{cart-id}")
    public ResponseEntity cartOrder(@PathVariable("cart-id") long cartId) {

        Order order = orderService.cartOrder(cartId);
        OrderResponseDto response = orderMapper.orderToOrderResponseDto(order);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.CREATED);
    }

    @GetMapping("/{order-id}")
    public ResponseEntity getOrder(@PathVariable("order-id") long orderId){

        Order order = orderService.getOrder(orderId);
        OrderResponseDto response = orderMapper.orderToOrderResponseDto(order);

        return new ResponseEntity<>(new SingleResponseDto<>(response),HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity getOrderList(@RequestParam int page,
                                       @RequestParam int size,
                                       @RequestParam long type){

        Page<Order> orderPage = orderService.getOrderList(page-1,size,type);
        List<OrderResponseDto> response = orderMapper.ordersToOrderResponseDtos(orderPage.getContent());

        return new ResponseEntity<>(new MultiResponseDto<>(response,orderPage),HttpStatus.OK);
    }
}
