package com.example.JpaShop.domain;

import com.example.JpaShop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 생성메서드 외의 생성방법을 막는다. 다른곳에서 new를 사용할 수 있으니.
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문 당시의 가격
    private int count; // 주문 당시의 수량





    //==연관 관게 편의 메서드==//
    void setAddOrder(Order order){
        this.order = order;
        order.getOrderItems().add(this);
    }





    //==생성 메서드==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count); // 재고를 까줌.

        return orderItem;
    }





    //==비즈니스 로직==//
    public void cancel() {
        getItem().addStock(count);
    }






    //==조회 로직==//

    /**
     * 주문상품 전체 가격 조회
     */
    public int getTotalPrice() {

        return getOrderPrice() * getCount();
    }
}
