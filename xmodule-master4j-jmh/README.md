## JMH使用的正确姿势



- **通过判断代码是否是热点代码来采取不同的基准测试配置**

  正常情况下JMH基准测试结果或多或少与JIT即时编译机制有关，设想一下：如果基准测试使用了预热，即JIT即时编译机制触发了，测试的这个代码(方法)转变为本地代码，测试结果很nice，但是实际生产环境下由于测试的这个方法并不会被JVM视作热点代码，因此这种基准测试场景是错误，我们可以通过在基准测试方法上加@CompilerControl(CompilerControl.Mode.EXCLUDE)注解，告诉JVM不要进行JIT优化

- **尽量不要在基准测试代码中使用循环来测试真正的待测试方法**

  为了在每次调用基准测试方法时重复更多的次数(以减少基准测试方法调用的开销)，您很容易将基准测试代码放在基准测试方法的循环中。然而，JVM非常擅长优化循环，因此您可能会得到与预期不同的结果。一般来说，您应该避免在基准测试方法中使用循环，除非它们是您想要测量的代码的一部分(而不是围绕您想测量的代码)。

- **消除那些JVM认为无用的"死代码"**

  JVM认为什么是死代码？

  ```java
  public class MyBenchmark {
  
      @Benchmark
      public void testMethod() {
          int a = 1;
          int b = 2;
          int sum = a + b;
      }
  
  }
  ```
  上面代码，由于sum没被使用过，所以JVM认为sum是死代码，自动优化即删除`int sum = a + b;`这句。这句删除之后，JVM由发现a，b都是死代码，因此整个基准测试方法就是个空方法，因此这样的基准测试毫无意义。

  为此，我们需要消除死代码，消除死代码的方法有以下两种：

  - 给基准测试方法添加`return`返回值，在上面的例子中，通过`return sum;`即可解决问题，消除死代码。
  - 给基准测试方法添加参数`(Blackhole bh)`，然后在`bh.consume(sum);`即可解决问题，消除死代码。

- **避免常数折叠**

  上面是常数折叠？

  常数折叠是另一种常见的JVM优化。基于常量的计算通常会得到完全相同的结果，无论执行了多少次计算。JVM可能会检测到这一点，并将计算替换为计算的结果。例如下面这段代码：

  ```java
  public class MyBenchmark {
  
      @Benchmark
      public int testMethod() {
          int a = 1;
          int b = 2;
          int sum = a + b;
  
          return sum;
      }
  
  }
  ```
  JVM常数折叠优化之后应该是这样的：

  ```java
  public class MyBenchmark {
  
      @Benchmark
      public int testMethod() {
          int sum = 3;
  
          return sum;
      }
  
  }
  ```
  避免常数折叠你应该这样子：

  ```java
  public class MyBenchmark {
  
      @State(Scope.Thread)
      public static class MyState {
          public int a = 1;
          public int b = 2;
      }
  
  
      @Benchmark 
      public int testMethod(MyState state) {
          int sum = state.a + state.b;
          return sum;
      }
  }
  ```