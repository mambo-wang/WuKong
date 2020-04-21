package com.wukong.provider.mapper;

import com.wukong.provider.controller.vo.OrderVO;
import com.wukong.provider.entity.Order;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface OrderMapper {
    @Delete({
        "delete from tbl_order",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into tbl_order (id, user_id, goods_id, ",
        "address, goods_name, ",
        "goods_count, goods_price, ",
        "status, create_date, ",
        "pay_date)",
        "values (#{id,jdbcType=BIGINT},#{userId,jdbcType=BIGINT}, #{goodsId,jdbcType=BIGINT}, ",
        "#{address,jdbcType=VARCHAR}, #{goodsName,jdbcType=VARCHAR}, ",
        "#{goodsCount,jdbcType=INTEGER}, #{goodsPrice,jdbcType=DECIMAL}, ",
        "#{status,jdbcType=INTEGER}, #{createDate,jdbcType=TIMESTAMP}, ",
        "#{payDate,jdbcType=TIMESTAMP})"
    })
    int insert(Order record);//id是自定义的

    int insertSelective(Order record);

    @Select({
        "select",
        "id, user_id, goods_id, address, goods_name, goods_count, goods_price, status, ",
        "create_date, pay_date",
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
          "address = #{address,jdbcType=VARCHAR},",
          "goods_name = #{goodsName,jdbcType=VARCHAR},",
          "goods_count = #{goodsCount,jdbcType=INTEGER},",
          "goods_price = #{goodsPrice,jdbcType=DECIMAL},",
          "status = #{status,jdbcType=INTEGER},",
          "create_date = #{createDate,jdbcType=TIMESTAMP},",
          "pay_date = #{payDate,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(Order record);

    List<Order> selectByUserId(Long userId);
}