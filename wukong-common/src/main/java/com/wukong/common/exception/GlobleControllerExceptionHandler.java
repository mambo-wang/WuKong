package com.wukong.common.exception;

import com.wukong.common.model.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Set;


@ControllerAdvice
@Component
@Slf4j
public class GlobleControllerExceptionHandler {

  /**
   * 业务异常处理
   */
  @ExceptionHandler(value = BusinessException.class)
  @ResponseBody
  public BaseResult businessExceptionHandler(BusinessException businessException) {
    logRecord(businessException);
    return BaseResult.fail(businessException.getCode(), businessException.getMessage());
  }

    /**
     * url参数的参数校验
     */
  @ExceptionHandler
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public BaseResult handle(ValidationException exception) {
    if(exception instanceof ConstraintViolationException){
      ConstraintViolationException exs = (ConstraintViolationException) exception;

      Set<ConstraintViolation<?>> violations = exs.getConstraintViolations();
      StringBuilder stringBuilder = new StringBuilder();
      for (ConstraintViolation<?> item : violations) {
        /**打印验证不通过的信息*/
        stringBuilder.append(item.getMessage());
      }
      return BaseResult.fail("400", stringBuilder.toString()) ;

    }
    return BaseResult.fail("400", "参数校验有误，请重新提交请求。") ;
  }

    /**
     * model参数校验
     */
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResult handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        StringBuilder stringBuilder = new StringBuilder();
        if(result.hasErrors()){
            List<ObjectError> allErrors = result.getAllErrors();
            for (ObjectError error : allErrors) {
                stringBuilder.append(error.getDefaultMessage());
            }
        }
        return BaseResult.fail("400", stringBuilder.toString()) ;
    }

  /**
   * 默认异常处理
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