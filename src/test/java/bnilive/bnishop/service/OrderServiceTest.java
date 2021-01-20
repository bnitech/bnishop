package bnilive.bnishop.service;

import bnilive.bnishop.domain.Address;
import bnilive.bnishop.domain.Member;
import bnilive.bnishop.domain.Order;
import bnilive.bnishop.domain.OrderStatus;
import bnilive.bnishop.domain.item.Book;
import bnilive.bnishop.domain.item.Item;
import bnilive.bnishop.exception.NotEnoughStockException;
import bnilive.bnishop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    void 상품주문() throws Exception {
        // region given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10); //이름, 가격, 재고
        int orderCount = 2;
        // endregion

        // region when
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        // endregion

        // region then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다.", 1, getOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량이다.", 10000 * 2, getOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 한다.", 8, item.getStockQuantity());
        // endregion
    }

    @Test
    void 상품주문_재고수량초과() throws Exception {
        // region given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10); //이름, 가격, 재고
        int orderCount = 11; //재고보다 많은 수량

        Exception expectedException = new NotEnoughStockException("need more stock");
        // endregion

        // region when
        Exception actualException = assertThrows(NotEnoughStockException.class,
                () -> orderService.order(member.getId(), item.getId(), orderCount));
        // endregion

        // region then
        assertEquals("재고 수량 부족 예외가 발생해야 한다.", expectedException.getMessage(), actualException.getMessage());
        // endregion
    }

    @Test
    void 상품취소() throws Exception {
        // region given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10); //이름, 가격, 재고
        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        // endregion

        // region when
        orderService.cancelOrder(orderId);
        // endregion

        // region then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals("주문 취소시 상태는 CANCEL 이다.", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야 한다.", 10, item.getStockQuantity());
        // endregion
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setStockQuantity(stockQuantity);
        book.setPrice(price);
        em.persist(book);
        return book;
    }
}