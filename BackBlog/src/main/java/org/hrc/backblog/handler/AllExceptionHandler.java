package org.hrc.backblog.handler;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.hrc.backblog.vo.ErrorCode;
import org.hrc.backblog.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 对Controller进行统一异常拦截
 */
@ControllerAdvice
@Slf4j
public class AllExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result doException(Exception e){
        if(e instanceof ExpiredJwtException){
            log.warn("用户Token过期");
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }
        e.printStackTrace();
        return Result.fail(-999,"系统异常");
    }
}
