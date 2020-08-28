package com.penglecode.xmodule.master4j.java.lang.thread;

/**
 * 线程中断示例
 *
 * 首先，一个线程不应该由其他线程来强制中断或停止，而是应该由线程自己自行停止。所以，Thread.stop, Thread.suspend, Thread.resume 都已经被废弃了。
 * 而 Thread.interrupt 的作用其实也不是中断线程，而是「通知线程应该中断了」，具体到底中断还是继续运行，应该由被通知的线程自己处理。
 * 具体来说，当对一个线程，调用 interrupt() 时，
 *  ① 如果线程处于被阻塞状态（例如处于sleep, wait, join 等状态），那么线程将立即退出被阻塞状态，并抛出一个InterruptedException异常。仅此而已。
 *  ② 如果线程处于正常活动状态，那么会将该线程的中断标志设置为 true，仅此而已。被设置中断标志的线程将继续正常运行，不受影响。interrupt() 并不能真正的中断线程，
 *     需要被调用的线程自己进行配合才行。也就是说，一个线程如果有被中断的需求，那么就可以这样做。
 *
 * 链接：https://www.zhihu.com/question/41048032/answer/89431513
 *
 * public void interrupt() 将调用者线程的中断状态设为true。
 * public boolean isInterrupted() 判断调用者线程的中断状态。
 * public static boolean interrupted() 只能通过Thread.interrupted()调用。 它会做两步操作：返回当前线程的中断状态；将当前线程的中断状态设为false；
 *
 * 链接：https://www.zhihu.com/question/41048032/answer/252905837
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/26 15:44
 */
public class ThreadInterruptExample {

    /**
     * 1、在运行时代码中调用了可以抛出InterruptedException的方法
     *
     * 线程如何支持中断？这依赖于线程的运行时代码是如何编写的。如果在运行时代码中调用了可以抛出InterruptedException的方法，
     * 那么线程在接受到的中断信号就体现在这个异常上。
     */
    public static void interruptByInterruptedException() throws InterruptedException {
        Thread thread = new Thread(() -> {
            int sleepMillis = 5000;
            for(int i = 0; i < 10; i++) { //预期循环10次
                try {
                    Thread.sleep(sleepMillis);
                    System.out.println(String.format("【%s】处理第%s个任务耗时：%s毫秒...", Thread.currentThread().getName(), i + 1, sleepMillis));
                } catch (InterruptedException e) { //特别注意：一旦捕获了InterruptedException，那么原本由interrupt()中断方法导致的中断标志位将不会改变，即Thread.currentThread().isInterrupted()仍然为false，而不是true
                    e.printStackTrace();
                    System.out.println(String.format("【%s】处理第%s个任务时发生中断... isInterrupted = %s", Thread.currentThread().getName(), i + 1, Thread.currentThread().isInterrupted()));
                    return; //接受到中断信号时，停止运行
                }
            }
        });

        thread.start(); //启动子线程
        Thread.sleep(12000); //主线程休眠
        System.out.println(String.format("【%s】主线程：等待12秒后发送中断信号, 当前子线程的中断状态：%s", Thread.currentThread().getName(), thread.isInterrupted()));
        thread.interrupt(); //发送中断信号
        while(!thread.isInterrupted()) { //实际这个while是进不去的，thread.interrupt()调用后线程的中断标志立马同步改为了true
            System.out.println(String.format("【%s】主线程等待子线程中断...", Thread.currentThread().getName()));
        }
        System.out.println(String.format("【%s】子线程已经中断...", Thread.currentThread().getName()));
    }

