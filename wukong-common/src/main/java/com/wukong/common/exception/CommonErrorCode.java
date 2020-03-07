package com.wukong.common.exception;

public enum CommonErrorCode {

  /**
   * 参数错误,复用http的status
   */
  PARAMETER_ILLEGAL("400", "Parameter Illegal"),
  /**
   * 系统内部异常，复用http的status
   */
  SYSTEM_INTERNAL_ERROR("500", "System Internal Error"),
  /**
   * 路径错误
   */
  NOT_FOUND("404", "Not Found"),
  /**
   * 403 禁止访问，复用http的status
   */
  FORBIDDEN("403", "Forbidden"),

  //=======================================认证失败=====================================================================
  //认证相关错误，不知道具体错误时可使用
  AUTHENTICATION_COMMON_ERROR(ErrorCodeBaseConstant.AUTHENTICATION_PREFIX + "00", "authentication error"),
  //token校验失败
  AUTHENTICATION_TOKEN_AUTHENTICATION_FAILED(ErrorCodeBaseConstant.AUTHENTICATION_PREFIX + "01", "Token authentication failed"),
  //cas相关错误
  AUTHENTICATION_CAS(ErrorCodeBaseConstant.AUTHENTICATION_PREFIX + "02", "cas error"),
  //cas获取nginx代理地址失败，错误码 0x11900003
  AUTHENTICATION_CAS_GET_NGINX_PROXY_ERROR(ErrorCodeBaseConstant.AUTHENTICATION_PREFIX + "03", "Cas Get Nginx Proxy Address Error"),

  //cas 获取本机ip地址失败，错误码 0x11900004
  AUTHENTICATION_CAS_GET_LOCAL_IP_ERROR(ErrorCodeBaseConstant.AUTHENTICATION_PREFIX + "04", "Cas Get Local Ip Error"),

  //======================================资源相关======================================================================
  //资源错误, 不知道具体错误时可使用
  RESOURCE_COMMON_ERROR(ErrorCodeBaseConstant.RESOURCE_PREFIX + "00", "resource error"),
  //config.properties文件导致的错误
  RESOURCE_CONFIG_PROPERTIES(ErrorCodeBaseConstant.RESOURCE_PREFIX + "01", "config.properties lead to error"),

  //======================================网络相关======================================================================
  //网络错误，不知道具体错误时可使用
  NETWORK_COMMON_ERROR(ErrorCodeBaseConstant.NETWORK_PREFIX + "00", "network error"),
  //网域相关错误
  NETWORK_DOMAIN(ErrorCodeBaseConstant.NETWORK_PREFIX + "01", "network domain error"),

  //======================================数据库相关======================================================================
  //数据库错误，不知道具体错误时可使用
  DATABASE_COMMON_ERROR(ErrorCodeBaseConstant.DATABASE_PREFIX + "00", "database error"),
  //redis错误
  DATABASE_REDIS(ErrorCodeBaseConstant.DATABASE_PREFIX + "01", "redis error"),
  //zk 错误
  DATABASE_ZK(ErrorCodeBaseConstant.DATABASE_PREFIX + "02", "zookeeper error"),

  //======================================系统错误======================================================================
  //系统错误，不知道具体错误时可使用
  SYSTEM_COMMON_ERROR(ErrorCodeBaseConstant.SYSTEM_PREFIX + "00", "system error"),

  //======================================服务异常======================================================================
  //服务异常，不知道具体错误时可使用
  SERVICE_COMMON_ERROR(ErrorCodeBaseConstant.SERVICE_PREFIX + "00", "service error"),

  //======================================参数错误======================================================================
  //参数错误，不知道具体错误时可使用
  PARAMS_COMMON_ERROR(ErrorCodeBaseConstant.PARAMS_PREFIX + "00", "params error"),
  PARAMS_BLANK(ErrorCodeBaseConstant.PARAMS_PREFIX + "01", "The required parameter $$ is blank."),
  PARAMS_OUT_OF_RANGE(ErrorCodeBaseConstant.PARAMS_PREFIX + "02", "The value of Parameter $$ is out of range."),
  PARAMS_FORMAT_INCORRECT(ErrorCodeBaseConstant.PARAMS_PREFIX + "03", "The format of Parameter $$ is not correct."),
  PARAMS_MUST_PAGE(ErrorCodeBaseConstant.PARAMS_PREFIX + "04", "Return message too long, please setting paging size."),
  PARAMS_NOT_SUPPORT(ErrorCodeBaseConstant.PARAMS_PREFIX + "05", "Not supported $$ parameters"),
  PARAMS_CONTENT_TOO_LONG(ErrorCodeBaseConstant.PARAMS_PREFIX + "06", "The parameter $$ content is too long"),
  PARAMS_UNKNOWN(ErrorCodeBaseConstant.PARAMS_PREFIX + "07", "This Error is Unknown"),

  //======================================其他错误======================================================================
  //其他错误 错误码 0x11902300
  OTHER_ERROR(ErrorCodeBaseConstant.OTHER_PREFIX +"00","Other error."),
  //bic-iii库错误 错误码 0x11902301
  OTHER_BIC_ERROR(ErrorCodeBaseConstant.OTHER_PREFIX + "01", "bic-iii error"),
  //startup-config错误 错误码 0x11902302
  OTHER_STARTUP_ERROR(ErrorCodeBaseConstant.OTHER_PREFIX + "02", "startup-config error"),
  //rsync 同步库 错误 错误码 0x11902303
  OTHER_RSYNC_ERROR(ErrorCodeBaseConstant.OTHER_PREFIX + "03", "rsync error"),
  //consul寻址失败或接口连接超时 错误码 0x11902304
  OTHER_ERROR_CONSUL_NO_AVAILABLE_SERVER(ErrorCodeBaseConstant.OTHER_PREFIX +"04","consul Load balancer does not have available server OR request timeout");


  private String code;
  private String msg;

  CommonErrorCode(String code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  /**
   * @return 错误码
   */
  public String getCode() {
    return code;
  }

  /**
   * @param code 错误码
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * @return 错误信息
   */
  public String getMsg() {
    return msg;
  }

  /**
   * @param msg 错误信息
   */
  public void setMsg(String msg) {
    this.msg = msg;
  }
  }
