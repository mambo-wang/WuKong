package com.wukong.consumer.repository;

import com.wukong.consumer.repository.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GoodsRepository extends PagingAndSortingRepository<Goods, Long>, JpaSpecificationExecutor<Goods> , JpaRepository<Goods, Long> {


    @Modifying
    @Query(value = "update tbl_goods set stock = stock - 1 where id = ?1 and stock > 0", nativeQuery = true)
    int reduceStock(Long goodsId);

    List<Goods> findByDeleted(String deleted);

    Optional<Goods> findById(Long id);
}
