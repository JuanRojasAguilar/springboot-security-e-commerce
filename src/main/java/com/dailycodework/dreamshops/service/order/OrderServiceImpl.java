package com.dailycodework.dreamshops.service.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.dailycodework.dreamshops.dto.OrderDto;
import com.dailycodework.dreamshops.enums.OrderStatus;
import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Cart;
import com.dailycodework.dreamshops.model.Order;
import com.dailycodework.dreamshops.model.OrderItem;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.repository.CartRepository;
import com.dailycodework.dreamshops.repository.OrderRepository;
import com.dailycodework.dreamshops.repository.ProductRepository;
import com.dailycodework.dreamshops.service.cart.CartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
  private final OrderRepository orderRepository;
  private final ProductRepository productRepository;
  private final CartRepository cartRepository;
  private final CartService cartService;
  private final ModelMapper modelMapper;

  @Override
  public OrderDto placeOrder(Long userId) {
    Cart cart = cartRepository.findByUserId(userId);
    Order order = createOrder(cart);
    List<OrderItem> orderItems = createOrderItems(order, cart);
    order.setOrderItems(new HashSet<>(orderItems));
    order.setOrderTotal(calculateTotalOrderPrice(orderItems));
    Order savedOrder = orderRepository.save(order);
    cartService.clearCart(cart.getId());

    return convertToDto(savedOrder);
  }

  private Order createOrder(Cart cart) {
    Order order = new Order();

    order.setUser(cart.getUser());
    order.setOrderStatus(OrderStatus.PENDING);
    order.setOrderDate(LocalDate.now());

    return orderRepository.save(order);
  }

  private List<OrderItem> createOrderItems(Order order, Cart cart) {
    return cart.getCartItems().stream()
            .map(cartItem -> {
              Product product = cartItem.getProduct();
              product.setQuantity(product.getQuantity() - cartItem.getQuantity());
              productRepository.save(product);
              return new OrderItem(
                order,
                product,
                cartItem.getQuantity(),
                cartItem.getUnitPrice()
              );
            }).toList();
  }


  private BigDecimal calculateTotalOrderPrice(List<OrderItem> orderItems) {
    return orderItems.stream()
        .map(item -> item
            .getPrice()
            .multiply(new BigDecimal(item.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  @Override
  public OrderDto getOrder(Long orderId) {
    return orderRepository
        .findById(orderId)
        .map(this::convertToDto)
        .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
  }

  @Override
  public List<OrderDto> getUserOrders(Long userId) {
    return orderRepository
            .findByUserId(userId)
            .stream()
            .map(this::convertToDto)
            .toList();
  }

  private OrderDto convertToDto(Order order) {
    return modelMapper.map(order, OrderDto.class);
  }

}
