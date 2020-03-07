package com.wukong.common.model;

import com.wukong.common.exception.BusinessException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.beans.Transient;
import java.io.Serializable;
import java.util.Optional;

/**
 * 基础的页数据类型
 * @author jinshi
 * @author fangzhibin add the NO_AUTH_TYPE type and update the success msg
 * @param <T> 数据泛型
 */
@ApiModel(description = "hik-ga-common提供的统一返回格式")
public class BaseResult<T> implements Serializable {
  private static final long serialVersionUID = 6803323956728517039L;

  /**
   * 成功
   */
  public static final int SUCCESS_TYPE = 0;
  /**
   * 错误：系统内部未处理
   */
  public static final int ERROR_TYPE = -1;
  /**
   * 失败：系统内部已经处理
   */
  public static final int FAIL_TYPE = -2;

  /**
   * 禁止访问：没有权限
   */
  public static final int NO_AUTH_TYPE = -3;

  @ApiModelProperty( required = true, value = "结果类型：0成功;-1错误，系统未处理;-2异常，系统已处理", dataType = "int", example = "0", position = 0)
  private int type = ERROR_TYPE; //默认结果类型是：错误：系统内部未处理
  @ApiModelProperty( required = true, value = "错误码", dataType = "String", example = "0", position = 1)
  private String code; //处理结果业务代码
  @ApiModelProperty( required = true, value = "返回描述信息", dataType = "String", example = "SUCCESS", position = 2)
  private String msg;  //处理结果业务信息
  @ApiModelProperty( value = "返回数据封装", position = 3)
  private T data;      //返回的数据

  /**
   * 无参构造函数
   */
  public BaseResult() {

  }

  /**
   * @param code 处理结果业务代码
   * @param msg 处理结果业务信息
   */
  public BaseResult(String code, String msg) {
    this.code = code;
    this.msg = msg;
  }
  /**
   * @param type 结果类型
   * @param code 处理结果业务代码
   * @param msg 处理结果业务信息
   */
  public BaseResult(int type, String code, String msg) {
    this.type = type;
    this.code = code;
    this.msg = msg;
  }

  /**
   * @param type 结果类型
   * @param code 处理结果业务代码
   * @param msg 处理结果业务信息
   * @param data 返回的数据
   */
  public BaseResult(int type, String code, String msg, T data) {
    this.type = type;
    this.code = code;
    this.msg = msg;
    this.data = data;
  }

  public static <T> BaseResult<T> success(T t) {
    return new BaseResult<>(BaseResult.SUCCESS_TYPE, "0", "SUCCESS", t);
  }
  public static <T> BaseResult<T> fail(String code, String message) {
    return new BaseResult<>(BaseResult.FAIL_TYPE, code, message);
  }
  public static <T> BaseResult<T> error(String code, String message) {
    return new BaseResult<>(BaseResult.ERROR_TYPE, code, message);
  }

  /**
   * @return 结果类型
   */
  public int getType() {
    return type;
  }

  /**
   * @param type 结果类型
   */
  public void setType(int type) {
    this.type = type;
  }

  /**
   * @return 结果数据
   */
  public T getData() {
    return data;
  }

  /**
   * @param data 结果数据
   */
  public void setData(T data) {
    this.data = data;
  }

  /**
   * @return 处理结果业务代码
   */
  public String getCode() {
    return code;
  }

  /**
   * @param code 处理结果业务代码
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * @return 处理结果业务信息
   */
  public String getMsg() {
    return msg;
  }

  /**
   * @param msg 处理结果业务信息
   */
  public void setMsg(String msg) {
    this.msg = msg;
  }

  public Optional<T> safeGetData() {
    if(type != SUCCESS_TYPE) {
      throw new BusinessException(code, msg, null);
    }
    return Optional.ofNullable(data);
  }

  @Transient
  public boolean isSuccess() {
    return type == SUCCESS_TYPE;
  }
  @Transient
  public boolean isFail() {
    return type == FAIL_TYPE;
  }
  @Transient
  public boolean isError() {
    return type == ERROR_TYPE;
  }
}
