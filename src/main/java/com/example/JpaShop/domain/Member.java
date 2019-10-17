package com.example.JpaShop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded // 내장타입을 포함했다는 뜻.
    private Address address;

    @OneToMany(mappedBy = "member") // 나느 Order에 의해 매핑된 거울일 뿐이야. 우리 연관관계의 주인은 Order야. 1:N에서 주인은 N에 있다. 여기에 뭔 값을 넣는다 해서 Order의 포린키가 바뀌지 않는다.
    private List<Order> orders = new ArrayList<>();
}
