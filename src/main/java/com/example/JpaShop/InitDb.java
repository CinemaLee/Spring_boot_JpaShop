package com.example.JpaShop;

import com.example.JpaShop.domain.*;
import com.example.JpaShop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb { // 단순 데이터를 넣어놓기 위함.


    private final InitService initService;


    @PostConstruct // 애플리케이션 로딩시점에 얘를 넣어주고 싶은거임.
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }



    @Component
    @Transactional
    static class  InitService {

        @Autowired
        private EntityManager em;

        public void dbInit1() {
            Member member = createMember("userA", "서울", "1", "1111");

            Book book1 = createBook("JPA1 BOOK", 10000, 100);


            Book book2 = createBook("JPA2 BOOK", 20000, 100);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = createDelivery(member);

            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);

        }

        public void dbInit2() {
            Member member = createMember("userB", "부산", "2", "2222");

            Book book1 = createBook("Spring1 BOOK", 20000, 200);


            Book book2 = createBook("Spring2 BOOK", 30000, 300);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 20000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 30000, 4);

            Delivery delivery = createDelivery(member);

            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);

        }

        private Member createMember(String name, String address, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(address, street, zipcode));
            em.persist(member);
            return member;
        }

        private Book createBook(String name, int price, int stockQuantity) {
            Book book1 = new Book();
            book1.setName(name);
            book1.setPrice(price);
            book1.setStockQuantity(stockQuantity);
            em.persist(book1);
            return book1;
        }

        private Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }
    }
}

