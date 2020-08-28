Java面试题汇总

一份以Java作为基础语言的，涵盖Java后端开发所涉及到的各个方面的面试题



# 第一部分	Java语言篇



## 1、Java语言基础

### 1.1、什么是面向对象?

aaaa



### 1.2、面向对象的特征有哪些？

aaa



## 2、Java集合框架

### 2.1、Set和List的区别



## 3、Java多线程

### 3.1、线程的实现方式

主流的操作系统都提供了线程实现，实现线程主要有三种方式：使用内核线程实现（1:1实现），使用用户线程实现（1:N实现），使用用户线程加轻量级进程混合实现（N:M实现）。

1. **使用内核线程实现（1:1实现）**

   使用内核线程实现的方式也被称为1:1实现。内核线程（KernelLevel Thread，KLT）就是直接由操作系统内核（Kernel，下称内核）支持的线程，这种线程由内核来完成线程切换，内核通过操纵调度器（Scheduler）对线程进行调度，并负责将线程的任务映射到各个处理器 上。每个内核线程可以视为内核的一个分身，这样操作系统就有能力同时处理多件事情，支持多线程的内核就称为多线程内核（Multi-Threads Kernel）。程序一般不会直接使用内核线程，而是使用内核线程的一种高级接口——轻量级进程（Light Weight Process，LWP），轻量级进程就是我们通常意义上所讲的线程，由于每个轻量级进程都由一个内核线程支持，因此只有先支持内核线程，才能有轻量级进程。这种轻量级进程与内核线程之间1:1的关系称为一对一的线程模型。

2. **使用用户线程实现（1:N实现）**

   这里所说的用户线程指的是完全建立在用户空间的线程库上，系统内核不能感知到用户线程的存在及如何实现的。用户线程的建立、同步、销毁和调度完全在用户态中完成，不需要内核的帮助。如果程序实现得当，这种线程不需要切换到内核态，因此操作可以是非常快速且低消耗的，也能够支持规模更大的线程数量，部分高性能数据库中的多线程就是由用户线程实现的。这种进程与用户线程之间1:N的关系称为一对多的线程模型。用户线程的优势在于不需要系统内核支援，劣势也在于没有系统内核的支援，所有的线程操作都需要由用户程序自己去处理。线程的创建、销毁、切换和调度都是用户必须考虑的问题，而且由于操作系统只把处理器资源分配到进程，那诸如“阻塞如何处理”“多处理器系统中如何将线程映射到其他处理器上”这类问题解决起来将会异常困难，甚至有些是不可能实现的。因为使用用户线程实现的程序通常都比较复杂，除了有明确的需求外（譬如以前在不支持多线程的操作系统中的多线程程序、需要支持大规模线程数量的应用），一般的应用程序都不倾向使用用户线程。Java、Ruby等语言都曾经使用过用户线程，最终又都放弃了使用它。但是近年来许多新的、以高并发为卖点的编程语言又普遍支持了用户线程，譬如Golang、Erlang等，使得用户线程的使用率有所回升。

3. **使用用户线程加轻量级进程混合实现（N:M实现）**

   线程除了依赖内核线程实现和完全由用户程序自己实现之外，还有 一种将内核线程与用户线程一起使用的实现方式，被称为N:M实现。 在这种混合实现下，既存在用户线程，也存在轻量级进程。用户线程还是完全建立在用户空间中，因此用户线程的创建、切换、析构等操作依然廉价，并且可以支持大规模的用户线程并发。而操作系统支持的轻量级进程则作为用户线程和内核线程之间的桥梁，这样可以使用内核提供的线程调度功能及处理器映射，并且用户线程的系统调用要通过轻量级进程来完成，这大大降低了整个进程被完全阻塞的风险。在这种混合模式中，用户线程与轻量级进程的数量比是不定的，是N:M的关系，是一种多对多的线程模型。许多UNIX系列的操作系统，如Solaris、HP-UX等都提供了M：N的 线程模型实现。在这些操作系统上的应用也相对更容易应用M：N的线 程模型。

### 3.2、Java线程的实现方式

Java线程在早期的Classic虚拟机上（JDK 1.2以 前），是基于一种被称为“绿色线程”（Green Threads）的用户线程实现的，但从JDK 1.3起，“主流”平台上的“主流”商用Java虚拟机的线程模型普遍都被替换为基于操作系统原生线程模型来实现，即采用1:1的线程模型。

