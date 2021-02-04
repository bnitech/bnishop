package bnilive.bnishop.controller;

import static java.util.stream.Collectors.toList;

import bnilive.bnishop.domain.Address;
import bnilive.bnishop.domain.Order;
import bnilive.bnishop.domain.OrderSearch;
import bnilive.bnishop.domain.OrderStatus;
import bnilive.bnishop.repository.OrderRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * xToOne (ManyToOne, OneToOne) Order Order -> Member Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OderSimpleApiController {

  private final OrderRepository orderRepository;

  @GetMapping("/api/v1/simple-orders")
  public List<Order> ordersV1() {

    List<Order> all = orderRepository.findAllByString(new OrderSearch());

    for (Order order : all) {
      order.getMember().getName();
      order.getDelivery().getAddress();
    }

    return all;
  }

  @GetMapping("/api/v2/simple-orders")
  public List<SimpleOrderDto> ordersV2() {
    // ORDER 2개
    // N + 1 -> 1 + 회원 N + 배송 N
    List<Order> orders = orderRepository.findAllByString(new OrderSearch());
    return orders.stream().map(SimpleOrderDto::new).collect(toList());
  }

  @Data
  static class SimpleOrderDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate; //주문시간
    private OrderStatus orderStatus;
    private Address address;

    public SimpleOrderDto(Order order) {
      orderId = order.getId();
      name = order.getMember().getName();
      orderDate = order.getOrderDate();
      orderStatus = order.getStatus();
      address = order.getDelivery().getAddress();
    }
  }
}