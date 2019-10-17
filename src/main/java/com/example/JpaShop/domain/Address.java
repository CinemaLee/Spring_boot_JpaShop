package com.example.JpaShop.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Embeddable;




@Embeddable // JPA의 내장 타입. 어딘가에 내장될 수 있다는 뜻. 값 타입.
@Getter
public class Address { // 값타입은 변경불가능하게 설계해야 한다.

    private String city;
    private String street;
    private String zipcode;

    protected Address() { // protected로 기본생성자 생성. 동일 패키지나 얘를 상속받은 애만 사용할 수 있음. 어 왜 사용이 안되지 하고 왔을때 아 값타입은 변경불가능이구만! 알수 있음.

    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