**以HotSpot为例，它的每一个Java线程都是直接映射到一个操作系统原生线程来实现的，而且中间没有额外的间接结构，所以HotSpot自己是不会去干涉线程调度的（可以设置线程优先级给操作系统提供调度建议），全权交给底下的操作系统去处理，所以何时冻结或唤醒线程、该给线程分配多少处理器执行时间、该把线程安排给哪个处理器核心去执行等，都是由操作系统完成的，也都是由操作系统全权决定的。**

**另外一个例子是在Solaris平台的HotSpot虚拟机，由于操作系统的线程特性本来就可以同时支持1:1（通过Bound Threads或Alternate Libthread实现）及N:M（通过LWP/Thread Based Synchronization实 现）的线程模型，因此Solaris版的HotSpot也对应提供了两个平台专有的虚拟机参数，即-XX:+UseLWPSynchronization（默认值）和-XX:+UseBoundThreads来明确指定虚拟机使用哪种线程模型。**

### 3.3、Java线程的调度方式

线程调度是指系统为线程分配处理器使用权的过程，调度主要方式有两种，分别是协同式（Cooperative Threads-Scheduling）线程调度和抢占式（Preemptive Threads-Scheduling）线程调度。

使用协同式调度的多线程系统，线程的执行时间由线程本身来控制，线程把自己的工作执行完了之后，要主动通知系统切换到另外一 个线程上去。设想如果程序本身存在bug，始终不得执行结束，那岂不是一直占着CPU及其他资源不放？这种弊端也是致命的不可接受的，会导致整个进程甚至整个系统阻塞的问题。所以，**Java使用的线程调度方式就是抢占式调度**。

### 3.4、Java线程的优先级

Java语言一共设置了10个级别的线程优先级（Thread.MIN_PRIORITY至Thread.MAX_PRIORITY）。在两个线程同时处于Ready状态时，优先级越高的线程越容易被系统选择执行。**不过，线程优先级并不是一项稳定的调节手段，很显然因为主流虚拟机上的Java线程是被映射到系统的原生线程上来实现的，所以线程调度最终还是由操作系统说了算。**尽管现代的操作系统基本都提供线程优先级的概念，但是并不见得能与Java线程的优先级一一对应，如Solaris 中线程有2147483648（2的31次幂）种优先级，但Windows中就只有七种优先级。如果操作系统的优先级比Java线程优先级更多，那问题还比较好处理，中间留出一点空位就是了，但对于比Java线程优先级少的系统，就不得不出现几个线程优先级对应到同一个操作系统优先级的情况了。

表12-1　Java线程优先级与各平台线程优先级之间的对应关系

| Java 线程优先级 | Linux | Windows                          | Apple | Bsd  | Solaris |
| :-------------: | :---: | -------------------------------- | :---: | :--: | :-----: |
|        1        |   4   | THREAD_PRIORITY_LOWEST(-2)       |  27   |  0   |    0    |
|        2        |   3   | THREAD_PRIORITY_LOWEST(-2)       |  28   |  3   |   32    |
|        3        |   2   | THREAD_PRIORITY_BELOW_NORMAL(-1) |  29   |  6   |   64    |
|        4        |   1   | THREAD_PRIORITY_BELOW_NORMAL(-1) |  30   |  10  |   96    |
|        5        |   0   | THREAD_PRIORITY_NORMAL(0)        |  31   |  15  |   127   |
|        6        |  -1   | THREAD_PRIORITY_NORMAL(0)        |  32   |  18  |   127   |
|        7        |  -2   | THREAD_PRIORITY_ABOVE_NORMAL(1)  |  33   |  21  |   127   |
|        8        |  -3   | THREAD_PRIORITY_ABOVE_NORMAL(1)  |  34   |  25  |   127   |
|        9        |  -4   | THREAD_PRIORITY_HIGHEST(2)       |  35   |  28  |   127   |
|       10        |  -5   | THREAD_PRIORITY_HIGHEST(2)       |  36   |  31  |   127   |



### 3.5、Java线程的状态转换

Java语言定义了6种线程状态，在任意一个时间点中，一个线程只能有且只有其中的一种状态，并且可以通过特定的方法在不同状态之间转换。这6种状态分别是：

