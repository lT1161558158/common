package top.trister.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 独占工具
 * 可以使用在线程中,用于生成线程独占副本
 * 用法和ThreadLocal类似,但关注点在于提供值的对象,而非线程,因此可以用于线程池中
 * @param <K>
 * @param <V>
 */
public abstract class ExclusiveCopy<K, V> {
    private final Map<K, V> space = new ConcurrentHashMap<>();

    /**
     * 生成一个独占值
     * @param k 独占k
     * @return 独占值
     */
    public abstract V initial(K k);

    /**
     * 跳过一些条件获取独占的情况
     * @return 生成独占 key
     */
    public abstract K generatorKey();

    /**
     * 跳过一些条件,释放独占的副本,可以使得副本能够被其他人使用
     * @param k 独占的key
     */
    public abstract void destroyKey(K k);

    final class Exclusive implements AutoCloseable {
        final V v;
        final K k;

        public Exclusive() {
            this.k = generatorKey();
            this.v = space.computeIfAbsent(k, ExclusiveCopy.this::initial);
        }

        final V value() {
            return v;
        }

        @Override
        public void close() throws Exception {
            destroyKey(k);
        }
    }

    /**
     *
     * @return 独占资源
     */
    public Exclusive exclusive() {
        return new Exclusive();
    }

}
