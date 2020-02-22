package top.trister.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 使用int作为key生成器的独占副本
 * 可以使用在线程中,用于以下情况
 * 每个线程都需要使用的资源,但资源可以被不同的线程在不同时候使用.
 * @param <V>
 */
public abstract class SimpleExclusiveCopy<V> extends ExclusiveCopy<Integer, V> {
    private final AtomicInteger keyGenerator = new AtomicInteger(0);

    @Override
    public Integer generatorKey() {
        return keyGenerator.getAndIncrement();
    }

    @Override
    public void destroyKey(Integer integer) {
        keyGenerator.getAndDecrement();
    }
}
