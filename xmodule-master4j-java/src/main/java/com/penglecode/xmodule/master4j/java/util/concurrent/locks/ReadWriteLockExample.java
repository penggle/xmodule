package com.penglecode.xmodule.master4j.java.util.concurrent.locks;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁示例
 *
 * 总的来说读写锁适合读多写少的使用场景，例如项目启动时初始加载一些不常改动的配置项到内存中供程序使用，以提高程序性能等。
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/5 21:08
 */
public class ReadWriteLockExample {

    public static class ConfigCache {

        private final Map<String,Object> cache;

        private final ReadWriteLock readWriteLock;

        private final Lock readLock;

        private final Lock writeLock;

        public ConfigCache() {
            this.cache = new HashMap<>();
            this.readWriteLock = new ReentrantReadWriteLock();
            this.readLock = this.readWriteLock.readLock();
            this.writeLock = this.readWriteLock.writeLock();
        }

        public <T> T get(String key) {
            readLock.lock();
            try {
                return (T) cache.get(key);
            } finally {
                readLock.unlock();
            }
        }

        public <T> T set(String key, Object value) {
            writeLock.lock();
            try {
                return (T) cache.put(key, value);
            } finally {
                writeLock.unlock();
            }
        }

        public <T> T remove(String key) {
            writeLock.lock();
            try {
                return (T) cache.remove(key);
            } finally {
                writeLock.unlock();
            }
        }

        public void clear() {
            writeLock.lock();
            try {
                cache.clear();
            } finally {
                writeLock.unlock();
            }
        }

        public void reload(Map<String,Object> newCache) {
            writeLock.lock();
            try {
                cache.clear();
                cache.putAll(newCache);
            } finally {
                writeLock.unlock();
            }
        }

        public Lock getReadLock() {
            return readLock;
        }

        public Lock getWriteLock() {
            return writeLock;
        }
    }

    static class CacheReader extends Thread {

        private final ConfigCache configCache;

        private final String boundedKey;

        private final AtomicBoolean running;

        public CacheReader(ConfigCache configCache, String boundedKey, AtomicBoolean running) {
            this.configCache = configCache;
            this.boundedKey = boundedKey;
            this.running = running;
        }

        @Override
        public void run() {
            while (running.get()) {
                Object value = configCache.get(boundedKey);
                System.out.println(String.format("【%s】get(%s) = %s", Thread.currentThread().getName(), boundedKey, value));
            }
        }
    }

    static class CacheWriter extends Thread {

        private final ConfigCache configCache;

        private final String boundedKey;

        private final Object boundedValue;

        private final AtomicBoolean running;

        public CacheWriter(ConfigCache configCache, String boundedKey, Object boundedValue, AtomicBoolean running) {
            this.configCache = configCache;
            this.boundedKey = boundedKey;
            this.boundedValue = boundedValue;
            this.running = running;
        }

        @Override
        public void run() {
            while (running.get()) {
                LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(100));
                configCache.set(boundedKey, boundedValue);
                System.out.println(String.format("【%s】set(%s) = %s", Thread.currentThread().getName(), boundedKey, boundedValue));
            }
        }
    }

    public static void main(String[] args) {
        Properties properties = System.getProperties();
        final Map<String,Object> configProps = new HashMap<>();
        properties.forEach((key, value) -> {
            System.out.println(key + " = " + value);
            configProps.put(key.toString(), value);
        });

        ConfigCache configCache = new ConfigCache();
        configCache.reload(configProps); //初始化配置缓存

        AtomicBoolean running = new AtomicBoolean(true);

        String[] boundedKeys = new String[]{"user.timezone", "java.vendor", "java.version", "user.language", "java.home", "user.name", "file.encoding", "java.class.version", "os.name", "os.arch", "user.country", "user.dir"};

        CacheReader[] cacheReaders = new CacheReader[boundedKeys.length];
        for(int i = 0; i < cacheReaders.length; i++) {
            cacheReaders[i] = new CacheReader(configCache, boundedKeys[i], running);
            cacheReaders[i].start();
        }

        CacheWriter cacheWriter = new CacheWriter(configCache, "user.timezone", "Asia/Shanghai", running);
        cacheWriter.start();

        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(120));
        running.set(false);
    }

}
