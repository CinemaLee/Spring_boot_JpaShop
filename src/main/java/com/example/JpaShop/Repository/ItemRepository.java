package com.example.JpaShop.Repository;

import com.example.JpaShop.domain.item.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class ItemRepository{

    @Autowired
    private EntityManager em;


    public void save(Item item) {
        if (item.getId() == null) {
            em.persist(item);
        }else{
            em.merge(item); // 아 이미 아이디가 있구나 그렇다면 업데이트하자. 라는 뜻. 병합.
        }
    }


    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }


    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }




}
