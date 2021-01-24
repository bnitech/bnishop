package bnilive.bnishop.service;

import bnilive.bnishop.domain.Delivery;
import bnilive.bnishop.domain.DeliveryStatus;
import bnilive.bnishop.domain.Member;
import bnilive.bnishop.domain.Order;
import bnilive.bnishop.domain.OrderItem;
import bnilive.bnishop.domain.OrderSearch;
import bnilive.bnishop.domain.item.Item;
import bnilive.bnishop.repository.ItemRepository;
import bnilive.bnishop.repository.MemberRepository;
import bnilive.bnishop.repository.OrderRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

  private final MemberRepository memberRepository;
  private final OrderRepository orderRepository;
  private final ItemRepository itemRepository;

  @Transactional
  public Long order(Long memberId, Long itemId, int count) {

    //엔티티 조회
    Member member = memberRepository.findOne(memberId);
    Item item = itemRepository.findOne(itemId);

    //배송정보 생성
    Delivery delivery = new Delivery();
    delivery.setAddress(member.getAddress());
    delivery.setStatus(DeliveryStatus.READY);

    //주문상품 생성
    OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

    //주문 생성
    Order order = Order.createOrder(member, delivery, orderItem);

    //주문 저장
    orderRepository.save(order);
    return order.getId();
  }

  @Transactional
  public void cancelOrder(Long orderId) {

    //주문 엔티티 조회
    Order order = orderRepository.findOne(orderId);

    //주문 취소
    order.cancel();
  }

  public List<Order> findOrders(OrderSearch orderSearch) {
    return orderRepository.findAllByString(orderSearch);
  }
}