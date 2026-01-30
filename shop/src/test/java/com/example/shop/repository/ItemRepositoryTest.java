package com.example.shop.repository;

import com.example.shop.constant.ItemSellStatus;
import com.example.shop.entity.Item;
import com.example.shop.entity.QItem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Log4j2
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest() {
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        Item savedItem = itemRepository.save(item);
        System.out.println(savedItem.toString());
    }

    public void createItemList() {
        for (int i = 1; i <= 10; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            if (i < 8)
                item.setItemSellStatus(ItemSellStatus.SELL);
            else
                item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNmTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("상품명, 상품상세설명 or 테스트")
    public void findByItemNmOrItemDetailTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트 상품1", "테스트 상품 상세 설명5");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByPriceLessThanTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThan(10005);
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 내림차순 조회 테스트")
    public void findByPriceLessThanOrderByPriceDesc() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    public void findByItemDetailTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("Querydsl 조회 테스트1")
    public void queryDslTest() {
        this.createItemList();

        JPQLQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;


/*
        JPQLQuery<Item> query = queryFactory
                .select(qItem)
                .from(qItem)
                .where(qItem.itemNm.like("%셔츠%"));

        List<Item> list = query.fetch();

        list.forEach(i -> log.info(i));
        long count = query.fetchCount();
        log.info(count);
*/

        JPQLQuery<Item> query =
                //queryFactory.select(qItem).from(qItem);
                queryFactory.selectFrom(qItem)
                        .where(qItem.itemSellStatus.eq(ItemSellStatus.SOLD_OUT))
                        .where(qItem.itemNm.like("%" + "상품" + "%"))
                        .orderBy(qItem.price.desc());

        List<Item> list = query.fetch();
        for (Item item : list)
            log.info(item);

        long count = query.fetchCount();
        log.info("count : " + count);
    }

    @Test
    @DisplayName("Querydsl 조회 테스트2")
    public void queryDslTest2() {
        this.createItemList();

        JPQLQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;

        int page = 1;
        int size = 3;

        JPQLQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(
                        qItem.itemSellStatus.eq(ItemSellStatus.SOLD_OUT)
                                .or(qItem.itemNm.like("%상품8%"))
                )
                .orderBy(qItem.id.desc())
                .offset(page * size)
                .limit(size);

        List<Item> list = query.fetch();

        list.forEach(item -> log.info(item));

        long count = query.fetchCount();
        log.info("count : " + count);
    }

    @Test
    @DisplayName("Querydsl 조회 테스트3")
    public void queryDslTest3() {
        this.createItemList();

        JPQLQueryFactory queryFactory = new JPAQueryFactory(em);

        QItem qItem = QItem.item;

        int page = 1;
        int size = 3;

        //where 조건
        BooleanExpression conf =
                qItem.itemSellStatus.eq(ItemSellStatus.SELL)
                        .or(qItem.itemNm.like("%상품8%"));


        //조건에 맞는 전체 데이터 가져오기
        List<Item> list = queryFactory.selectFrom(qItem)
                .where(conf)
                .orderBy(qItem.price.desc())
                .offset(page * size)
                .limit(size)
                .fetch();

        //조건에 맞는 데이터 개수
        long count = queryFactory
                .select(qItem.count())
                .from(qItem)
                .where(conf)
                .fetchCount();


        list.forEach(item -> log.info(item));

        log.info("count : " + count);
    }

    public void createItemList2() {
        for (int i = 1; i <= 5; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }

        for (int i = 6; i <= 10; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setStockNumber(0);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("조건 맞는 데이터 조회")
    public void queryDslTest4(){
        this.queryDslTest2();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem item = QItem.item;

        String itemDetail = "테스트 상품 상세 설명";
        int price = 10003;
        String itemSellStat = "SELL";

        booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%"));
        booleanBuilder.and(item.price.gt(price));

        if (StringUtils.equals(itemSellStat, ItemSellStatus.SELL)){
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        Pageable pageable = PageRequest.of(0,5);

        Page<Item> itemPageingResult = itemRepository.findAll(booleanBuilder, pageable);

        log.info("total elements : " + itemPageingResult.getTotalElements());

        List<Item> list = itemPageingResult.getContent();

        list.forEach(i -> log.info(i));


    }
}
