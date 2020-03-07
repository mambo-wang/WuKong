package com.wukong.common.exception;

import com.wukong.common.model.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
@Slf4j
public class GlobleControllerExceptionHandler {

  /**
   * 业务异常处理
   * @author J.SUN 2019-01-18
   * @param businessException
   * @return com.hikvision.ga.common.BaseResult
   * @since 1.0.0
   */
  @ExceptionHandler(value = BusinessException.class)
  @ResponseBody
  public BaseResult businessExceptionHandler(BusinessException businessException) {
    logRecord(businessException);
    return BaseResult.fail(businessException.getCode(), businessException.getMessage());
  }

  /**
   * 默认异常处理
   *
   * @author J.SUN 2018-04-25
   * @param e
   * @return org.springframework.web.servlet.ModelAndView
   * @since 1.0.0
   */
  @ExceptionHandler(value = Exception.class)
  @ResponseBody
  public BaseResult defaultErrorHandler(Exception e) {
    logRecord(e);
    return BaseResult.error("500", "服务器内部错误");
  }

  void logRecord(Exception e, String... message){
    if (e instanceof BusinessException){
      BusinessException businessException = (BusinessException) e;
      log.error(businessException.getCode() + businessException.getMessage(), businessException);
    } else {
      String errorMsg;
      if (message != null && message.length > 0){
        errorMsg = message[0];
      }else {
        errorMsg = e.getMessage();
      }
      log.error(errorMsg, e);
    }
  }

}