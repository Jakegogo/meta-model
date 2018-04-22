package com.concur.meta.client.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheLoader;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

/**
 * 缓存增强工具
 *
 * @author yongfu.cyf
 * @create 2017-10-30 下午8:40
 **/
public class CacheUtils {

    /** 线程池大小 */
    private static int threadNum = 4;

    private static ListeningExecutorService executor = createRefreshThreadPool();

    /**
     * 创建缓存刷新线程池
     * @return
     */
    private static ListeningExecutorService createRefreshThreadPool() {
        // 初始化消费线程池
        ThreadGroup threadGroup = new ThreadGroup("LModel缓存异步刷新线程");
        NamingThreadFactory threadFactory = new NamingThreadFactory(threadGroup, "LModel缓存异步刷新线程池");
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(threadNum, threadNum, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(),
            threadFactory, new AbortPolicy());
        return MoreExecutors.listeningDecorator(threadPool);
    }

    /**
     * 异步刷新缓存加载器
     * @param <K>
     * @param <V>
     */
    public static abstract class AsynchronousCacheLoader<K, V> extends CacheLoader<K, V> {
        @Override
        public ListenableFuture<V> reload(final K key, V oldValue) throws Exception {
            return executor.submit(new Callable<V>() {
                @Override
                public V call() throws Exception {
                    return AsynchronousCacheLoader.this.load(key);
                }
            });
        }
    }

}
