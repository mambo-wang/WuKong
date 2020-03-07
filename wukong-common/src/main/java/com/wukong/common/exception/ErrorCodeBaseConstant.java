package com.wukong.common.exception;

public class ErrorCodeBaseConstant {
  /**
   * 标识码
   */
  public static final String COMPONENT_INDENTIFICATION = "0x119";

  /**
   * 类别码
   * 0代表 组件自定义的错误码 不区分失败或异常
   */
  public static final String TYPE_CODE_O = "0";

  /**
   * 基础  错误码前缀，后续只需添加4位16进制，便可形成错误码
   */
  public static final String COMMON_PREFIX = COMPONENT_INDENTIFICATION + TYPE_CODE_O;


//  ===============================失败/异常码 前两位===================================================================

  //认证错误
  public static final String AUTHENTICATION = "00";

  //网络错误
  public static final String NETWORK = "01";

  //数据库错误
  public static final String DATABASE = "02";

  //系统错误
  public static final String SYSTEM = "03";

  //设备错误
  public static final String DEVICE = "04";

  //参数错误
  public static final String PARAMS = "20";

  //服务异常
  public static final String SERVICE = "21";

  //资源异常
  public static final String RESOURCE = "22";

  //其他错误
  public static final String OTHER = "23";


//  ===============================错误码前6位，后补2位即可成为错误码======================================================

  //认证错误
  public static final String AUTHENTICATION_PREFIX = COMMON_PREFIX + AUTHENTICATION;

  //网络错误
  public static final String NETWORK_PREFIX = COMMON_PREFIX + NETWORK;

  //数据库错误
  public static final String DATABASE_PREFIX = COMMON_PREFIX + DATABASE;

  //系统错误
  public static final String SYSTEM_PREFIX = COMMON_PREFIX + SYSTEM;

  //设备错误
  public static final String DEVICE_PREFIX = COMMON_PREFIX + DEVICE;

  //参数错误
  public static final String PARAMS_PREFIX = COMMON_PREFIX + PARAMS;

  //服务异常
  public static final String SERVICE_PREFIX = COMMON_PREFIX + SERVICE;

  //资源异常
  public static final String RESOURCE_PREFIX = COMMON_PREFIX + RESOURCE;

  //其他错误
  public static final String OTHER_PREFIX = COMMON_PREFIX + OTHER;


  private static final String ARCHITECTURE_IDENTIFIER = "infosight";

  /**
   * 拼装出[infosight.errorcode=xxx]，规范要求，其他组件错误码这样表示
   * @param errorCode 错误码
   * @return 拼装后的错误码
   */
  public static String getInfosightErrorCode(String errorCode) {
    StringBuilder sb = new StringBuilder();
    sb.append("[")
        .append(ARCHITECTURE_IDENTIFIER)
        .append(".errorcode=")
        .append(errorCode)
        .append("]");
    return sb.toString();
  }

}
