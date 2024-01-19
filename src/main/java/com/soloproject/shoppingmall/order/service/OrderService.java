package com.soloproject.shoppingmall.order.service;

import com.soloproject.shoppingmall.cart.entity.Cart;
import com.soloproject.shoppingmall.cart.entity.CartProduct;
import com.soloproject.shoppingmall.cart.repository.CartRepository;
import com.soloproject.shoppingmall.exception.BusinessLogicException;
import com.soloproject.shoppingmall.exception.ExceptionCode;
import com.soloproject.shoppingmall.order.dto.OrderPostDto;
import com.soloproject.shoppingmall.order.dto.OrderProductDto;
import com.soloproject.shoppingmall.order.entity.Order;
import com.soloproject.shoppingmall.order.entity.OrderProduct;
import com.soloproject.shoppingmall.order.mapper.OrderMapper;
import com.soloproject.shoppingmall.order.repository.OrderRepository;
import com.soloproject.shoppingmall.product.entity.Product;
import com.soloproject.shoppingmall.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Transactional
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public Order createOrder(Order order) {

        int totalPrice = calculateTotalPrice(order);
        order.setTotalPrice(totalPrice);
        order.setOrderStatus(Order.OrderStatus.ORDER_REQUEST);

        return orderRepository.save(order);
    }

    public Order cartOrder(long cartId) {

        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.CART_EMPTY));
        int totalPrice = cart.getTotalPrice();

        Order order = new Order();

        order.setMember(cart.getMember());

        List<OrderProduct> orderProducts = new ArrayList<>();

        for (CartProduct cartProduct : cart.getCartProducts()){
            OrderProduct orderProduct = new OrderProduct();
            Product product = cartProduct.getProduct();

            orderProduct.setProduct(product);
            orderProduct.setOrder(order);
            orderProduct.setQuantity(cartProduct.getQuantity());

            orderProducts.add(orderProduct);
        }

        order.setOrderProducts(orderProducts);
        order.setTotalPrice(totalPrice);
        order.setOrderStatus(Order.OrderStatus.ORDER_REQUEST);

        orderRepository.save(order);

        return order;
    }


    public int calculateTotalPrice(Order order) {

        List<OrderProduct> orderProducts = order.getOrderProducts();
        List<Integer> quantity = orderProducts.stream().map(OrderProduct::getQuantity).toList();

        List<Long> productIds = orderProducts.stream().map(orderProduct -> orderProduct.getProduct().getProductId()).toList();
        List<Product> products = productRepository.findAllById(productIds);

        int totalPrice = IntStream.range(0, quantity.size())
                .map(i->
                {
                    Product product = products.get(i);
                    int price = product.getPrice();
                    return price * quantity.get(i);
                }).sum();

        return totalPrice;
    }

    public Order getOrder(long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        return order;
    }
}
