package com.example.JpaShop.domain.item;

import com.example.JpaShop.domain.Category;
import com.example.JpaShop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity // Ctrl + B 자세히 보기.
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 테이블전략. 한테이블에 다 떄려넣겠다.Book.Album.Movie...
@DiscriminatorColumn // 한테이블에 다 때려넣기 때문에 구분자가 필요함.
@Getter @Setter
public abstract class Item { // 아이템을 어디선가 객체생성할 수 없게.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();





    //==비즈니스 로직==// 도메인 주도 개발.
    // 보통은 ItemService만들어서 했겠지? 근데 도메인 주도 개발이라는게 이 데이터를 가지고있는 곳에서
    // 처리를 하는게 응집력이 있고 조금더 객체지향적이야.
    // 도메인의 데이터를 생성이 아닌 변경할 일이 있다면 이렇게 도메인의 자체 비즈니스 로직으로 처리해야해.
    // Setter를 통해서 바깥에서 지지고 볶아서 넣는 식은 안돼.


    /**
     * 재고 증가
     */
    public void addStock(int quantity)
    {
        this.stockQuantity += quantity;
    }


    /**
     * 재고 감소
     */
    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0 ){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }

}
