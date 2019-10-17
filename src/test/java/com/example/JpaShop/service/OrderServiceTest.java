package com.example.JpaShop.service;

import com.example.JpaShop.Repository.OrderRepository;
import com.example.JpaShop.domain.Address;
import com.example.JpaShop.domain.Member;
import com.example.JpaShop.domain.Order;
import com.example.JpaShop.domain.OrderStatus;
import com.example.JpaShop.domain.item.Book;
import com.example.JpaShop.domain.item.Item;
import com.example.JpaShop.exception.NotEnoughStockException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;


    @Test
    public void order() throws Exception{
        Member member = createMember();

        Book book = createBook("leebook", 10000, 10);

        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);


        //then
        Order getOrder = orderRepository.findOne(orderId);

        Assert.assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다.", 1,getOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격*수량이다.",10000 * orderCount, getOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 한다.",8,book.getStockQuantity());
    }


    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception{
        //given
        Member member = createMember();
        Item item = createBook("leebook", 10000, 10);

        int orderCount=11;


        //when
        orderService.order(member.getId(), item.getId(), orderCount);


        //then
        fail("재고 수량 부족 예외가 발생해야 한다.");
    }



    @Test
    public void 주문취소() throws Exception{
        //given
        Member member = createMember();
        Book book = createBook("ParkBook", 10000, 10);
        int orderCount=2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getorder = orderRepository.findOne(orderId);
        assertEquals("주문 취소시 상태는 CANCEL이다.",OrderStatus.CANCEL,getorder.getStatus());
        assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야 한다.",10,book.getStockQuantity());
    }



    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","오목로","123-123"));
        em.persist(member);
        return member;
    }
}