package com.example.JpaShop.service;

import com.example.JpaShop.Repository.ItemRepository;
import com.example.JpaShop.Repository.MemberRepository;
import com.example.JpaShop.Repository.OrderRepository;
import com.example.JpaShop.Repository.OrderSearch;
import com.example.JpaShop.domain.*;
import com.example.JpaShop.domain.item.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ItemRepository itemRepository;


    /**
     * 주문
     * 대부분 엔티티에 있는 비즈니스 로직에 위임하고 있다. 이게 바로 도메인 모델 패턴/ 도메인 주도 개발.
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count){

        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order); // order만 저장해줘도 cascade속성 때문에 delivery랑 OrderItem은 따로 저장 안해줘도 됨.
        // 오더만가능. 오더아이템, 딜리버리의 생명을 cascade로 관리하고 있기때문에 쓸 수 있따.


        return order.getId();
    }

    /**
     * 주문취소
     */

    @Transactional
    public void cancelOrder(Long orderId){
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);

        //주문 최소
        order.cancel();
    }


    /**
     * 검색
     */
    public List<Order> findOrders(OrderSearch orderSearch){

        return orderRepository.search(orderSearch);
    }


}
