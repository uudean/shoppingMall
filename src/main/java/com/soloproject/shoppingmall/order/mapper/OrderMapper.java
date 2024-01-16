package com.soloproject.shoppingmall.order.mapper;

import com.soloproject.shoppingmall.member.entity.Member;
import com.soloproject.shoppingmall.order.dto.OrderPostDto;
import com.soloproject.shoppingmall.order.dto.OrderProductResponseDto;
import com.soloproject.shoppingmall.order.dto.OrderResponseDto;
import com.soloproject.shoppingmall.order.entity.Order;
import com.soloproject.shoppingmall.order.entity.OrderProduct;
import com.soloproject.shoppingmall.product.entity.Product;
import com.soloproject.shoppingmall.product.mapper.ProductMapper;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",uses={ProductMapper.class})
public interface OrderMapper {


    default Order orderPostDtoToOrder(OrderPostDto orderPostDto) {
        Order order = new Order();
        Member member = new Member();
        member.setMemberId(orderPostDto.getMemberId());

        List<OrderProduct> orderProducts = orderPostDto.getOrderProducts().stream()
                .map(orderProductDto -> {
                    OrderProduct orderProduct = new OrderProduct();
                    Product product = new Product();
                    product.setProductId(orderProductDto.getProductId());
                    orderProduct.setOrder(order);
                    orderProduct.setProduct(product);
                    orderProduct.setQuantity(orderProductDto.getQuantity());
                    return orderProduct;
                }).collect(Collectors.toList());
        order.setMember(member);
        order.setOrderProducts(orderProducts);

        return order;
    }

    default OrderResponseDto orderToOrderResponseDto(Order order) {

        if (order == null) {
            return null;
        }


        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setMemberId(order.getMember().getMemberId());
        orderResponseDto.setCreatedAt(order.getCreatedAt());
        orderResponseDto.setModifiedAt(order.getModifiedAt());
        orderResponseDto.setOrderId(order.getOrderId());
        orderResponseDto.setOrderProducts(orderProductListToOrderProductResponseDtoList(order.getOrderProducts()));
        orderResponseDto.setTotalPrice(order.getTotalPrice());

        return orderResponseDto;
    }


    default List<OrderProductResponseDto> orderProductListToOrderProductResponseDtoList(List<OrderProduct> list) {
        if (list == null) {
            return null;
        }

        List<OrderProductResponseDto> list1 = new ArrayList<OrderProductResponseDto>(list.size());
        for (OrderProduct orderProduct : list) {
            list1.add(orderProductToOrderProductResponseDto(orderProduct));
        }

        return list1;
    }

    default OrderProductResponseDto orderProductToOrderProductResponseDto(OrderProduct orderProduct) {
        if (orderProduct == null) {
            return null;
        }

        OrderProductResponseDto orderProductResponseDto = new OrderProductResponseDto();

        orderProductResponseDto.setProductId(orderProduct.getProduct().getProductId());
        orderProductResponseDto.setQuantity(orderProduct.getQuantity());

        return orderProductResponseDto;
    }


}
