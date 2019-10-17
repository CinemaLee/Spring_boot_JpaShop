package com.example.JpaShop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name="order_id") // 컬럼 설정.
    private Long id;

    @OneToMany(mappedBy = "order" , cascade = CascadeType.ALL) // Order를 persist하면 orderItems도 persist해주겠다는 뜻.
    private List<OrderItem> orderItems = new ArrayList<>(); // 필드에서 초기화 꼭 해주자.

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL) // X to One => 무조건 LAZY타입으로.
    @JoinColumn(name = "delivery_id") // 1:1에서는 주인을 아무곳에 둬도 되지만 보통 오더를 통해 배송에 접근하니까 오더로 설정.
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태. Enum타입.[ORDER, CANCEL]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // 포린키가 member가 아니라 member_id로 된다는 뜻.
    private Member member;



    //==연관관계 편의 메서드==// 연관관계에 있는 애들 사이의 값세팅을 위한 메서드
    // 위치는 두 엔티티중 컨트롤하는 쪽이 들고 있으면 좋음.
    public void setMember(Member member){ // 오더 랑 멤버중 오더를 이용해 멤버를 컨트롤
        this.member = member; // 멤버에도 값 세팅
        member.getOrders().add(this); // 멤버랑 연관관계에 있는 오더에도 값 세팅.

        // 이 메서드가 없다면 메인에서 이렇게 세팅해줘야해
        /*Member member = new Member();
        Order order = new Order();

        member.getOrders().add(order);  근데 둘 중 하나라도 까먹을 수 있자나 그래서 만들어주는 것.
        order.setMember(member);*/
    }

    public void addOrderItem(OrderItem orderItem) { // 오더랑 오더 아이템중 오더를 이용해 컨트롤
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){ // 오더랑 딜리버리중 오더를 이용해 컨트롤
        this.delivery=delivery;
        delivery.setOrder(this);
    }



    // 이렇게 엔티티가 비즈니스 로직을 가지고 객체 지향의 특성을 활용하는 것을 도메인 모델 패턴이라 한다. jpa스타일.
    // 서비스는 필요한 요청을 엔티티에 위임하는 역할.
    // 반대로 서비스에서 거의 로직처리를 거의 다하는 것을 트랜잭션 스크립트 패턴.







    //==생성 메서드==//
    // 오더를 생성하려면 연관관계에 있는 애들 다 걸리니까 복잡해. 그래서 따로 생성 메서드 생성.
    // 생성 문제가 생겼을 때 얘만 바꾸면 됨. 여기저기 찾아다닐필요 없다. 도메인 주도 개발.
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }

        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());

        return order;
    }






    //==비즈니스 로직==//
    /**
     * 주문 취소
     */
    public void cancel() {
        if(this.delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);

        for (OrderItem orderItem : this.orderItems) {
            orderItem.cancel();
        }
    }








    //==조회 로직==//
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        int totalPrice=0;
        for (OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();

        }
        return totalPrice;
    }


}
