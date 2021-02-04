package bnilive.bnishop.controller;

import static java.util.stream.Collectors.toList;

import bnilive.bnishop.repository.order.simplequery.OrderSimpleQueryDto;
import bnilive.bnishop.repository.order.simplequery.OrderSimpleQueryRepository;
import bnilive.bnishop.domain.Order;
import bnilive.bnishop.domain.OrderSearch;
import bnilive.bnishop.repository.OrderRepository;
import java.util.List;
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
  private final OrderSimpleQueryRepository orderSimpleQueryRepository;

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
  public List<OrderSimpleQueryDto> ordersV2() {
    // ORDER 2개
    // N + 1 -> 1 + 회원 N + 배송 N
    List<Order> orders = orderRepository.findAllByString(new OrderSearch());
    return orders.stream().map(OrderSimpleQueryDto::new).collect(toList());
  }

  @GetMapping("/api/v3/simple-orders")
  public List<OrderSimpleQueryDto> ordersV3() {
    List<Order> orders = orderRepository.findAllWithMemberDelivery();
    return orders.stream().map(OrderSimpleQueryDto::new).collect(toList());
  }

  @GetMapping("/api/v4/simple-orders")
  public List<OrderSimpleQueryDto> ordersV4() {
    return orderSimpleQueryRepository.findOrderDtos();
  }

}