    /**
     * 使用Thread.currentThread().isInterrupted()来检测中断信号
     */
    public static void interruptByNoInterruptedException1() throws InterruptedException {
        Thread thread = new Thread(() -> {
            long lastRunningTimeMillis = System.currentTimeMillis();
            System.out.println(String.format("【%s】子线程开始运行，lastRunningTimeMillis = %s", Thread.currentThread().getName(), lastRunningTimeMillis));
            while(!Thread.currentThread().isInterrupted()) {
                lastRunningTimeMillis = System.currentTimeMillis();
            }
            System.out.println(String.format("【%s】子线程发生中断，lastRunningTimeMillis = %s", Thread.currentThread().getName(), lastRunningTimeMillis));
            /**
             * 不能使用阻塞方法（例如处于sleep, park等）代替此处的while(true)，使用这些阻塞方法都是徒劳的，因为在interrupt标志位为true的情况下，
             * 程序走到此处仍然会抛出InterruptedException异常，阻塞形同虚设，该子线程立马终结寿命变成not alive状态
             * 只有while(true)自旋才能维持该子线程的alive状态
             *
             * 正如上面所说：线程的interrupt状态与线程的死活没直接关系，java仅仅是通过interrupt标志位来告诉程序有人想让你中断运行，你是否中断运行就看你run()方法中如何设计的，
             * 我们(JVM)不帮你这个忙，我们仅仅是通知你
             */
            /*while(true) {
                lastRunningTimeMillis = System.currentTimeMillis();
            }*/
        });

        thread.start(); //启动子线程
        Thread.sleep(5000); //主线程休眠
        System.out.println(String.format("【%s】主线程：等待5秒后发送中断信号, 当前子线程的中断状态：%s", Thread.currentThread().getName(), thread.isInterrupted()));
        thread.interrupt(); //发送中断信号

        //yield等待，可以替换下面的Thread.sleep(1000)
        /*System.out.println(System.currentTimeMillis());
        while(thread.isInterrupted()) {
            Thread.yield();
        }*/

        /**
         * 实际这个while是进不去的，thread.interrupt()调用后线程的中断标志立马同步改为了true
         * 但是如果在这个while(!thread.isInterrupted())前加上Thread.sleep(1000)那就能进入到循环中去
         * 原因请看thread.isInterrupted()方法的注释：当thread的run()方法运行完毕之后(not alive)，thread的状态码被重置了，
         * 所以该isInterrupted()又会返回false的
         *
         * 如果你不信请将上面子线程run()方法最下面的while(true)打开使其运行(同时下面的sleep语句也打开)，这时thread线程铁定是alive的，
         * 所以此时thread的interrupt标志位仍然是true，所以下面的while(!thread.isInterrupted())是进不去的
         */
        //Thread.sleep(1000); //此处可以由上面的yield等待代替
        while(!thread.isInterrupted()) { //在注掉前面这个sleep语句的情况下，这个while是进不去的，thread.interrupt()调用后线程的中断标志立马同步改为了true
            System.out.println(String.format("【%s】主线程等待子线程中断...", Thread.currentThread().getName()));
            System.out.println(System.currentTimeMillis());
            break;
        }
        System.out.println(String.format("【%s】子线程已经中断...", Thread.currentThread().getName()));
    }

    /**
     * 使用Thread.interrupted()来检测中断信号
     */
    public static void interruptByNoInterruptedException2() throws InterruptedException {
        Thread thread = new Thread(() -> {
            long lastRunningTimeMillis = System.currentTimeMillis();
            System.out.println(String.format("【%s】子线程开始运行，lastRunningTimeMillis = %s", Thread.currentThread().getName(), lastRunningTimeMillis));
            while(!Thread.interrupted()) { //此处与上面interruptByNoInterruptedException1()示例不一样
                lastRunningTimeMillis = System.currentTimeMillis();
            }
            System.out.println(String.format("【%s】子线程发生中断，lastRunningTimeMillis = %s", Thread.currentThread().getName(), lastRunningTimeMillis));
            /**
             * 不能使用阻塞方法（例如处于sleep, park等）代替此处的while(true)，使用这些阻塞方法都是徒劳的，因为在interrupt标志位为true的情况下，
             * 程序走到此处仍然会抛出InterruptedException异常，阻塞形同虚设，该子线程立马终结寿命变成not alive状态
             * 只有while(true)自旋才能维持该子线程的alive状态
             *
             * 正如上面所说：线程的interrupt状态与线程的死活没直接关系，java仅仅是通过interrupt标志位来告诉程序有人想让你中断运行，你是否中断运行就看你run()方法中如何设计的，
             * 我们(JVM)不帮你这个忙，我们仅仅是通知你
             */
            /*while(true) {
                lastRunningTimeMillis = System.currentTimeMillis();
            }*/
        });

        thread.start(); //启动子线程
        Thread.sleep(5000); //主线程休眠
        System.out.println(String.format("【%s】主线程：等待5秒后发送中断信号, 当前子线程的中断状态：%s", Thread.currentThread().getName(), thread.isInterrupted()));
        thread.interrupt(); //发送中断信号

        /*System.out.println(System.currentTimeMillis());
        while(thread.isInterrupted()) {
            Thread.yield();
        }*/

        /**
         * 实际这个while是有可能进得去的(概率太小)，也无法模拟出来
         *
         * 原因是上面子线程run()方法中调用的是while(!Thread.interrupted())来检测中断信号的，
         * Thread.interrupted()方法被调用之后一定会复位interrupt标志位为false，所以下面的while(!thread.isInterrupted())
         * 循环是有可能进去的
         *
         * 但是这个进去的原因（即下面thread.isInterrupted()=false的原因）有两种可能：
         *  1、thread子线程虽然退出了while(!Thread.interrupted())循环，但是子线程还没运行完毕，仍然是alive的，但是此时主线程检测到thread.isInterrupted()为false，所以进去了
         *  2、同interruptByNoInterruptedException1()示例的原因，子线程运行完毕(not alive)，此时thread.isInterrupted()必然返回false
         */
        while(!thread.isInterrupted()) {
            System.out.println(String.format("【%s】主线程等待子线程中断...", Thread.currentThread().getName()));
            System.out.println(System.currentTimeMillis());
            break;
        }
        System.out.println(String.format("【%s】子线程已经中断...", Thread.currentThread().getName()));
    }

    public static void main(String[] args) throws Exception {
        interruptByInterruptedException();
        //interruptByNoInterruptedException1();
        //interruptByNoInterruptedException2();
    }

}
