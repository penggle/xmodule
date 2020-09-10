package com.penglecode.xmodule.master4j.java.util.concurrent;

/**
 * LockSupport源码
 */
public class JdkLockSupport {
    private JdkLockSupport() {} // Cannot be instantiated.

    private static void setBlocker(Thread t, Object arg) {
        UNSAFE.putObject(t, parkBlockerOffset, arg);
    }
    
    /**
     * 返回提供给最近一次尚未解除阻塞的 park 方法调用的 blocker 对象。
     * 如果该调用不受阻塞，则返回 null。
     * 返回的值只是一个瞬间快照，即由于未解除阻塞或者在不同的 blocker 对象上受阻而具有的线程。
     */
    public static Object getBlocker(Thread t) {
        if (t == null)
            throw new NullPointerException();
        return UNSAFE.getObjectVolatile(t, parkBlockerOffset);
    }
    
    /**
     * 如果给定线程的许可尚不可用，则使其可用。
     * 如果线程在 park 上受阻塞，则它将解除其阻塞状态。
     * 否则，保证下一次调用 park 不会受阻塞。
     * 如果给定线程尚未启动，则无法保证此操作有任何效果。 
     * @param thread: 要执行 unpark 操作的线程；该参数为 null 表示此操作没有任何效果。
     */
    public static void unpark(Thread thread) {
        if (thread != null)
            UNSAFE.unpark(thread);
    }

    /**
     * 为了线程调度，在许可可用之前阻塞当前线程。 
     * 如果许可可用，则使用该许可，并且该调用立即返回；
     * 否则，为线程调度禁用当前线程，并在发生以下三种情况之一以前，使其处于休眠状态：
     *  1. 其他某个线程将当前线程作为目标调用 unpark
     *  2. 其他某个线程中断当前线程
     *  3. 该调用不合逻辑地（即毫无理由地）返回
     */
    public static void park() {
        UNSAFE.park(false, 0L);
    }

    /**
     * 和park()方法类似，不过增加了等待的相对时间
     */
    public static void parkNanos(long nanos) {
        if (nanos > 0)
            UNSAFE.park(false, nanos);
    }

    /**
     * 和park()方法类似，不过增加了等待的绝对时间
     */
    public static void parkUntil(long deadline) {
        UNSAFE.park(true, deadline);
    }
    
    /**
     * 和park()方法类似，只不过增加了暂停的同步对象
     * @param blocker 导致此线程暂停的同步对象
     * @since 1.6
     */
    public static void park(Object blocker) {
        Thread t = Thread.currentThread();
        setBlocker(t, blocker);
        UNSAFE.park(false, 0L);
        setBlocker(t, null);
    }
    
    /**
     * parkNanos(long nanos)方法类似，只不过增加了暂停的同步对象
     * @param blocker 导致此线程暂停的同步对象
     * @since 1.6
     */
    public static void parkNanos(Object blocker, long nanos) {
        if (nanos > 0) {
            Thread t = Thread.currentThread();
            setBlocker(t, blocker);
            UNSAFE.park(false, nanos);
            setBlocker(t, null);
        }
    }
    
    /**
     * parkUntil(long deadline)方法类似，只不过增加了暂停的同步对象
     * @param blocker 导致此线程暂停的同步对象
     * @since 1.6
     */
    public static void parkUntil(Object blocker, long deadline) {
        Thread t = Thread.currentThread();
        setBlocker(t, blocker);
        UNSAFE.park(true, deadline);
        setBlocker(t, null);
    }

    static final int nextSecondarySeed() {
        int r;
        Thread t = Thread.currentThread();
        if ((r = UNSAFE.getInt(t, SECONDARY)) != 0) {
            r ^= r << 13;   // xorshift
            r ^= r >>> 17;
            r ^= r << 5;
        }
        else if ((r = java.util.concurrent.ThreadLocalRandom.current().nextInt()) == 0)
            r = 1; // avoid zero
        UNSAFE.putInt(t, SECONDARY, r);
        return r;
    }

    // Hotspot implementation via intrinsics API
    private static final sun.misc.Unsafe UNSAFE;
    private static final long parkBlockerOffset;
    private static final long SEED;
    private static final long PROBE;
    private static final long SECONDARY;
    static {
        try {
            UNSAFE = sun.misc.Unsafe.getUnsafe();
            Class<?> tk = Thread.class;
            parkBlockerOffset = UNSAFE.objectFieldOffset
                (tk.getDeclaredField("parkBlocker"));
            SEED = UNSAFE.objectFieldOffset
                (tk.getDeclaredField("threadLocalRandomSeed"));
            PROBE = UNSAFE.objectFieldOffset
                (tk.getDeclaredField("threadLocalRandomProbe"));
            SECONDARY = UNSAFE.objectFieldOffset
                (tk.getDeclaredField("threadLocalRandomSecondarySeed"));
        } catch (Exception ex) { throw new Error(ex); }
    }
}