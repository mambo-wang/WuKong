package com.wukong.provider.mapper;

import com.wukong.provider.entity.Order;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface OrderMapper {
    @Delete({
        "delete from tbl_order",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into tbl_order (user_id, goods_id, ",
        "delivery_addr_id, goods_name, ",
        "goods_count, goods_price, ",
        "order_channel, status, ",
        "create_date, pay_date)",
        "values (#{userId,jdbcType=BIGINT}, #{goodsId,jdbcType=BIGINT}, ",
        "#{deliveryAddrId,jdbcType=BIGINT}, #{goodsName,jdbcType=VARCHAR}, ",
        "#{goodsCount,jdbcType=INTEGER}, #{goodsPrice,jdbcType=DECIMAL}, ",
        "#{orderChannel,jdbcType=INTEGER}, #{status,jdbcType=INTEGER}, ",
        "#{createDate,jdbcType=TIMESTAMP}, #{payDate,jdbcType=TIMESTAMP})"
    })
    @Options(useGeneratedKeys=true,keyProperty="id")
    int insert(Order record);

    int insertSelective(Order record);

    @Select({
        "select",
        "id, user_id, goods_id, delivery_addr_id, goods_name, goods_count, goods_price, ",
        "order_channel, status, create_date, pay_date",
        "from tbl_order",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("BaseResultMap")
    Order selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Order record);

    @Update({
        "update tbl_order",
        "set user_id = #{userId,jdbcType=BIGINT},",
          "goods_id = #{goodsId,jdbcType=BIGINT},",
          "delivery_addr_id = #{deliveryAddrId,jdbcType=BIGINT},",
          "goods_name = #{goodsName,jdbcType=VARCHAR},",
          "goods_count = #{goodsCount,jdbcType=INTEGER},",
          "goods_price = #{goodsPrice,jdbcType=DECIMAL},",
          "order_channel = #{orderChannel,jdbcType=INTEGER},",
          "status = #{status,jdbcType=INTEGER},",
          "create_date = #{createDate,jdbcType=TIMESTAMP},",
          "pay_date = #{payDate,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(Order record);
}