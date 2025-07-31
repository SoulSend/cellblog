package org.hrc.backblog.utils;

import org.hrc.backblog.dao.pojo.SysUser;

/**
 * 用ThreadLocal封装的工具类
 */
public class UserThreadLocal {
    /**
     * 私有构造器，防止外部主动创建一个UserThreadLocal的实例
     */
    private UserThreadLocal(){}

    /**
     * 创建一个当前线程的ThreadLocal
     */
    private static  final  ThreadLocal<SysUser> LOCAL=new ThreadLocal<>();

    /**
     * 放入一个用户类的实例到ThreadLocal里面
     * @param sysUser
     */
    public static void put(SysUser sysUser){
        LOCAL.set(sysUser);
    }

    /**
     * 获取当前线程（会话）的用户类的实例，
     * @return
     */
    public static SysUser get(){
        return LOCAL.get();
    }

    /**
     * 清除ThreadLocal里面的数据
     * 防止内存泄漏，降低程序性能，
     * 甚至导致程序崩溃
     */
    public static void remove(){
        LOCAL.remove();
    }
}
