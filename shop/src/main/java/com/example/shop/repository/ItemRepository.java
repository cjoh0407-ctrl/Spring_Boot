package com.example.shop.repository;

import com.example.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> ,
        QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {

    Optional<Item> findItemByItemNm(String itemNm);

    List<Item> findByItemNm(String itemNm);

    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);

    List<Item> findByPriceLessThan(int priceIsLessThan);

    List<Item> findByPriceLessThanOrderByPriceDesc(int priceIsLessThan);

    List<Item> findByItemDetail(String itemDetail);
}