1. **新建（New）**：创建后尚未启动的线程处于这种状态。
2. **运行（Runnable）**：包括操作系统线程状态中的Running和Ready， 也就是处于此状态的线程有可能正在执行，也有可能正在等待着操作系统为它分配执行时间。
3. **无限期等待（Waiting）**：处于这种状态的线程不会被分配处理器执行时间，它们要等待被其他线程显式唤醒。以下方法会让线程陷入无限期的等待状态：

   - 没有设置Timeout参数的Object::wait()方法；
   - 没有设置Timeout参数的Thread::join()方法；
   - LockSupport::park()方法。
4. **限期等待（Timed Waiting）**：处于这种状态的线程也不会被分配处理器执行时间，不过无须等待被其他线程显式唤醒，在一定时间之后它们会由系统自动唤醒。以下方法会让线程进入限期等待状态：
   - Thread::sleep()方法；
   - 设置了Timeout参数的Object::wait()方法；
   - 设置了Timeout参数的Thread::join()方法；
   - LockSupport::parkNanos()方法；
   - LockSupport::parkUntil()方法。
5. **阻塞（Blocked）**：线程被阻塞了，**“阻塞状态”与“等待状态”的区别是“阻塞状态”在等待着获取到一个排它锁**，这个事件将在另外一个线程放弃这个锁的时候发生；而“等待状态”则是在等待一段时间，或者唤醒动作的发生。**在程序等待进入同步区域的时候，线程将进入这种状态。**
6. **结束（Terminated）**：已终止线程的线程状态，线程已经结束执行。

上述6种状态在遇到特定事件发生的时候将会互相转换，它们的转换关系如下图所示。

![Java线程6种状态及转变.png](src/main/java/com/penglecode/xmodule/master4j/java/lang/thread/Java线程6种状态及转变.png)

### 3.6、sleep()和wait()的区别

1. sleep()方法和wait()方法所在的类不同，sleep()是java.lang.Thread类的静态方法，wait()是java.lang.Object类的实例方法。
2. sleep()方法在任何地方都能运行，而wait()方法必须在同步方法或同步块中才能运行，如果两者都在同步代码中运行的话，sleep()方法不会释放对象锁，而wait()方法会暂时释放对象锁。



### 3.7、sleep(0)、join(0)、wait(0)的区别

wait()和join()分别和wait(0)和join(0)等价，他们都代表了无限期的等待，而sleep(0)有点特殊，它的功能等同于yield()方法：让出本轮CPU执行机会。



### 3.8、说说Java线程的中断机制

首先，一个线程不应该由其他线程来强制中断或停止，而是应该由线程自己自行停止。所以，Thread.stop, Thread.suspend, Thread.resume 都已经被废弃了。而 Thread.interrupt()方法的作用其实也不是中断线程，而是「通知线程应该中断了」，具体到底中断还是继续运行，应该由被通知的线程自己处理。具体来说，当对一个线程，调用 interrupt() 时，
-  ① 如果线程处于被阻塞状态（例如处于sleep, wait, join 等状态），那么线程将立即退出被阻塞状态，并抛出一个InterruptedException异常。仅此而已。
-  ② 如果线程处于正常活动状态，那么会将该线程的中断标志设置为 true，仅此而已。被设置中断标志的线程将继续正常运行，不受影响。interrupt() 并不能真正的中断线程， 需要被调用的线程自己进行配合才行。也就是说，一个线程如果有被中断的需求，那么就可以这样做。

说的直白点：线程的interrupt状态与线程的死活没直接关系，java仅仅是通过interrupt标志位来告诉程序有人想让你中断运行，你是否中断运行就看你run()方法中如何设计的，我们(JVM)不帮你这个忙，我们仅仅是通知你。



### 3.9、Thread类的interrupt()、interrupted()、isInterrupted()三个方法的区别

- interrupt()：将调用者线程的中断状态设为true，仅此而已，线程实际正常运行。
- interrupted()：只能通过Thread.interrupted()调用。 它会做两步操作：返回当前线程的中断状态；将当前线程的中断状态设为false。
- isInterrupted()：判断调用者线程的中断状态。



### 3.10、为什么wait()与notify()方法要设计在Object类中?

