package com.ldx.blog.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

/**
 * @author Uaena
 * @date 2023/8/3 21:40
 */
@Service
@Slf4j
public class AsyncService {

    @Async("async_executor")//调用指定配置线程池名称，如果名称不存在会报异常
    public Future<String> configName(){
        log.info("configName，线程池名称：" + Thread.currentThread().getName());
        return new AsyncResult<>("configName，线程池名称：" + Thread.currentThread().getName());

    }

    @Async //如果不指定配置线程池名称，会找 getAsyncExecutor方法配置的默认线程池
    public Future<String> noConfigName(){
        log.info("noCofigName，线程池名称：" + Thread.currentThread().getName());
        return new AsyncResult<>("noCofigName，线程池名称：" + Thread.currentThread().getName());

    }
}
