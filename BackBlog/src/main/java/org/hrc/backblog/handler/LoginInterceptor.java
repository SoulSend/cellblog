package org.hrc.backblog.handler;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hrc.backblog.dao.pojo.SysUser;
import org.hrc.backblog.service.TokenService;
import org.hrc.backblog.utils.UserThreadLocal;
import org.hrc.backblog.vo.ErrorCode;
import org.hrc.backblog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private TokenService tokenService;

    /**
     * 请求处理前拦截，作为登录权限控制，拦截没有登陆的用户，不让他们访问一些资源
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /**
         * 判断是否有token
         * 没有token，未登录
         * 有token则校验token
         * 如果token合法，放行
         */
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)){
            Result result=Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            //设置
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        /**
         * 去掉头部：Bearer
         */
        SysUser sysUser = tokenService.checkToken(token.substring(7));
        if(sysUser==null){
            Result result=Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            //设置
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        //放行
        /**
         * 为了后续的处理程序能够获得当前会话的用户信息，将会话用户放入threadLocal里面保存，方便调用
         * 一定要记得，在处理完毕，所有方法调用完毕以后，要清除ThreadLocal里面的数据，防止内存泄漏
         */
        UserThreadLocal.put(sysUser);
        return true;
    }

    /**
     * 请求处理完毕后，处理一些善后工作
     * 比如清除ThreadLocal里面的数据，防止内存泄漏
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("ThreadLocal释放");
        UserThreadLocal.remove();
    }
}