- 首先、在Java中，所有对象的对象头中都有一个monitor监视器(对于HotSpot虚拟机来说，它的底层实现是ObjectMonitor.cpp)，而该monitor即是Java同步关键字`synchronized`实现的基石。
- wait()和notify()不仅仅是普通方法或同步工具，更重要的是它们是 **Java 中两个线程之间的通信机制**。对语言设计者而言, 如果不能通过Java 关键字(例如`synchronized`)实现通信此机制，同时又要确保这个机制对每个对象可用, 那么 Object 类则是的合理的声明位置。记住**同步**和**等待通知**是两个不同的领域，不要把它们看成是相同的或相关的。同步是提供互斥并确保 Java 类的线程安全，而 **wait 和 notify 是两个线程之间的通信机制**。
- **任意对象都可以上锁**，这是在 Object 类而不是 Thread 类中声明 wait 和 notify 的另一个原因。



### 3.11、为什么wait()与notify()方法必须在同步方法或同步块中才能调用?

wait/notify是Java线程之间的通信机制，他们存在竞态，我们必须保证在满足条件的情况下才进行wait。换句话说，如果不加锁的话，那么wait被调用的时候可能wait的条件已经不满足了。由于错误的条件下进行了wait，那么就有可能永远不会被notify到，所以我们需要强制wait/notify在synchronized中。细一点讲，在并发情况下，不加锁是不能保证wait()和notify()的最终执行顺序的，有可能出现上一个wait还没真正执行呢，这边notify已经执行完了，用个大白话的例子说：在只通知一次的情况下，那个人还没睡呢，你就提前叫醒他，这个没有任何作用，等他睡下后，由于只通知一次而且你已经用完了，那么等他睡着以后就是一直睡下去不会有人通知叫醒他了。



### 3.12、wait()方法为什么都放在while()循环中进行检测而不是放在if()中?

我们知道调用wait()方法是会释放对象锁的，那么wait()的底层是怎么实现的呢？wait()方法的实现是在ObjectMonitor.cpp中：

```c++
void ObjectMonitor::wait(jlong millis, bool interruptible, TRAPS) {
    ...
    ObjectWaiter node(Self); //当前线程调用wait方法后，jvm马上当前线程进行封装成一个ObjectWaiter对象
    AddWaiter(&node); //接着将该对象添加到_WaitSet 队列中
    _waiters++;
    exit (Self, true); //暂时释放对象锁
    Self -> _ParkEvent -> park(); //挂起当前线程，使其等待
    ...
}
```

Consumer线程释放对象锁的目的是使得Producer线程能够进入同步块调用notifyAll()方法唤醒所有Consumer（试想如果调用wait()方法的线程不释放对象锁会怎么样？它持有锁，并且还睡着了，其他人(指准备调用wait和notify的线程)都在等待获取锁，结果就只有一个结局：死锁），因此释放锁以后，Producer肯定是修改了共享区的数据了，此时Consumer醒来(**记住wait()方法结束后会重新获得锁**)肯定要回头再来检查下共享区的最新数据情况才能正确地做下一步的操作，而这一点if是做不到(你都已经在if块里面了，wait醒来后if就结束了，无法回头再来检测条件)。



### 3.13、wait()方法为什么要暂时释放对象锁？

结论我们知道：调用wait()方法是会暂时释放对象锁的，那么wait()的底层是怎么实现的呢？wait()方法的实现是在ObjectMonitor.cpp中：

```c++
void ObjectMonitor::wait(jlong millis, bool interruptible, TRAPS) {
    ...
    ObjectWaiter node(Self); //当前线程调用wait方法后，jvm马上当前线程进行封装成一个ObjectWaiter对象
    AddWaiter(&node); //接着将该对象添加到_WaitSet 队列中
    _waiters++;
    exit (Self, true); //暂时释放对象锁
    Self -> _ParkEvent -> park(); //挂起当前线程，使其等待
    ...
}
```

试想如果调用wait()方法的线程不释放对象锁会怎么样？它持有锁，并且还睡着了，其他人(指准备调用wait和notify的线程)都在等待获取锁，结果就只有一个结局：死锁



### 3.14、notify() 和 signal() 唤醒线程是随机的吗？

synchronized 是**非公平锁**。ReentrantLock 默认是**非公平锁**，但可设置为**公平锁**。

那线程通过`Object.nofity()` 和 `Condition.signal()` 被唤醒时是否是**公平**的呢？

先说结果，在`Java 1.8 HotSpot`下，两者都是**公平**的。

