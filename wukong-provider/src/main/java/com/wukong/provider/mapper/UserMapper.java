package com.wukong.provider.mapper;

import com.wukong.provider.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

public interface UserMapper {
    @Delete({
        "delete from tbl_user",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into tbl_user (name, username, ",
        "password, address, ",
        "phone_number, email, ",
        "score, balance)",
        "values (#{name,jdbcType=VARCHAR}, #{username,jdbcType=VARCHAR}, ",
        "#{password,jdbcType=VARCHAR}, #{address,jdbcType=VARCHAR}, ",
        "#{phoneNumber,jdbcType=VARCHAR}, #{email,jdbcType=VARCHAR}, ",
        "#{score,jdbcType=INTEGER}, #{balance,jdbcType=DECIMAL})"
    })
    @Options(useGeneratedKeys=true,keyProperty="id")
    int insert(User record);

    int insertSelective(User record);

    @Select({
        "select",
        "id, name, username, password, address, phone_number, email, score, balance",
        "from tbl_user",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("BaseResultMap")
    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    @Update({
        "update tbl_user",
        "set name = #{name,jdbcType=VARCHAR},",
          "username = #{username,jdbcType=VARCHAR},",
          "password = #{password,jdbcType=VARCHAR},",
          "address = #{address,jdbcType=VARCHAR},",
          "phone_number = #{phoneNumber,jdbcType=VARCHAR},",
          "email = #{email,jdbcType=VARCHAR},",
          "score = #{score,jdbcType=INTEGER},",
          "balance = #{balance,jdbcType=DECIMAL}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(User record);

    @Select({
            "select",
            "id, name, username, password, address, phone_number, email, score, balance",
            "from tbl_user"
    })
    List<User> getAll();

    @Select({
            "select",
            "id, name, username, password, address, phone_number, email, score, balance",
            "from tbl_user",
            "where username = #{username,jdbcType=VARCHAR}"
    })
    @ResultMap("BaseResultMap")
    User selectByUsername(String username);


    @Update({
            "update tbl_user set balance = balance - #{price,jdbcType=DECIMAL} where id = #{id,jdbcType=BIGINT} and balance > #{price,jdbcType=DECIMAL}"
    })
    int reduceBalance(Long id, BigDecimal price);
}