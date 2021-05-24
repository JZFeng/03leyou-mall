/**
 * @Author jzfeng
 * @Date 5/16/21-2:10 PM
 */

package com.leyou.common.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 重构：
 * 多线程执行，单独放在一个工具类里;
 * 这个线程工具类专门提供一个线程池，任何其他的线程都可以往里面塞，不只是saveHtml的线程，还可以是下载的线程等等
 *
 */
public class ThreadUtils {

    private static int MAX_NUM_OF_THREADS = 20;

    private static ExecutorService executorService = Executors.newFixedThreadPool(MAX_NUM_OF_THREADS);

    public static void execute(Runnable runnable) {
        executorService.submit(runnable);
    }

}
