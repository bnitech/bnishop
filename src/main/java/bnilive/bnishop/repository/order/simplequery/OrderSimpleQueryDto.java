package bnilive.bnishop.repository.order.simplequery;

import bnilive.bnishop.domain.Address;
import bnilive.bnishop.domain.Order;
import bnilive.bnishop.domain.OrderStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderSimpleQueryDto {

  private Long orderId;
  private String name;
  private LocalDateTime orderDate; //주문시간
  private OrderStatus orderStatus;
  private Address address;

  public OrderSimpleQueryDto(Order order) {
    orderId = order.getId();
    name = order.getMember().getName();
    orderDate = order.getOrderDate();
    orderStatus = order.getStatus();
    address = order.getDelivery().getAddress();
  }
}
