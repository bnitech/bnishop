package bnilive.bnishop.controller;

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

  @GetMapping("/api/v1/simple-orders")
  public List<Order> ordersV1() {

    List<Order> all = orderRepository.findAllByString(new OrderSearch());

    for (Order order : all) {
      order.getMember().getName();
      order.getDelivery().getAddress();
    }

    return all;
  }

}
