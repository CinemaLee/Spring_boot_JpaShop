package com.example.JpaShop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B") // 구분될떄의 이름.
@Getter
@Setter
public class Book extends Item {
    private String author;
    private String isbn;
}
