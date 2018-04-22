package com.concur.meta.client.utils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 可命名线程工厂
 *
 * @author jake
 */
public class NamingThreadFactory implements ThreadFactory {

    final ThreadGroup group;
    final AtomicInteger threadNumber = new AtomicInteger(1);
    final String namePrefix;

    public NamingThreadFactory(ThreadGroup group, String name) {
        this.group = group;
        namePrefix = group.getName() + ":" + name;
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(group, r, namePrefix
            + threadNumber.getAndIncrement(), 0);
    }

}