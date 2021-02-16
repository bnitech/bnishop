package bnilive.bnishop.api;

import bnilive.bnishop.domain.Order;
import bnilive.bnishop.domain.OrderItem;
import bnilive.bnishop.domain.OrderSearch;
import bnilive.bnishop.repository.OrderRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OderApiController {

  private final OrderRepository orderRepository;

  @GetMapping("/api/v1/orders")
  public List<Order> ordersV1() {

    List<Order> all = orderRepository.findAllByString(new OrderSearch());

    for (Order order : all) {
      order.getMember().getName();
      order.getDelivery().getAddress();

      List<OrderItem> orderItems = order.getOrderItems();
      orderItems.stream().forEach(o -> o.getItem().getName());
    }

    return all;
  }
}