- `Object.nofity()` 的API文档明确说一个**随机**的线程将被唤醒，但具体情况将由**实现者决定**，因为`Object.nofity()`是一个native方法。具体请参见HotSpot的监视器底层实现ObjectMonitor.cpp的notify方法，其中存储ObjectWaiter的_WaitSet是个双向链表，采用FIFO方式唤醒一个线程。
- `Object.nofityAll()`的唤醒方式是LIFO

- `Condition.signal()` 的API文档则说一个被选定的线程将被唤醒。实际是采用FIFO方式唤醒一个线程。
- `Condition.signalAll()`的唤醒方式是FIFO

具体测试代码见[NotifyOrderExample.java](src/main/java/com/penglecode/xmodule/master4j/java/lang/thread/NotifyOrderExample.java)



### 3.15、Java中synchronized加锁的锁对象有哪几种?

- `synchronized`关键字加在实例方法上，锁对象是当前对象this；
- `synchronized`关键字加在静态方法上，锁对象是当前Class对象；
- `synchronized`关键字加在同步块上，锁对象是`synchronized(..)`括号内的对象；



### 3.16、Java中什么样的变量可以成为共享资源?

- 基础类型局部变量：`局部变量`存在于线程自己的方法栈中，如果局部变量时基础类型(包括原始类型及其包装类)，那么他是绝对线程安全的，这涉及到Java中只有值传递问题。
- 对象类型局部变量：`对象类型局部变量`对和`基础类型局部变量`不太一样。尽管引用本身没有被共享，但引用所指的对象并没有存储在线程的栈内。所有的对象都存在`共享堆`中。如果在某个方法中创建的对象不会逃逸出（译注：即该对象不会被其它方法获得，也不会被非局部变量引用到）该方法，那么它就是线程安全的。实际上，哪怕将这个对象作为参数传给其它方法，只要别的线程不修改这个对象，那它仍是线程安全的。
- 对象成员变量：对象成员存储在`堆`上。如果两个线程同时更新同一个对象的同一个成员，那这个代码就不是线程安全的。

当多个线程同时访问同一个资源，并且其中的一个或者多个线程对这个资源进行了写操作，才会产生**竞态条件**。多个线程同时读同一个资源不会产生竞态条件。所以综上所述绝对线程安全的只有基础类型局部变量。



3.17、Java中中断线程的方式有哪些?

- 如果代码中调用了产生WATING的方法，诸如Object.wait()、Thread.sleep()、Thread.join()、I/O等，那么通过捕获中断异常来处理中断，在catch块中终止线程的运行(即让线程提前结束)。伪代码如下：

  ```java
  Thread threadA = new Thread(new Runnable() {
      @Override
      public void run() {
          //这个while条件始终为true,尽管已经调用了threadA.interrupt()
          while(!Thread.currentThread().isInterrupted()) {
              try {
  				Thread.sleep(10000);
              } catch (InterruptedException e) {
                  e.printStackTrace();
                  System.out.println(Thread.currentThread().isInterrupted()); //注意了，这里是false
                  break;
              }
          }
          System.out.println("threadA done");
      }
  }).start();
  
  Thread.sleep(5000);
  
  threadA.interrupt(); //5秒后中断threadA线程
  ```

- 如果代码中没有调用产生WATING的方法，那么可以通过中断信号机制来处理中断。伪代码如下所示：

  ```java
  Thread threadA = new Thread(new Runnable() {
      @Override
      public void run() {
          long lastRunningTimeMillis = System.currentTimeMillis();
          //在调用了threadA.interrupt()之后，这个while条件就变为false了退出了while循环
          while(!Thread.currentThread().isInterrupted()) { //通过自旋来模拟一个高耗时的计算任务
              lastRunningTimeMillis = System.currentTimeMillis();
          }
          System.out.println("threadA done");
      }
  }).start();
  
  Thread.sleep(5000);
  
  threadA.interrupt(); //5秒后中断threadA线程
  ```







# 第二部分	JVM篇



## 1、内存区域



aaa



## 2、垃圾收集



aaa



## 3、监控工具



aaa



## 4、class与字节码



aaa



## 5、类加载机制



### 5.1、类加载机制-双亲委托机制

先说说类加载机制的几个核心方法：

- ```java
  protected Class<?> loadClass(String name, boolean resolve): 加载类的入口方法
  ```

- ```java
  protected final Class<?> findLoadedClass(String name): 检测类是否已经被当前ClassLoader加载过了，未加载过返回null
  ```
