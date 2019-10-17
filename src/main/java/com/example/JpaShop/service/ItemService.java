package com.example.JpaShop.service;

import com.example.JpaShop.Repository.ItemRepository;
import com.example.JpaShop.domain.item.Book;
import com.example.JpaShop.domain.item.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ItemService {

    @Autowired
    ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item)
    {
        itemRepository.save(item);
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }


    public List<Item> findItems() {
        return itemRepository.findAll();
    }



    // 변경감지방법. 머지사용하지말자. 그리고 setter대신에 바로 추적할 수 있는 메소드를 사용하는게 더 좋다.
    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity){
        Item findItem = itemRepository.findOne(itemId); // 영속상태. 값수정하고 따로 persist필요없다. 트랜잭션이 끝나면서 알아서 업데이트 쿼리가 나감.
        findItem.setPrice(price);
        findItem.setName(name);
        findItem.setStockQuantity(stockQuantity);

    }
}
