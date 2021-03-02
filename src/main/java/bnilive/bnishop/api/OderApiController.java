package bnilive.bnishop.api;

import static java.util.stream.Collectors.toList;

import bnilive.bnishop.domain.Address;
import bnilive.bnishop.domain.Order;
import bnilive.bnishop.domain.OrderItem;
import bnilive.bnishop.domain.OrderSearch;
import bnilive.bnishop.domain.OrderStatus;
import bnilive.bnishop.repository.OrderRepository;
import bnilive.bnishop.repository.order.query.OrderQueryDto;
import bnilive.bnishop.repository.order.query.OrderQueryRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OderApiController {

  private final OrderRepository orderRepository;
  private final OrderQueryRepository orderQueryRepository;

  @GetMapping("/api/v1/orders")
  public List<Order> ordersV1() {

    List<Order> all = orderRepository.findAllByString(new OrderSearch());

    for (Order order : all) {
      order.getMember().getName();
      order.getDelivery().getAddress();

      List<OrderItem> orderItems = order.getOrderItems();
      orderItems.forEach(o -> o.getItem().getName());
    }

    return all;
  }

  @GetMapping("/api/v2/orders")
  public List<OrderDto> ordersV2() {
    return orderRepository.findAllByString(new OrderSearch()).stream()
        .map(OrderDto::new)
        .collect(toList());
  }

  @GetMapping("/api/v3/orders")
  public List<OrderDto> ordersV3() {
    return orderRepository.findAllWithItem().stream()
        .map(OrderDto::new)
        .collect(toList());
  }

  @GetMapping("/api/v3.1/orders")
  public List<OrderDto> ordersV3_page(
      @RequestParam(value = "offset", defaultValue = "0") int offset,
      @RequestParam(value = "limit", defaultValue = "100") int limit) {

    List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);

    return orders.stream().map(OrderDto::new).collect(toList());
  }

  @GetMapping("/api/v4/orders")
  public List<OrderQueryDto> ordersV4() {
    return orderQueryRepository.findOrderQueryDtos();
  }

  @Data
  static class OrderDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemDto> orderItems;

    public OrderDto(Order order) {
      orderId = order.getId();
      name = order.getMember().getName();
      orderDate = order.getOrderDate();
      orderStatus = order.getStatus();
      address = order.getDelivery().getAddress();
      orderItems = order.getOrderItems().stream()
          .map(OrderItemDto::new)
          .collect(toList());
    }
  }

  @Data
  static class OrderItemDto {

    private String itemName;
    private int orderPrice;
    private int count;

    public OrderItemDto(OrderItem orderItem) {
      itemName = orderItem.getItem().getName();
      orderPrice = orderItem.getOrderPrice();
      count = orderItem.getCount();
    }
  }
}