- ```java
  protected Class<?> findClass(String name): 尝试在当前ClassLoader在管辖范围内(例如ExtClassLoader的管辖范围是<JAVA_HOME>/jre/lib/ext目录)加载该类，未发现抛出ClassNotFoundException
  ```

举个例子，例如：当jvm要加载用户自定义的类Test.class的时候，其加载流程如下所述：

1. 首先会到自定义加载器中查找，看是否已经加载过(**调用`findLoadedClass()`**)，如果已经加载过，则返回字节码。
2. 如果自定义加载器没有加载过，则询问上一层加载器(即AppClassLoader)是否已经加载过Test.class。
3. 如果没有加载过，则询问上一层加载器（ExtClassLoader）是否已经加载过。
4. 如果没有加载过，则继续询问上一层加载（BoopStrap ClassLoader）是否已经加载过。
5. 如果BoopStrap ClassLoader依然没有加载过，则到自己指定类加载路径下（"sun.boot.class.path"）查找(**调用`findClass()`**)是否有Test.class字节码，有则返回，没有通知(**这个通知是靠下级ClassLoader捕获parent.loadClass()方法来实现的**)下一层加载器ExtClassLoader到自己指定的类加载路径下（java.ext.dirs）查看。
6. 依次类推，最后到自定义类加载器指定的路径还没有找到Test.class字节码，则抛出异常`ClassNotFoundException`。
7. **如果Test.class依赖一些Java核心类库则不会使用自定义的类加载器加载，BoopStrap ClassLoader肯定会代为加载的。**
8. **如果Test.class依赖一些用户自定义的类(如BaseTest.java)则依然使用自定义的类加载器加载。**

**loadClass()源码：**

```java
protected Class<?> loadClass(String name, boolean resolve)
        throws ClassNotFoundException
    {
        synchronized (getClassLoadingLock(name)) {
            // 首先，检查是否已经加载过
            Class<?> c = findLoadedClass(name);
            if (c == null) {
                long t0 = System.nanoTime();
                try {
                    if (parent != null) {
                        //父加载器不为空,调用父加载器的loadClass
                        c = parent.loadClass(name, false);
                    } else {
                        //父加载器为空则,调用Bootstrap Classloader
                        c = findBootstrapClassOrNull(name);
                    }
                } catch (ClassNotFoundException e) {
                    // ClassNotFoundException thrown if class not found
                    // from the non-null parent class loader
                }

                if (c == null) {
                    // If still not found, then invoke findClass in order
                    // to find the class.
                    long t1 = System.nanoTime();
                    //父加载器没有找到，则调用findclass
                    c = findClass(name);

                    // this is the defining class loader; record the stats
                    sun.misc.PerfCounter.getParentDelegationTime().addTime(t1 - t0);
                    sun.misc.PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
                    sun.misc.PerfCounter.getFindClasses().increment();
                }
            }
            if (resolve) {
                //调用resolveClass()
                resolveClass(c);
            }
            return c;
        }
    }
```



### 5.2、遵守双亲委派模型自定义类加载器的步骤

1. **继承ClassLoader类**
2. **重写protected Class<?> findClass(String name)方法**
3. **在上面findClass()方法中调用defineClass()方法**

参考示例：[JarEntryClassLoader.java](src/main/java/com/penglecode/xmodule/master4j/java/lang/classloader/JarEntryClassLoader.java)、[ClassLoaderExample.java](src/main/java/com/penglecode/xmodule/master4j/java/lang/classloader/ClassLoaderExample.java)



### 5.3、使用双亲委派模型来组织类加载器之间的关系的好处是什么?

使用双亲委派模型来组织类加载器之间的关系，一个显而易见的好处就是Java中的类随着它的类加载器一起具备了一种带有优先级的层次 关系。例如类java.lang.Object，它存放在rt.jar之中，无论哪一个类加载器要加载这个类，最终都是委派给处于模型最顶端的启动类加载器进行加载，因此Object类在程序的各种类加载器环境中都能够保证是同一个类。反之，如果没有使用双亲委派模型，都由各个类加载器自行去加载的话，如果用户自己也编写了一个名为java.lang.Object的类，并放在程序的ClassPath中，那系统中就会出现多个不同的Object类，Java类型体系中最基础的行为也就无从保证，应用程序将会变得一片混乱，而且还带来安全问题。



