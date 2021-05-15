# 基础篇

## 面向对象

### 什么是面向对象

#### 面向对象与面向过程

- 什么是面向过程？

  面向过程编程是自上而下的编程模式，将问题分解成一个一个步骤，每个步骤定义一个函数来实现，依次调用各个函数即可。

- 什么是面向对象？

  面向对象编程是将事物高度抽象化的编程模式，将问题分解成一个一个步骤，对每个步骤进行相应的抽象，形成对象，通过不同对象之间的调用，组合解决问题。

  

#### 面向对象的三大基本特征

- 封装(Encapsulation)

  所谓封装，也就是把客观事物封装成抽象的类，并且类可以把自己的数据和方法只让可信的类或者对象操作，对不可信的进行信息隐藏。封装是面向对象的特征之一，是对象和类概念的主要特性。简单的说，一个类就是一个封装了数据以及操作这些数据的代码的逻辑实体。在一个对象内部，某些代码或某些数据可以是私有的，不能被外界访问。通过这种方式，对象对内部数据提供了不同级别的保护，以防止程序中无关的部分意外的改变或错误的使用了对象的私有部分。

- 继承(Inheritance)

  继承是指这样一种能力：它可以使用现有类的所有功能，并在无需重新编写原来的类的情况下对这些功能进行扩展。通过继承创建的新类称为“子类”或“派生类”，被继承的类称为“基类”、“父类”或“超类”。继承的过程，就是从一般到特殊的过程。要实现继承，可以通过“继承”（Inheritance）和“组合”（Composition）来实现。继承概念的实现方式有二类：实现继承与接口继承。实现继承是指直接使用基类的属性和方法而无需额外编码的能力；接口继承是指仅使用属性和方法的名称、但是子类必须提供实现的能力；

- 多态(Polymorphism)

  所谓多态就是指一个类实例的相同方法在不同情形有不同表现形式。多态机制使具有不同内部结构的对象可以共享相同的外部接口。这意味着，虽然针对不同对象的具体操作不同，但通过一个公共的类，它们（那些操作）可以通过相同的方式予以调用。

#### 面向对象的五大基本原则

1. 单一职责原则（Single-Responsibility Principle）：

   **其核心思想为：一个类，最好只做一件事，只有一个引起它的变化**。单一职责原则可以看做是低耦合、高内聚在面向对象原则上的引申，将职责定义为引起变化的原因，以提高内聚性来减少引起变化的原因。职责过多，可能引起它变化的原因就越多，这将导致职责依赖，相互之间就产生影响，从而大大损伤其内聚性和耦合度。通常意义下的单一职责，就是指只有一种单一功能，不要为类实现过多的功能点，以保证实体只有一个引起它变化的原因。 专注，是一个人优良的品质；同样的，单一也是一个类的优良设计。交杂不清的职责将使得代码看起来特别别扭牵一发而动全身，有失美感和必然导致丑陋的系统错误风险。

2. 开放封闭原则（Open-Closed principle）

   **其核心思想是：软件实体应该是可扩展的，而不可修改的**。也就是，对扩展开放，对修改封闭的。开放封闭原则主要体现在两个方面1、对扩展开放，意味着有新的需求或变化时，可以对现有代码进行扩展，以适应新的情况。2、对修改封闭，意味着类一旦设计完成，就可以独立完成其工作，而不要对其进行任何尝试的修改。 实现开放封闭原则的核心思想就是对抽象编程，而不对具体编程，因为抽象相对稳定。让类依赖于固定的抽象，所以修改就是封闭的；而通过面向对象的继承和多态机制，又可以实现对抽象类的继承，通过覆写其方法来改变固有行为，实现新的拓展方法，所以就是开放的。 “需求总是变化”没有不变的软件，所以就需要用封闭开放原则来封闭变化满足需求，同时还能保持软件内部的封装体系稳定，不被需求的变化影响。

3. 里氏替换原则（Liskov-Substitution Principle）

   **其核心思想是：子类必须能够替换其基类**。这一思想体现为对继承机制的约束规范，只有子类能够替换基类时，才能保证系统在运行期内识别子类，这是保证继承复用的基础。在父类和子类的具体行为中，必须严格把握继承层次中的关系和特征，将基类替换为子类，程序的行为不会发生任何变化。同时，这一约束反过来则是不成立的，子类可以替换基类，但是基类不一定能替换子类。 Liskov替换原则，主要着眼于对抽象和多态建立在继承的基础上，因此只有遵循了Liskov替换原则，才能保证继承复用是可靠地。实现的方法是面向接口编程：将公共部分抽象为基类接口或抽象类，通过Extract Abstract Class，在子类中通过覆写父类的方法实现新的方式支持同样的职责。 Liskov替换原则是关于继承机制的设计原则，违反了Liskov替换原则就必然导致违反开放封闭原则。 Liskov替换原则能够保证系统具有良好的拓展性，同时实现基于多态的抽象机制，能够减少代码冗余，避免运行期的类型判别。

4. 依赖倒置原则（Dependecy-Inversion Principle）

   **其核心思想是：依赖于抽象**。具体而言就是高层模块不依赖于底层模块，二者都同依赖于抽象；抽象不依赖于具体，具体依赖于抽象。 我们知道，依赖一定会存在于类与类、模块与模块之间。当两个模块之间存在紧密的耦合关系时，最好的方法就是分离接口和实现：在依赖之间定义一个抽象的接口使得高层模块调用接口，而底层模块实现接口的定义，以此来有效控制耦合关系，达到依赖于抽象的设计目标。 抽象的稳定性决定了系统的稳定性，因为抽象是不变的，依赖于抽象是面向对象设计的精髓，也是依赖倒置原则的核心。 依赖于抽象是一个通用的原则，而某些时候依赖于细节则是在所难免的，必须权衡在抽象和具体之间的取舍，方法不是一层不变的。依赖于抽象，就是对接口编程，不要对实现编程。

5. 接口隔离原则（Interface-Segregation Principle）

   **其核心思想是：使用多个小的专门的接口，而不要使用一个大的总接口**。 具体而言，接口隔离原则体现在：接口应该是内聚的，应该避免“胖”接口。一个类对另外一个类的依赖应该建立在最小的接口上，不要强迫依赖不用的方法，这是一种接口污染。 接口有效地将细节和抽象隔离，体现了对抽象编程的一切好处，接口隔离强调接口的单一性。而胖接口存在明显的弊端，会导致实现的类型必须完全实现接口的所有方法、属性等；而某些时候，实现类型并非需要所有的接口定义，在设计上这是“浪费”，而且在实施上这会带来潜在的问题，对胖接口的修改将导致一连串的客户端程序需要修改，有时候这是一种灾难。在这种情况下，将胖接口分解为多个特点的定制化方法，使得客户端仅仅依赖于它们的实际调用的方法，从而解除了客户端不会依赖于它们不用的方法。 分离的手段主要有以下两种：1、委托分离，通过增加一个新的类型来委托客户的请求，隔离客户和接口的直接依赖，但是会增加系统的开销。2、多重继承分离，通过接口多继承来实现客户的需求，这种方式是较好的。

### 封装、继承、多态

#### 什么是多态

- 什么是多态

  多态的概念呢比较简单，就是同一操作作用于不同的对象，可以有不同的解释，产生不同的执行结果。

- 多态的必要条件

  为了实现运行期的多态，或者说是动态绑定，需要满足三个条件。

  1. 有类继承或者接口实现
  2. 子类要重写父类的方法
  3. 父类的引用指向子类的对象

#### 方法重写与重载

重载（Overloading）和重写（Overriding）是Java中两个比较重要的概念。但是对于新手来说也比较容易混淆。

- **重载**

  简单说，就是方法有同样的名称，但是参数列表不相同的情形，这样的同名不同参数的方法之间，互相称之为重载方法。
  从JVM的角度来说，方法重载是JVM"静态分配"的体现，即JVM在编译阶段，Javac编译器就根据参数的静态类型决定了会使用哪个重载版本了。来个高级点的例子：

  ```java
  public class AdvancedOverloadingExample {
  
      static abstract class Human {}
  
      static class Man extends Human {}
  
      static class Woman extends Human {}
  
      public void sayHello(Human guy) {
          System.out.println("hello,guy!");
      }
  
      public void sayHello(Man guy) {
          System.out.println("hello,gentleman!");
      }
  
      public void sayHello(Woman guy) {
          System.out.println("hello,lady!");
      }
  
      public static void main(String[] args) {
          Human man = new Man();
          Human woman = new Woman();
          AdvancedOverloadingExample example = new AdvancedOverloadingExample();
          /**
           * 重载方法在javac编译阶段就根据参数类型确定好了具体调用哪个方法了
           * 这里的"参数类型"指的是参数的声明类型而不是实际类型!
           */
          example.sayHello(man);
          example.sayHello(woman);
      }
  
  }
  ```
  运行结果：
  ```shell
  hello,guy!
  hello,guy!
  ```

- **重写**

  重写指的是在Java的子类与父类中有两个名称、参数列表都相同的方法的情况。由于他们具有相同的方法签名，所以子类中的新方法将覆盖父类中原有的方法。
  从JVM的角度来说，方法重写是JVM"动态分配"的体现，即JVM在运行期根据引用变量的实际类型来选择方法版本。
  
  **重写的特征**：
  
  1. 重写后的方法的访问修饰符可以更宽松一点但不能严谨(区别于网上拗口的说法)。例如父类是protected的修饰符那么子类重写之后可以是public(更宽松一点)，反之则不行。
  2. 重写方法参数列表必须与被重写方法完全相同。
  3. 重写方法的返回值必须和被重写方法的返回值一致，或者是其子类，比如被重写方法返回值是Collection，那么重写方法的返回值可以是List或者Set。
  4. 重写方法所抛出的异常必须和被重写方法的所抛出的异常一致，或者是其子类。无论被重写方法是否抛出异常，重写方法都可以抛出任何UnChecked异常。被重写方法抛出异常，重写方法可以不抛出异常。
  5. 被重写的方法不能为private，子类再写一个同名的方法并不是对父类方法进行重写(Override)，而是重新生成一个新的方法。
  6. 静态方法不能被重写。
  7. 不能重写被标识为final的方法，子类中必须重写父类中的abstract方法。

  重写的一个综合示例：
  ```java
  public class AdvancedOverridingExample {
  
      static class ServiceInstance { }
  
      static abstract class ConfigServer {
  
          public abstract void startup() throws IOException;
  
          protected abstract void shutdown() throws IOException, IllegalStateException;
  
          public abstract Collection<ServiceInstance> getInstances(String instanceId);
  
          public abstract ServiceInstance registerInstance(String serviceName, String host, int port);
  
      }
  
      static class ConsulConfigServer extends ConfigServer {
  
          /**
           * 重写方法可以不抛出任何异常
           * 无论被重写方法是否抛出异常，重写方法都可以抛出任何UnChecked异常
           */
          @Override
          public void startup() throws IllegalStateException {
              System.out.println("Consul config server startup...");
          }
  
          /**
           * 重写后的方法的访问修饰符可以更宽松一点但不能严谨
           *
           * 重写方法可以不抛出任何异常，此处去掉了IllegalStateException
           *
           * 重写方法所抛出的异常必须和被重写方法的所抛出的异常一致，或者是其子类
           */
          @Override
          public void shutdown() throws EOFException {
              System.out.println("Consul config server shutdown...");
          }
  
          /**
           * 重写方法的返回值必须与被重写方法保持一致或者是其子类
           */
          @Override
          public List<ServiceInstance> getInstances(String instanceId) {
              return null;
          }
  
          /**
           * 重写方法的参数列表必须与被重写方法的参数列表完全一致
           * 例如下面将String serviceName改为CharSequence serviceName是会报错编译不通过的
           */
          @Override
          public ServiceInstance registerInstance(String serviceName, String host, int port) {
              return null;
          }
      }
  
  }
  ```

- 重载 VS 重写

  > 1、重载是一个编译期概念、重写是一个运行期间概念。
  >
  > 2、重载遵循所谓“编译期绑定”，涉及到JVM字节码执行引擎中的**静态分派**机制，即在编译时根据**参数变量的声明类型**判断应该调用哪个方法。
  >
  > 3、重写遵循所谓“运行期绑定”，涉及到JVM字节码执行引擎中的**动态分派**机制，即在运行的时候，根据**引用变量所指向的实际对象的类型**来调用方法。
  >
  > 4、因为在编译期已经确定调用哪个方法，所以重载并不是多态。而重写是多态。重载只是一种语言特性，是一种语法规则，与多态无关，与面向对象也无关。（注：严格来说，重载是编译时多态，即静态多态。但是，Java中提到的多态，在不特别说明的情况下都指动态多态）



#### Java的继承与实现

面向对象有三个特征：封装、继承、多态。

其中继承和实现都体现了`传递性`。而且明确定义如下：

> 继承：如果多个类的某个部分的功能相同，那么可以抽象出一个类出来，把他们的相同部分都放到父类里，让他们都继承这个类。
>
> 实现：如果多个类处理的目标是一样的，但是处理的方法方式不同，那么就定义一个接口，也就是一个标准，让他们的实现这个接口，各自实现自己具体的处理方法来处理那个目标。

所以，继承的根本原因是因为要*复用*，而实现的根本原因是需要定义一个*标准*。

在Java中，继承使用`extends`关键字实现，而实现通过`implements`关键字。

**Java中支持一个类同时实现多个接口，但是不支持同时继承多个类。**

> 简单点说，就是同样是一台汽车，既可以是电动车，也可以是汽油车，也可以是油电混合的，只要实现不同的标准就行了，但是一台车只能属于一个品牌，一个厂商。

```java
class Car extends Benz implements GasolineCar, ElectroCar {

}
```

在接口中只能定义全局常量（public static final）和无实现的方法（Java 8以后可以有defult方法）；而在继承中可以定义属性方法,变量,常量等。



#### Java的继承与组合

Java是一个面向对象的语言。每一个学习过Java的人都知道，封装、继承、多态是面向对象的三个特征。每个人在刚刚学习继承的时候都会或多或少的有这样一个印象：继承可以帮助我实现类的复用。所以，很多开发人员在需要复用一些代码的时候会很自然的使用类的继承的方式，因为书上就是这么写的（老师就是这么教的）。但是，其实这样做是不对的。长期大量的使用继承会给代码带来很高的维护成本。

本文将介绍组合和继承的概念及区别，并从多方面分析在写代码时如何进行选择。

- **面向对象的复用技术**

  前面提到复用，这里就简单介绍一下面向对象的复用技术。

  复用性是面向对象技术带来的很棒的潜在好处之一。如果运用的好的话可以帮助我们节省很多开发时间，提升开发效率。但是，如果被滥用那么就可能产生很多难以维护的代码。

  作为一门面向对象开发的语言，代码复用是Java引人注意的功能之一。Java代码的复用有继承，组合以及代理三种具体的表现形式。本文将重点介绍继承复用和组合复用。

- **继承**

  继承（Inheritance）是一种联结类与类的层次模型。指的是一个类（称为子类、子接口）继承另外的一个类（称为父类、父接口）的功能，并可以增加它自己的新功能的能力，继承是类与类或者接口与接口之间最常见的关系；继承是一种[`is-a`](https://zh.wikipedia.org/wiki/Is-a)关系。

- **组合**

  组合(Composition)体现的是整体与部分、拥有的关系，即[`has-a`](https://en.wikipedia.org/wiki/Has-a)的关系。

- **组合与继承的区别和联系**

  > 在`继承`结构中，父类的内部细节对于子类是可见的。所以我们通常也可以说通过继承的代码复用是一种`白盒式代码复用`。（如果基类的实现发生改变，那么派生类的实现也将随之改变。这样就导致了子类行为的不可预知性；）
  >
  > `组合`是通过对现有的对象进行拼装（组合）产生新的、更复杂的功能。因为在对象之间，各自的内部细节是不可见的，所以我们也说这种方式的代码复用是`黑盒式代码复用`。（因为组合中一般都定义一个类型，所以在编译期根本不知道具体会调用哪个实现类的方法）
  >
  > `继承`，在写代码的时候就要指名具体继承哪个类，所以，在`编译期`就确定了关系。（从基类继承来的实现是无法在运行期动态改变的，因此降低了应用的灵活性。）
  >
  > `组合`，在写代码的时候可以采用面向接口编程。所以，类的组合关系一般在`运行期`确定。

- **优缺点对比**

  | 组 合 关 系                                                  | 继 承 关 系                                                  |
  | ------------------------------------------------------------ | ------------------------------------------------------------ |
  | 优点：不破坏封装，整体类与局部类之间松耦合，彼此相对独立     | 缺点：破坏封装，子类与父类之间紧密耦合，子类依赖于父类的实现，子类缺乏独立性 |
  | 优点：具有较好的可扩展性                                     | 缺点：支持扩展，但是往往以增加系统结构的复杂度为代价         |
  | 优点：支持动态组合。在运行时，整体对象可以选择不同类型的局部对象 | 缺点：不支持动态继承。在运行时，子类无法选择不同的父类       |
  | 优点：整体类可以对局部类进行包装，封装局部类的接口，提供新的接口 | 缺点：子类不能改变父类的接口                                 |
  | 缺点：整体类不能自动获得和局部类同样的接口                   | 优点：子类能自动继承父类的接口                               |
  | 缺点：创建整体类的对象时，需要创建所有局部类的对象           | 优点：创建子类的对象时，无须创建父类的对象                   |

- **如何选择**

  相信很多人都知道面向对象中有一个比较重要的原则『多用组合、少用继承』或者说『组合优于继承』。从前面的介绍已经优缺点对比中也可以看出，组合确实比继承更加灵活，也更有助于代码维护。

  所以，

  > **建议在同样可行的情况下，优先使用组合而不是继承。**
  > **因为组合更安全，更简单，更灵活，更高效。**

  注意，并不是说继承就一点用都没有了，前面说的是【在同样可行的情况下】。有一些场景还是需要使用继承的，或者是更适合使用继承。

  > **继承要慎用，其使用场合仅限于你确信使用该技术有效的情况。一个判断方法是，问一问自己是否需要从新类向基类进行向上转型。如果是必须的，则继承是必要的。反之则应该好好考虑是否需要继承。**《[Java编程思想](http://s.click.taobao.com/t?e=m%3D2%26s%3DHzJzud6zOdocQipKwQzePOeEDrYVVa64K7Vc7tFgwiHjf2vlNIV67vo5P8BMUBgoEC56fBbgyn5pS4hLH%2FP02ckKYNRBWOBBey11vvWwHXSniyi5vWXIZhtlrJbLMDAQihpQCXu2JnPFYKQlNeOGCsYMXU3NNCg%2F&pvid=10_125.119.86.125_222_1458652212179)》
  >
  > **只有当子类真正是超类的子类型时，才适合用继承。换句话说，对于两个类A和B，只有当两者之间确实存在[`is-a`](https://zh.wikipedia.org/wiki/Is-a)关系的时候，类B才应该继承类A。**《[Effective Java](http://s.click.taobao.com/t?e=m%3D2%26s%3DwIPn8%2BNPqLwcQipKwQzePOeEDrYVVa64K7Vc7tFgwiHjf2vlNIV67vo5P8BMUBgoUOZr0mLjusdpS4hLH%2FP02ckKYNRBWOBBey11vvWwHXSniyi5vWXIZvgXwmdyquYbNLnO%2BjzYQLqKnzbV%2FMLqnMYMXU3NNCg%2F&pvid=10_125.119.86.125_345_1458652241780)》



#### 构造函数与默认构造函数

构造函数，是一种特殊的方法。 主要用来在创建对象时初始化对象， 即为对象成员变量赋初始值，总与new运算符一起使用在创建对象的语句中。 特别的一个类可以有多个构造函数，可根据其参数个数的不同或参数类型的不同来区分它们即构造函数的重载。

构造函数跟一般的实例方法十分相似；但是与其它方法不同，构造器没有返回类型，不会被继承，且可以有范围修饰符。构造器的函数名称必须和它所属的类的名称相同。 它承担着初始化对象数据成员的任务。

如果在编写一个可实例化的类时没有专门编写构造函数，多数编程语言会自动生成缺省构造器（默认构造函数）。默认构造函数一般会把成员变量的值初始化为默认值，如int -> 0，Integer -> null。

**Java继承关系中的构造函数特征：**

- 如果父类存在默认构造函数，那么子类在未显示调用(super(...))父类构造器的情况下，会默认调用父类的默认无参构造器，如果子类显示调用了父类的构造器则不会再默认调用父类的默认无参构造器了。
- 如果父类不存在默认构造函数，那么子类必须显示调用(super(...))父类的构造器。

示例：
```java
public class ConstructorInheritExample {

    static class Parent {

        //父类的无参构造器就算是private的，子类照样也能默认调用或者显示调用super()
        private Parent() {
            System.out.println("Parent()");
        }

        public Parent(String name) {
            System.out.println("Parent(String name)");
        }

    }

    static class Child extends Parent {

        public Child() {
            System.out.println("Child()");
        }

        public Child(String name) {
            System.out.println("Child(String name)");
        }

        public Child(String name, char sex) {
            super(name); //此处显示调用父类的有参构造器，则不会再默认调用父类的默认无参构造器了
            System.out.println("Child(String name, char sex)");
        }

    }

    static class Person {

        public Person(String name, char sex) {
            System.out.println("Person(String name, char sex)");
        }

    }

    static class Gentleman extends Person {

        public Gentleman(String name) {
            super(name, '男');
            System.out.println("Gentleman(String name)");
        }

    }

    public static void main(String[] args) {
        new Child();
        System.out.println("--------------------------------------");
        new Child("Tom");
        System.out.println("--------------------------------------");
        new Child("Tony", '男');
        System.out.println("--------------------------------------");
        new Gentleman("Jack");
    }

}
```
输出结果：
```shell
Parent()
Child()
--------------------------------------
Parent()
Child(String name)
--------------------------------------
Parent(String name)
Child(String name, char sex)
--------------------------------------
Person(String name, char sex)
Gentleman(String name)
```



#### 类变量、成员变量和局部变量

Java中共有三种变量，分别是类变量、成员变量和局部变量。他们分别存放在JVM的方法区、堆内存和栈内存中。

```java
    /**
     * @author Hollis
     */
    public class Variables {

        /**
         * 类变量
         */
        private static int a;

        /**
         * 成员变量
         */
        private int b;

        /**
         * 局部变量
         * @param c
         */
        public void test(int c){
            int d;
        }
    }
```

上面定义的三个变量中，变量a就是类变量，变量b就是成员变量，而变量c和d是局部变量。



#### 成员变量和方法作用域

对于成员变量和方法的作用域，<font color=#770088>public</font>，<font color=#770088>protected</font>，<font color=#770088>private</font>以及不写之间的区别：

<font color=#770088 size=4>`public`</font> : 表明该成员变量或者方法是对所有类或者对象都是可见的,所有类或者对象都可以直接访问

<font color=#770088 size=4>`private`</font> : 表明该成员变量或者方法是私有的,只有当前类对其具有访问权限,除此之外其他类或者对象都没有访问权限.子类也没有访问权限.

<font color=#770088 size=4>`protected`</font> : 表明成员变量或者方法对类自身,与同在一个包中的其他类可见,其他包下的类不可访问,除非是他的子类

<font color=#770088 size=4>`default`</font> : 表明该成员变量或者方法只有自己和其位于同一个包的内可见,其他包内的类不能访问,即便是它的子类



### 平台无关性

#### Java如何实现的平台无关性的

相信对于很多Java开发来说，在刚刚接触Java语言的时候，就听说过Java是一门跨平台的语言，Java是平台无关性的，这也是Java语言可以迅速崛起并风光无限的一个重要原因。那么，到底什么是平台无关性？Java又是如何实现平台无关性的呢？本文就来简单介绍一下。

- **什么是平台无关性**

  平台无关性就是一种语言在计算机上的运行不受平台的约束，一次编译，到处执行（Write Once ,Run Anywhere）。

  也就是说，用Java创建的可执行二进制程序，能够不加改变的运行于多个平台。

- **平台无关性的好处**

  作为一门平台无关性语言，无论是在自身发展，还是对开发者的友好度上都是很突出的。

  因为其平台无关性，所以Java程序可以运行在各种各样的设备上，尤其是一些嵌入式设备，如打印机、扫描仪、传真机等。随着5G时代的来临，也会有更多的终端接入网络，相信平台无关性的Java也能做出一些贡献。

  对于Java开发者来说，Java减少了开发和部署到多个平台的成本和时间。真正的做到一次编译，到处运行。

- **平台无关性的实现**

  对于Java的平台无关性的支持，就像对安全性和网络移动性的支持一样，是分布在整个Java体系结构中的。其中扮演者重要的角色的有Java语言规范、Class文件、Java虚拟机（JVM）等。

- **小结**

  对于Java的平台无关性的支持是分布在整个Java体系结构中的。其中扮演着重要角色的有Java语言规范、Class文件、Java虚拟机等。

  - Java语言规范
    - 通过规定Java语言中基本数据类型的取值范围和行为
  - Class文件
    - 所有Java文件要编译成统一的Class文件
  - Java虚拟机
    - 通过Java虚拟机将Class文件转成对应平台的二进制文件等

  **Java的平台无关性是建立在Java虚拟机的平台有关性基础之上的，是因为Java虚拟机屏蔽了底层操作系统和硬件的差异。**

- **语言无关性**

  其实，Java的无关性不仅仅体现在平台无关性上面，向外扩展一下，Java还具有语言无关性。

  前面我们提到过。JVM其实并不是和Java文件进行交互的，而是和Class文件，也就是说，其实JVM运行的时候，并不依赖于Java语言。

  时至今日，商业机构和开源机构已经在Java语言之外发展出一大批可以在JVM上运行的语言了，如Groovy、Scala、Jython等。之所以可以支持，就是因为这些语言也可以被编译成字节码（Class文件）。而虚拟机并不关心字节码是有哪种语言编译而来的。



### 值传递

#### 值传递、引用传递

- **实参与形参**

  我们都知道，在Java中定义方法的时候是可以定义参数的。比如Java中的main方法，`public static void main(String[] args)`，这里面的args就是参数。参数在程序语言中分为形式参数和实际参数。

  > 形式参数：是在定义函数名和函数体的时候使用的参数,目的是用来接收调用该函数时传入的参数。
  >
  > 实际参数：在调用有参函数时，主调函数和被调函数之间有数据传递关系。在主调函数中调用一个函数时，函数名后面括号中的参数称为“实际参数”。

- **值传递与引用传递**

  上面提到了，当我们调用一个有参函数的时候，会把实际参数传递给形式参数。但是，在程序语言中，这个传递过程中传递的两种情况，即值传递和引用传递。我们来看下程序语言中是如何定义和区分值传递和引用传递的。

  > 值传递（pass by value）是指在调用函数时将实际参数`复制`一份传递到函数中，这样在函数中如果对`参数`进行修改，将不会影响到实际参数。
  >
  > 引用传递（pass by reference）是指在调用函数时将实际参数的地址`直接`传递到函数中，那么在函数中对`参数`所进行的修改，将影响到实际参数。
  
  那么，我来给大家总结一下，值传递和引用传递之前的区别的重点是什么：
  
  ![pass](http://www.hollischuang.com/wp-content/uploads/2018/04/pass.jpg)



#### 为什么说Java中只有值传递

在Java的方法中，在传递原始类型的时候无疑是值传递。在传递对象类型的时候是引用传递也是值传递，只不过是把对象的引用当做值传递给方法，传递的是对象的引用地址（指针），而不是对象的拷贝，Java会将对象的引用地址的拷贝传递给被调函数的形式参数。也可以这么理解只要是分配在栈中的都是值传递，所以Java只有值传递。



## Java基础知识

### 基本数据类型



#### 8种基本数据类型

Java中有8种基本数据类型分为三大类。

- 字符型: char
- 布尔型: boolean
- 数值型: 整型(byte，short，int，long)、浮点型(float，double)

1. **char**
- char类型是一个单一的 16 位 Unicode 字符；
   - 最小值是 \u0000（即为0）；
   - 最大值是 \uffff（即为65,535）；
   - char 数据类型可以储存任何字符；
   - 默认值是 Character.MIN_VALUE；
   - 例子：char letter = 'A';

2. boolean

   - boolean数据类型表示一位的信息；
   - 只有两个取值：true 和 false；
   - 这种类型只作为一种标志来记录 true/false 情况；
   - 默认值是 false；
   - 例子：boolean one = true。

3. byte

   - byte 数据类型是8位、有符号的，以二进制补码表示的整数；
   - 最小值是 -128（-2^7）；
   - 最大值是 127（2^7-1）；
   - 默认值是 0；
   - byte 类型用在大型数组中节约空间，主要代替整数，因为 byte 变量占用的空间只有 int 类型的四分之一；
   - 例子：byte a = 100，byte b = -50。

4. short

   - short 数据类型是 16 位、有符号的以二进制补码表示的整数；
   - 最小值是 -32768（-2^15）；
   - 最大值是 32767（2^15 - 1）；
   - Short 数据类型也可以像 byte 那样节省空间。一个short变量是int型变量所占空间的二分之一；
   - 默认值是 0；
   - 例子：short s = 1000，short r = -20000。

5. int

   - int 数据类型是32位、有符号的以二进制补码表示的整数；
   - 最小值是 -2,147,483,648（-2^31）；
   - 最大值是 2,147,483,647（2^31 - 1）；
   - 一般地整型变量默认为 int 类型；
   - 默认值是 0 ；
   - 例子：int a = 100000, int b = -200000。

6. long

   - long 数据类型是 64 位、有符号的以二进制补码表示的整数；
   - 最小值是 -9,223,372,036,854,775,808（-2^63）；
   - 最大值是 9,223,372,036,854,775,807（2^63 -1）；
   - 这种类型主要使用在需要比较大整数的系统上；
   - 默认值是 0L；
   - 例子： long a = 100000L，Long b = -200000L。
   - "L"理论上不分大小写，但是若写成"l"容易与数字"1"混淆，不容易分辩。所以最好大写。

7. float

   - float 数据类型是单精度、32位、符合IEEE 754标准的浮点数；
   - float 在储存大型浮点数组的时候可节省内存空间；
   - 默认值是 0.0f；
   - 浮点数不能用来表示精确的值，如货币；
   - 例子：float f1 = 234.5f。

8. double

   - double 数据类型是双精度、64 位、符合IEEE 754标准的浮点数；
   - 浮点数的默认类型为double类型；
   - double类型同样不能表示精确的值，如货币；
   - 默认值是 0.0d；
   - 例子：double d1 = 123.4。

> Java中的数值类型不存在无符号的，它们的取值范围是固定的，不会随着机器硬件环境或者操作系统的改变而改变。
>
> 实际上，Java中还存在另外一种基本类型`void`，它也有对应的包装类 `java.lang.Void`，不过我们无法直接对它们进行操作。



#### 基本数据类型有什么好处

我们都知道在Java语言中，`new`一个对象是存储在堆里的，我们通过栈中的引用来使用这些对象；所以，对象本身来说是比较消耗资源的。

对于经常用到的类型，如int等，如果我们每次使用这种变量的时候都需要new一个Java对象的话，就会比较笨重。所以，和C++一样，**Java提供了基本数据类型，这种数据的变量不需要使用new创建，他们不会在堆上创建，而是直接在栈内存中存储，因此会更加高效**。



#### Java数值类型提升机制

- 类型转换的问题

  **我们只需要记一句话小转大自动转，大转小需要强制转换。**

- **数值提升**

  数字类型提升机制被用于算术运算符上，通常使用场景为：

  - 同一类型转换

    虽然并无什么作用，但有时可以使代码更清晰。

  - 拓宽原始类型转换

    指byte、short、int、long、float、double由低向高转换。

  - 自动拆箱转换

    基础类型引用类的拆箱方法，如`r.intValue()`。

数值提升用于将算术运算中的操作数转化为一个相同的类型以便于运算，具体分为两种情况：一元数值提升和二元数值提升。

- **一元数值提升**

  - if 操作数是**编译时类型**Byte、Short、Character或Integer，那么它会先拆箱为对应的原始类型，然后拓宽为int类型。

  - else if 操作数是**编译时类型**Long、Float或Double，那么就直接拆箱为对应的原始类型。

  - else if 操作数是**编译时类型**byte、short、char或int，那么就拓宽为int类型。

  - else 保持原样。

  ```java
    /**
     * 一元数值提升示例
     */
    public static void unaryPromotionTest() {
        byte b = 2;
        int[] a = new int[b]; //数组创建表达式的变量提升
        char c = '\u0001';
        a[c] = 1; //数组索引表达式的变量提升
        a[0] = -c; //正负号运算符的变量提升
        System.out.println("a: " + a[0] + "," + a[1]);
        b = -1;
        int i = ~b; //按位补运算符(~)的变量提升
        System.out.println("~0x" + Integer.toHexString(b) + "==0x" + Integer.toHexString(i));
        i = b << 4L; // 移位运算符(>>, >>>, << )的每一个操作数(左操作数)的变量提升,(运算符右边不会发生提升)
        System.out.println("0x" + Integer.toHexString(b) + "<<4L==0x" + Integer.toHexString(i));
    }
  ```

- **二数值提升**

  当**二元运算符**的操作数皆可转化为数字类型时，那么将采用如下二元数值提升规则：

  1. 如果任一操作数为引用类型，那么对其进行自动拆箱转为原始类型。

  2. 原始类型转换被应用于以下情况：

     - if 某一操作数为`double`类型，那么另一个也转为`double`
     - else if 某一操作数为`float`类型，那么另一个也转为`float`
     - else if 某一操作数为`long`类型，那么另一个也转为`long`
     - else **两个操作数都转为`int`**
  ```java
      /**
       * 二元数值提升示例
       */
      public static void binaryPromotionTest1() {
          Integer i = 2;
          float f = 1.0f;
          double d = 2.0;
          //Integer * float 被自动拆箱为 int * float
          //int * float 被提升为 float * float，然后
          //float == double 被提升为 double == double
          if (i * f == d) {
              System.out.println("Oops");
          }
  
          byte b = 0x1f;
          char c = 'G';
          int control = c & b;
          System.out.println(Integer.toHexString(control));
  
          //此处 Integer : float 先是被自动拆箱为 int : float ，然后被提升为 float : float
          f = (b==0) ? i : 4.0f;
          System.out.println(1.0/f);
      }
  ```
  
- **三元条件运算符(? :)中的类型提升**

  - if 第二个操作数和第三个操作数有相同的类型（可以都为null），那么它就是条件表达式的类型。

  - else if 两个操作数中有一个的类型为原始类型T，而另一个为T的装箱类型，那么条件表达式的类型就是T。

  - else if 其中一个操作数是编译时null类型，另一个为引用类型，那么条件表达式的类型就是该引用类型。

  - else 太复杂了(略)

  ```java
      /**
       * 三元数值提升示例
       */
      public static void ternaryPromotionTest1() {
          //编译器无法确定null的静态类型，所以得让第三个参数1进行自动装箱
          Object k = true ? null : 1;
          System.out.println(k); //输出 null
      }
  
      /**
       * 三元数值提升示例
       */
      public static void ternaryPromotionTest2() {
          Integer a = null;
          //a的静态类型是Integer，根据规则a需要自动拆箱为int，于是报了NPE
          //此句等效于：Object k = true ? (Integer)null : 1;
          Object k = true ? a : 1; //此处报NPE
          System.out.println(k);
      }
  ```

- **总结一下**

  **运算中的类型提升通常都是先将包装类型自动拆箱，然后将低于`int`位数的类型提升为`int`，高于`int`的拆箱后保持不变，两边操作数位数不同则升为高精度的那一个类型。**

- **练练手**
  
  ```java
    /**
     * 一元数值提升示例
     */
    public static void unaryPromotionTest2() {
        byte b = 1;
        //其实数值类型的++,--操作都会发生变量提升，只是里面隐含了一层强制类型转换：byte c = (byte)(b + 1)
        byte c = b++; //编译通过
        byte d = ++b; //编译通过
        //byte e = b + 1; //编译报错
        System.out.println(c + ", " + d);
  
        short s1 = 1;
        //short s2 = s1 + 1; //编译报错
        //+=是java语言规定的运算符，java编译器会对它进行特殊处理，该语句相当于 s1 = (short)(s1 + 1);
        s1 += 1; //编译通过
        System.out.println(s1);
  
        char c1 = 'A';
        int i1 = c1; //隐式转换
        int i2 = c1 + 1; //变量提升：int + int
  
        /**
         * 从JVM的角度来说，switch语句只认int类型，因此
         * 1、switch支持byte, short, char存在小转大的隐式转换为int
         * 2、switch支持enum, 本质上是由于enum的ordinal()方法支持
         * 3、switch支持String, 本质上是由于hashcode()方法支持，当然也要结合equals()方法
         */
        int flag = 0;
        switch (flag) {
            case 1:
                System.out.println("1");
                break;
            case 2:
                System.out.println("2");
                break;
            default :
                System.out.println("0");
                break;
      }
  
        Thread.State state = Thread.State.BLOCKED;
        switch (state) {
            case BLOCKED:
                System.out.println("BLOCKED");
                break;
            case NEW:
                System.out.println("NEW");
                break;
            default :
                System.out.println("RUNNABLE");
                break;
      }
  
    }
  ```



#### 为什么不能用浮点型表示金额？

由于计算机中保存的小数其实是十进制的小数的近似值，并不是准确值，所以，千万不要在代码中使用浮点数来表示金额等重要的指标。

建议使用BigDecimal或者Long（单位为分）来表示金额。

附[阿里巴巴面试题](https://mp.weixin.qq.com/s?__biz=MzI3NzE0NjcwMg==&mid=2650127279&idx=2&sn=c59f538c42330fa57982d4ccb9103adf&chksm=f36ba68ec41c2f98f188d8b36d41d4012abc0ae4dc554f903c29566f61cd3c0080bc2d665817&mpshare=1&scene=1&srcid=&sharer_sharetime=1587773051881&sharer_shareid=5edb85262027f5ebdd4d8b4f2e55549a&key=2c65a839c81ba4f091a011aa905ca1f5e99456d2ccf22125569b937ea93d25841fc3d6393898717c02fadfa31289a245f7b256dab2edbb197cc8656fefec91058016fc574724005db3fe58f23b3663ce&ascene=1&uin=OTU4MTMxNTE0&devicetype=Windows+10+x64&version=62090529&lang=zh_CN&exportkey=ASqSi%2BgCKVNcxmph%2BZUL6o4%3D&pass_ticket=ULGMNfO8QjEfv6lXulizpnhZUY43hB76ubp879VSO7xDd7tn3BFdcCfp96BVlh8q)



### 自动拆装箱

#### 自动拆装箱

- **包装类型**

  Java语言是一个面向对象的语言，但是Java中的基本数据类型却是不面向对象的，这在实际使用时存在很多的不便，为了解决这个不足，在设计类时为每个基本数据类型设计了一个对应的类进行代表，这样八个和基本数据类型对应的类统称为包装类(Wrapper Class)。

  包装类均位于java.lang包，包装类和基本数据类型的对应关系如下表所示

  | 基本数据类型 | 包装类    |
  | ------------ | --------- |
  | byte         | Byte      |
  | boolean      | Boolean   |
  | short        | Short     |
  | char         | Character |
  | int          | Integer   |
  | long         | Long      |
  | float        | Float     |
  | double       | Double    |

  在这八个类名中，除了Integer和Character类以后，其它六个类的类名和基本数据类型一致，只是类名的第一个字母大写即可。

- **为什么需要包装类**

  很多人会有疑问，既然Java中为了提高效率，提供了八种基本数据类型，为什么还要提供包装类呢？

  这个问题，其实前面已经有了答案，因为Java是一种面向对象语言，很多地方都需要使用对象而不是基本数据类型。比如，在集合类中，我们是无法将int 、double等类型放进去的。因为集合的容器要求元素是Object类型。

  为了让基本类型也具有对象的特征，就出现了包装类型，它相当于将基本类型“包装起来”，使得它具有了对象的性质，并且为其添加了属性和方法，丰富了基本类型的操作。

- **拆箱与装箱**

  那么，有了基本数据类型和包装类，肯定有些时候要在他们之间进行转换。比如把一个基本数据类型的int转换成一个包装类型的Integer对象。

  我们认为包装类是对基本类型的包装，所以，把基本数据类型转换成包装类的过程就是打包装，英文对应于boxing，中文翻译为装箱。

  反之，把包装类转换成基本数据类型的过程就是拆包装，英文对应于unboxing，中文翻译为拆箱。

  **在Java SE5之前**，要进行装箱，可以通过以下代码：

  ```java
      Integer i = new Integer(10);
  ```

- **自动拆箱与自动装箱**

  在Java SE5中，为了减少开发人员的工作，Java提供了自动拆箱与自动装箱功能。

  自动装箱：就是将基本数据类型自动转换成对应的包装类。

  自动拆箱：就是将包装类自动转换成对应的基本数据类型。

  ```java
      Integer i = 10;  //自动装箱
      int b = i;     //自动拆箱
  ```

  `Integer i = 10` 可以替代 `Integer i = new Integer(10);`，这就是因为Java帮我们提供了自动装箱的功能，不需要开发者手动去new一个Integer对象。

- **自动拆装箱实现原理**

  既然Java提供了自动拆装箱的能力，那么，我们就来看一下，到底是什么原理，Java是如何实现的自动拆装箱功能。

  我们有以下自动拆装箱的代码：

  ```java
      public static  void main(String[]args){
          Integer integer = 1; //装箱
          int i = integer; //拆箱
      }
  ```
  对以上代码进行反编译后可以得到以下代码：
  
  ```java
      public static  void main(String[]args){
          Integer integer = Integer.valueOf(1); 
          int i = integer.intValue(); 
      }
  ```
  
  从上面反编译后的代码可以看出，int的自动装箱都是通过Integer.valueOf()方法来实现的，Integer的自动拆箱都是通过integer.intValue来实现的。如果读者感兴趣，可以试着将八种类型都反编译一遍 ，你会发现以下规律：
  
  > **自动装箱都是通过包装类的`valueOf()`方法来实现的，自动拆箱都是通过包装类对象的`xxxValue()`来实现的。**
  
- **哪些地方会自动拆装箱**

  我们了解过原理之后，在来看一下，什么情况下，Java会帮我们进行自动拆装箱。前面提到的变量的初始化和赋值的场景就不介绍了，那是最简单的也最容易理解的。

  我们主要来看一下，那些可能被忽略的场景。

  - **场景一、将基本数据类型放入集合类**

    我们知道，Java中的集合类只能接收对象类型，那么以下代码为什么会不报错呢？

    ```java
        List<Integer> li = new ArrayList<>();
        for (int i = 1; i < 50; i ++){
            li.add(i); //反编译后的代码：li.add(Integer.valueOf(i));
        }
    ```

  - **场景二、包装类型和基本类型的大小比较**

    ```java
        Integer a = 1;
        System.out.println(a == 1 ? "等于" : "不等于"); //相当于：sout(a.intValue() == 1 ? "等于" : "不等于");
        Boolean bool = false;
        System.out.println(bool ? "真" : "假"); //相当于：sout(bool.booleanValue() ? "真" : "假");
    ```

  - **场景三、包装类型的运算**

    ```java
        Integer i = 10;
        Integer j = 20;
    
        System.out.println(i+j); //由于“+”运算符不支持对象，所以铁定要自动拆箱转为int在进行“+”操作运算
    ```

  - **场景四、三目运算符的使用**

    ```java
        boolean flag = true;
        Integer i = 0;
        int j = 1;
        int k = flag ? i : j;
    ```
    反编译后代码：
    
    ```java
        boolean flag = true;
        Integer i = Integer.valueOf(0);
        int j = 1;
        int k = flag ? i.intValue() : j;
        System.out.println(k);
    ```
    
  - **场景五、函数参数与返回值**

    ```java
        //自动拆箱
        public int getNum1(Integer num) {
         	return num;
        }
        //自动装箱
        public Integer getNum2(int num) {
         	return num;
        }
    ```

- **自动拆装箱与缓存**

  确切地说，Java原始类型中除了浮点型(float, double)以外，在自动装箱时都有缓存效应，当然缓存是有界限的。具体缓存值范围是：

  - 整型：-128至127之间的整数，共256个数值。

  - 布尔型：true 和 false的布尔值，共true/false两个。

  - 字符型：‘\u0000’至 ‘\u007f’之间的字符，共128个字符。

  **对于int类型这个范围也是可以指定的，通过-XX:AutoBoxCacheMax=size参数指定，默认为128，也可以通过java.lang.Integer.IntegerCache.high设置最大值**

  示例：

  ```java
      /**
       * 确切地说，Java原始类型中除了浮点型(float, double)以外，在自动装箱时都有缓存效应，当然缓存是有界限的。具体缓存值范围是：
       *
       * - 整型：-128至127之间的整数，共256个数值。
       *
       * - 布尔型：true 和 false的布尔值，共true/false两个。
       *
       * - 字符型：‘\u0000’至 ‘\u007f’之间的字符，共128个字符。
       *
       * 当然这个范围也是可以指定的，通过-XX:AutoBoxCacheMax参数指定，默认为128
       */
      public static void main(String[] args) {
          Boolean bool1 = true;
          Boolean bool2 = true;
          System.out.println(String.format("【%s】%s == %s : %s", "Boolean", bool1, bool2, bool1 == bool2));
  
          bool1 = false;
          bool2 = false;
          System.out.println(String.format("【%s】%s == %s : %s", "Boolean", bool1, bool2, bool1 == bool2));
  
          Byte b1 = 1;
          Byte b2 = 1;
          System.out.println(String.format("【%s】%s == %s : %s", "Byte", b1, b2, b1 == b2));
  
          b1 = 123;
          b2 = 123;
          System.out.println(String.format("【%s】%s == %s : %s", "Byte", b1, b2, b1 == b2));
  
          Short s1 = 1;
          Short s2 = 1;
          System.out.println(String.format("【%s】%s == %s : %s", "Short", s1, s2, s1 == s2));
  
          s1 = 127;
          s2 = 127;
          System.out.println(String.format("【%s】%s == %s : %s", "Short", s1, s2, s1 == s2));
  
          s1 = 128;
          s2 = 128;
          System.out.println(String.format("【%s】%s == %s : %s", "Short", s1, s2, s1 == s2));
  
          Integer i1 = 1;
          Integer i2 = 1;
          System.out.println(String.format("【%s】%s == %s : %s", "Integer", i1, i2, i1 == i2));
  
          i1 = 124;
          i2 = 124;
          System.out.println(String.format("【%s】%s == %s : %s", "Integer", i1, i2, i1 == i2));
  
          i1 = 138;
          i2 = 138;
          System.out.println(String.format("【%s】%s == %s : %s", "Integer", i1, i2, i1 == i2));
  
          Long l1 = 1L;
          Long l2 = 1L;
          System.out.println(String.format("【%s】%s == %s : %s", "Long", l1, l2, l1 == l2));
  
          l1 = 125L;
          l2 = 125L;
          System.out.println(String.format("【%s】%s == %s : %s", "Long", l1, l2, l1 == l2));
  
          l1 = 139L;
          l2 = 139L;
          System.out.println(String.format("【%s】%s == %s : %s", "Long", l1, l2, l1 == l2));
  
          Character c1 = '\u0001';
          Character c2 = '\u0001';
          System.out.println(String.format("【%s】%s == %s : %s", "Character", c1, c2, c1 == c2));
  
          c1 = '\u007f'; //十进制127
          c2 = '\u007f'; //十进制127
          System.out.println(String.format("【%s】%s == %s : %s", "Character", c1, c2, c1 == c2));
  
          c1 = '\u008f'; //十进制143
          c2 = '\u008f'; //十进制143
          System.out.println(String.format("【%s】%s == %s : %s", "Character", c1, c2, c1 == c2));
      }
  ```
  输出结果：
  ```shell
  【Boolean】true == true : true
  【Boolean】false == false : true
  【Byte】1 == 1 : true
  【Byte】123 == 123 : true
  【Short】1 == 1 : true
  【Short】127 == 127 : true
  【Short】128 == 128 : false
  【Integer】1 == 1 : true
  【Integer】124 == 124 : true
  【Integer】138 == 138 : false //如果设置JVM参数：-XX:AutoBoxCacheMax=200后，此处输出true
  【Long】1 == 1 : true
  【Long】125 == 125 : true
  【Long】139 == 139 : false
  【Character】 ==  : true
  【Character】 ==  : true
  【Character】 ==  : false
  ```
- **自动拆装箱带来的问题**

  当然，自动拆装箱是一个很好的功能，大大节省了开发人员的精力，不再需要关心到底什么时候需要拆装箱。但是，他也会引入一些问题。

  > 包装对象的数值比较，不能简单的使用`==`，虽然-128到127之间的数字可以，但是这个范围之外还是需要使用`equals`比较。
  >
  > 前面提到，有些场景会进行自动拆装箱，同时也说过，由于自动拆箱，如果包装类对象为null，那么自动拆箱时就有可能抛出NPE。
  >
  > 如果一个for循环中有大量拆装箱操作，会影响性能。

  

### String

#### 字符串的不可变性

- **定义一个字符串**

  ```java
  String s = "abcd";
  ```
  ![String-Immutability-1](http://www.programcreek.com/wp-content/uploads/2009/02/String-Immutability-1.jpeg)
  
  `s`中保存了string对象的引用。下面的箭头可以理解为“存储他的引用”。
  
- **使用变量来赋值变量**

  ```java
  String s2 = s;
  ```
  ![String-Immutability-2](http://www.programcreek.com/wp-content/uploads/2009/02/String-Immutability-2.jpeg)
  
  `s2`保存了相同的引用值，因为他们代表同一个对象。
  
- **字符串连接**

  ```java
  s = s.concat("ef");
  ```
  ![string-immutability](http://www.programcreek.com/wp-content/uploads/2009/02/string-immutability-650x279.jpeg)
  
  `s`中保存的是一个重新创建出来的string对象的引用。
  
- **总结**

  一旦一个string对象在内存(堆)中被创建出来，他就无法被修改。特别要注意的是，String类的所有方法都没有改变字符串本身的值，都是返回了一个新的对象。

  如果你需要一个可修改的字符串，应该使用StringBuffer 或者 StringBuilder。否则会有大量时间浪费在垃圾回收上，因为每次试图修改都有新的string对象被创建出来。

  **所以"Java中的字符串为什么是不可变的？"，我的回答是：Java中字符串由唯一一个类java.lang.String表示，该类是final的不可被派生，导致其不可变特性的根本原因还是String类的所有方法都没有改变字符串本身的值，都是返回了一个新的String对象。**



#### JDK 6和JDK 7中substring的原理及区别

String是Java中一个比较基础的类，每一个开发人员都会经常接触到。而且，String也是面试中经常会考的知识点。String有很多方法，有些方法比较常用，有些方法不太常用。今天要介绍的subString就是一个比较常用的方法，而且围绕subString也有很多面试题。

`substring(int beginIndex, int endIndex)`方法在不同版本的JDK中的实现是不同的。了解他们的区别可以帮助你更好的使用他。为简单起见，后文中用`substring()`代表`substring(int beginIndex, int endIndex)`方法。

- substring()的作用

  `substring(int beginIndex, int endIndex)`方法截取字符串并返回其[beginIndex,endIndex-1]范围内的内容。

  ```java
  String x = "abcdef";
  x = x.substring(1,3);
  System.out.println(x);
  ```
  输出内容：
  ```shell
  bc
  ```
  
- **调用substring()时发生了什么？**

  你可能知道，因为x是不可变的，当使用`x.substring(1,3)`对x赋值的时候，它会指向一个全新的字符串：

  ![string-immutability1](http://www.programcreek.com/wp-content/uploads/2013/09/string-immutability1-650x303.jpeg)

  然而，这个图不是完全正确的表示堆中发生的事情。因为在jdk6 和 jdk7中调用substring时发生的事情并不一样。

- **JDK 6中的substring()**

  String是通过字符数组实现的。在jdk 6 中，String类包含三个成员变量：`char value[]`， `int offset`，`int count`。他们分别用来存储真正的字符数组，数组的第一个位置索引以及字符串中包含的字符个数。

  当调用substring方法的时候，会创建一个新的string对象，但是这个string的值仍然指向堆中的同一个字符数组。这两个对象中只有count和offset 的值是不同的。

  ![string-substring-jdk6](http://www.programcreek.com/wp-content/uploads/2013/09/string-substring-jdk6-650x389.jpeg)

  下面是证明上说观点的Java源码中的关键代码：

  ```java
  //JDK 6
  String(int offset, int count, char value[]) {
      this.value = value;
      this.offset = offset;
      this.count = count;
  }
  
  public String substring(int beginIndex, int endIndex) {
      //check boundary
      return  new String(offset + beginIndex, endIndex - beginIndex, value);
  }
  ```

- **JDK 6中的substring()导致的问题**

  如果你有一个很长很长的字符串，但是当你使用substring进行切割的时候你只需要很短的一段。这可能导致性能问题，因为你需要的只是一小段字符序列，但是你却引用了整个字符串（因为这个非常长的字符数组一直在被引用，所以无法被回收，就可能导致内存泄露，这样的bug是实际存在的且被记录在官方的Java Bug Database中）。在JDK 6中，一般用以下方式来解决该问题，原理其实就是生成一个新的字符串并引用他。

  ```java
  x = x.substring(x, y) + ""
  ```

- **JDK 7 中的substring()**

  上面提到的问题，在jdk 7中得到解决。在jdk 7 中，substring()方法会在堆内存中创建一个新的数组。

  ![string-substring-jdk7](http://www.programcreek.com/wp-content/uploads/2013/09/string-substring-jdk71-650x389.jpeg)

  Java源码中关于这部分的主要代码如下：

  ```java
  //JDK 7
  public String(char value[], int offset, int count) {
      //check boundary
      this.value = Arrays.copyOfRange(value, offset, offset + count);
  }
  
  public String substring(int beginIndex, int endIndex) {
      //check boundary
      int subLen = endIndex - beginIndex;
      return new String(value, beginIndex, subLen);
  }
  ```
  以上是JDK 7中的subString()方法，其使用`new String`创建了一个新字符串，避免对老字符串的引用。从而解决了内存泄露问题。
  
  所以，如果你的生产环境中使用的JDK版本小于1.7，当你使用String的subString方法时一定要注意，避免内存泄露。
  
  
#### replaceFirst、replaceAll、replace区别

replace、replaceAll和replaceFirst是Java中常用的替换字符的方法,它们的方法定义是：

replace(CharSequence target, CharSequence replacement) ，用replacement替换所有的target，两个参数都是字符串。

replaceAll(String regex, String replacement) ，用replacement替换所有的regex匹配项，regex很明显是个正则表达式，replacement是字符串。

replaceFirst(String regex, String replacement) ，基本和replaceAll相同，区别是只替换第一个匹配项。

**可以看到，其中replaceAll以及replaceFirst是和正则表达式有关的，而replace和正则表达式无关。**

replaceAll和replaceFirst的区别主要是替换的内容不同，replaceAll是替换所有匹配的字符，而replaceFirst()仅替换第一次出现的字符



#### String对“+”的重载

1. **String s = "a" + "b"，编译器会进行常量折叠(因为两个都是编译期常量，编译期可知)，即变成 String s = "ab"**
2. **对于能够进行优化的(String s = "a" + 变量 等)用 StringBuilder 的 append() 方法替代，最后调用 toString() 方法 (底层就是一个 new String())**



#### String.valueOf和Integer.toString的区别

我们有三种方式将一个int类型的变量变成呢过String类型，那么他们有什么区别？

```java
1.int i = 5;
2.String i1 = "" + i;
3.String i2 = String.valueOf(i);
4.String i3 = Integer.toString(i);
```

第三行和第四行没有任何区别，因为String.valueOf(i)也是调用Integer.toString(i)来实现的。

第二行代码其实是String i1 = (new StringBuilder()).append(i).toString();，首先创建一个StringBuilder对象，然后再调用append方法，再调用toString方法。



**switch语句对String的支持**

Java 7中，switch的参数可以是String类型了，这对我们来说是一个很方便的改进。到目前为止switch支持这样几种数据类型：`byte` `short` `int` `char` `String` 。但是，作为一个程序员我们不仅要知道他有多么好用，还要知道它是如何实现的，switch对整型的支持是怎么实现的呢？对字符型是怎么实现的呢？String类型呢？有一点Java开发经验的人这个时候都会猜测switch对String的支持是使用equals()方法和hashcode()方法。那么到底是不是这两个方法呢？接下来我们就看一下，switch到底是如何实现的。

1. **switch对整型支持的实现**

   下面是一段很简单的Java代码，定义一个int型变量a，然后使用switch语句进行判断。执行这段代码输出内容为5，那么我们将下面这段代码反编译，看看他到底是怎么实现的。

   ```java
   public class switchDemoInt {
       public static void main(String[] args) {
           int a = 5;
           switch (a) {
           case 1:
               System.out.println(1);
               break;
           case 5:
               System.out.println(5);
               break;
           default:
               break;
           }
       }
   }
   //output 5
   ```
   反编译后的代码如下：
   
   ```java
   public class switchDemoInt
   {
       public switchDemoInt()
       {
       }
       public static void main(String args[])
       {
           int a = 5;
           switch(a)
           {
           case 1: // '\001'
               System.out.println(1);
               break;
   
           case 5: // '\005'
               System.out.println(5);
               break;
           }
       }
   }
   ```
   我们发现，反编译后的代码和之前的代码比较除了多了两行注释以外没有任何区别，那么我们就知道，**switch对int的判断是直接比较整数的值**。
   
2. **switch对字符型支持的实现**

   ```java
   public class switchDemoInt {
       public static void main(String[] args) {
           char a = 'b';
           switch (a) {
           case 'a':
               System.out.println('a');
               break;
           case 'b':
               System.out.println('b');
               break;
           default:
               break;
           }
       }
   }
   ```
   编译后的代码如下： 
   
   ```java
   public class switchDemoChar
   {
       public switchDemoChar()
       {
       }
       public static void main(String args[])
       {
           char a = 'b';
           switch(a)
           {
           case 97: // 'a'
               System.out.println('a');
               break;
           case 98: // 'b'
               System.out.println('b');
               break;
           }
      }
   }
   ```
   通过以上的代码作比较我们发现：对char类型进行比较的时候，实际上比较的是ascii码，编译器会把char型变量转换成对应的int型变量
   
3. **switch对字符串支持的实现**

   ```java
   public class switchDemoString {
       public static void main(String[] args) {
           String str = "world";
           switch (str) {
           case "hello":
               System.out.println("hello");
               break;
           case "world":
               System.out.println("world");
               break;
           default:
               break;
           }
       }
   }
   ```
   对代码进行反编译：
   
   ```java
   public class switchDemoString
   {
       public switchDemoString()
       {
       }
       public static void main(String args[])
       {
           String str = "world";
           String s;
           switch((s = str).hashCode())
           {
           default:
               break;
           case 99162322:
               if(s.equals("hello"))
                   System.out.println("hello");
               break;
           case 113318802:
               if(s.equals("world"))
                   System.out.println("world");
               break;
           }
       }
   }
   ```
   看到这个代码，你知道原来字符串的switch是通过`equals()`和`hashCode()`方法来实现的。**记住，switch中只能使用整型**，比如`byte`。`short`，`char`(ackii码是整型)以及`int`。还好`hashCode()`方法返回的是`int`，而不是`long`。通过这个很容易记住`hashCode`返回的是`int`这个事实。仔细看下可以发现，进行`switch`的实际是哈希值，**然后通过使用equals方法比较进行安全检查，这个检查是必要的，因为哈希可能会发生碰撞(为什么重写hashcode方法也要重写equals方法，你就可以这么举例论证)**。因此它的性能是不如使用枚举进行switch或者使用纯整数常量，但这也不是很差。因为Java编译器只增加了一个`equals`方法，如果你比较的是字符串字面量的话会非常快，比如”abc” ==”abc”。如果你把`hashCode()`方法的调用也考虑进来了，那么还会再多一次的调用开销，因为字符串一旦创建了，它就会把哈希值缓存起来。因此如果这个`switch`语句是用在一个循环里的，比如逐项处理某个值，或者游戏引擎循环地渲染屏幕，这里`hashCode()`方法的调用开销其实不会很大。

好，以上就是关于switch对整型、字符型、和字符串型的支持的实现方式，总结一下我们可以发现，**其实switch只支持一种数据类型，那就是整型，其他数据类型都是转换成整型之后在使用switch的**。



#### 方法区和永久代及元空间的区别
- **三者历史**：

  说到方法区，不得不提一下“永久代”这个概念，尤其是在JDK 8以 前，许多Java程序员都习惯在HotSpot虚拟机上开发、部署程序，很多人都更愿意把方法区称呼为“永久代”（Permanent Generation），或将两者混为一谈。本质上这两者并不是等价的，因为仅仅是当时的HotSpot虚拟机设计团队选择把收集器的分代设计扩展至方法区，或者说使用永久代来实现方法区而已，这样使得HotSpot的垃圾收集器能够像管理Java堆一样管理这部分内存，省去专门为方法区编写内存管理代码的工作。但是对于其他虚拟机实现，譬如BEA JRockit、IBM J9等来说，是不存在永久代的概念的。但现在回头来看，当年使用永久代来实现方法区的决定并不是一个好主意，这种设计导致了Java应 用更容易遇到内存溢出的问题（永久代有-XX:MaxPermSize的上限， 即使不设置也有默认大小，而J9和JRockit只要没有触碰到进程可用内存 的上限，例如32位系统中的4GB限制，就不会出问题），而且有极少数 方法（例如String::intern()）会因永久代的原因而导致不同虚拟机下有不同的表现。当Oracle收购BEA获得了JRockit的所有权后，就借鉴了其方法区的实现方式。**在JDK 6的时候HotSpot开发团队就有放弃永久代，逐步改为采用本地内存（Native Memory）来实现方法区的计划了**，**到了JDK 7的HotSpot，已经把原本放在永久代的运行时常量池（包括字符串常量池）、静态变量等移出（移到了堆中），而到了JDK 8，终于完全废弃了永久代的概念，改用与 JRockit、J9一样在本地内存中实现的元空间（Meta-space）来代替，把 JDK 7中永久代还剩余的内容（主要是类型信息）全部移到元空间中。**

- **方法区（线程共享）**：类的所有字段和方法字节码，以及一些特殊方法如构造方法，接口代码也在此定义。也就是静态变量+常量+类信息（构造方法/接口定义）+运行时常量池都存在该方法区中。

- **永久代**：按照计划从JDK 7开始将会逐渐消除永久代，即以元空间取代永久代的故事永！所以在JDK 7中永久代还是存在，从JDK 8开始就被元空间给取代了！**永久代逻辑结构上属于堆，但是物理上不属于堆，会出现OOM异常**。

- **元空间**：元空间也称元数据区，是用来取代永久代的，本质和永久代类似，逻辑结构上属于堆，区别在于元数据区并不在虚拟机中，而是使用JVM内存之外的本地物理内存，永久代在虚拟机中，元数据区也有可能发生OutOfMemory异常。

- **概括总结**：

  **永久代本质上不等同于方法区，只是大家习惯上将两者混为一谈，JDK 7之前HotSpot虚拟机使用永久代来实现方法区，从JDK 7开始就有废除永久代改用元空间来代替，在JDK 7中将原本放在永久代中的运行时常量池（包括字符串常量池）、静态变量移到了堆中，但此时永久代还是存在的。到了JDK 8中就完全废弃了永久代，改用本地内存(堆外内存)实现的元空间来代替，将剩余的内容(主要是类信息)都移到了元空间中。**



#### JVM常量池分类

谈到常量池，在Java体系中，共用三种常量池。分别是**字符串常量池**、**Class常量池**和**运行时常量池**。



#### Class常量池

Class常量池可以理解为是Class文件中的资源仓库。 Class文件中除了包含类的版本、字段、方法、接口等描述信息外，还有一项信息就是常量池(constant pool table)，用于存放编译器生成的各种字面量(Literal)和符号引用(Symbolic References)。

由于不同的Class文件中包含的常量的个数是不固定的，所以在Class文件的常量池入口处会设置两个字节的常量池容量计数器，记录了常量池中常量的个数。

一种比较简单的查看Class文件中常量池的方法，那就是通过`javap`命令。对于以上的`HelloWorld.class`，
可以通过`javap -v  HelloWorld.class`命令查看字节码中的常量池：

![img](http://www.hollischuang.com/wp-content/uploads/2018/10/15401195127619.jpg)

> 从上图中可以看到，反编译后的class文件常量池中共有16个常量。而Class文件中常量计数器的数值是0011，将该16进制数字转换成10进制的结果是17。
>
> 原因是与Java的语言习惯不同，常量池计数器是从0开始而不是从1开始的，常量池的个数是10进制的17，这就代表了其中有16个常量，索引值范围为1-16。

**常量池有什么用?**

介绍完了什么是Class常量池以及如何查看常量池，那么接下来我们就要深入分析一下，Class常量池中都有哪些内容。

**Class常量池中主要存放两大类常量：字面量（literal）和符号引用（symbolic references）。**

- **字面量**

  前面说过，运行时常量池中主要保存的是字面量和符号引用，那么到底什么字面量？

  > 在计算机科学中，字面量（literal）是用于表达源代码中一个固定值的表示法（notation）。几乎所有计算机编程语言都具有对基本值的字面量表示，诸如：整数、浮点数以及字符串；而有很多也对布尔类型和字符类型的值也支持字面量表示；还有一些甚至对枚举类型的元素以及像数组、记录和对象等复合类型的值也支持字面量表示法。

  以上是关于计算机科学中关于字面量的解释，并不是很容易理解。说简单点，字面量就是指由字母、数字等构成的字符串或者数值。

  字面量只可以右值出现，所谓右值是指等号右边的值，如：int a=123这里的a为左值，123为右值。在这个例子中123就是字面量。

  ```java
  int a = 123;
  String s = "hollis";
  ```
  上面的代码事例中，123和hollis都是字面量。
  
  本文开头的上面javap查看HelloWorld.class字节码常量池的图中，Hollis就是一个字面量。
  
- **符号引用**

  常量池中，除了字面量以外，还有符号引用，那么到底什么是符号引用呢。

  符号引用是编译原理中的概念，是相对于直接引用来说的。主要包括了以下三类常量： **类和接口的全限定名、字段的名称和描述符、方法的名称和描述符**

  这也就可以印证前面的常量池中还包含一些`com/hollis/HelloWorld`、`main`、`([Ljava/lang/String;)V`等常量的原因了。

前面介绍了这么多，关于Class常量池是什么，怎么查看Class常量池以及Class常量池中保存了哪些东西。有一个关键的问题没有讲，那就是Class常量池到底有什么用。

Class常量池是Class文件中的资源仓库，其中保存了各种常量。而这些常量都是开发者定义出来，需要在程序的运行期使用的。

在《深入理解Java虚拟》中有这样的表述：

Java代码在进行`Javac`编译的时候，并不像C和C++那样有“连接”这一步骤，而是在虚拟机加载Class文件的时候进行动态连接。也就是说，在Class文件中不会保存各个方法、字段的最终内存布局信息，因此这些字段、方法的符号引用不经过运行期转换的话无法得到真正的内存入口地址，也就无法直接被虚拟机使用。当虚拟机运行时，需要从常量池获得对应的符号引用，再在类创建时或运行时解析、翻译到具体的内存地址之中。关于类的创建和动态连接的内容，在虚拟机类加载过程时再进行详细讲解。

前面这段话，看起来很绕，不是很容易理解。其实他的意思就是： Class是用来保存常量的一个媒介场所，并且是一个中间场所。在JVM真的运行时，需要把常量池中的常量加载到内存中。



#### 运行时常量池

运行时常量池（ Runtime Constant Pool）是每一个类或接口的常量池（ Constant_Pool）的运行时表示形式。

它包括了若干种不同的常量：从编译期可知的数值字面量到必须运行期解析后才能获得的方法或字段引用。运行时常量池扮演了类似传统语言中符号表（ SymbolTable）的角色，不过它存储数据范围比通常意义上的符号表要更为广泛。

每一个运行时常量池都分配在 Java 虚拟机的方法区之中，在类和接口被加载到虚拟机后，对应的运行时常量池就被创建出来。

以上，是Java虚拟机规范中关于运行时常量池的定义。

**运行时常量池在各版本JDK中的实现**

根据Java虚拟机规范约定：每一个运行时常量池都在Java虚拟机的方法区中分配，在加载类和接口到虚拟机后，就创建对应的运行时常量池。

在不同版本的JDK中，运行时常量池所处的位置也不一样。以HotSpot为例：

在JDK 1.7之前，方法区位于堆内存的永久代中，运行时常量池作为方法区的一部分，也处于永久代中。

因为使用永久代实现方法区可能导致内存泄露问题，所以，从JDK1.7开始，JVM尝试解决这一问题，在1.7中，将原本位于永久代中的运行时常量池移动到堆内存中。（永久代在JDK 1.7并没有完全移除，只是原来方法区中的运行时常量池、类的静态变量等移动到了堆内存中。）

从JDK 1.8开始，彻底移除了永久代，方法区通过元空间的方式实现。

**运行时常量池中常量的来源**

运行时常量池中包含了若干种不同的常量：

编译期可知的字面量和符号引用（来自Class常量池） 运行期解析后可获得的常量（如String的intern方法）

所以，**运行时常量池中的内容包含：Class常量池中的常量、字符串常量池中的内容**



#### 字符串常量池

字符串大家一定都不陌生，他是我们非常常用的一个类。

String作为一个Java类，可以通过以下两种方式创建一个字符串：

```java
String str = "Hollis";

String str = new String("Hollis")；
```

而第一种是我们比较常用的做法，这种形式叫做"字面量"。

在JVM中，为了减少相同的字符串的重复创建，为了达到节省内存的目的。会单独开辟一块内存，用于保存字符串常量，这个内存区域被叫做字符串常量池。

当代码中出现双引号形式（字面量）创建字符串对象时，JVM 会先对这个字符串进行检查，如果字符串常量池中存在相同内容的字符串对象的引用，则将这个引用返回；否则，创建新的字符串对象，然后将这个引用放入字符串常量池，并返回该引用。

这种机制，就是字符串驻留或池化。

**字符串常量池的位置**

- 在JDK 7以前的版本中，字符串常量池是放在永久代中的。

- 从JDK 7开始，字符串常量池被移到了堆中。



#### String.intern()

在JVM中，为了减少相同的字符串的重复创建，为了达到节省内存的目的。会单独开辟一块内存，用于保存字符串常量，这个内存区域被叫做字符串常量池。

当代码中出现双引号形式（字面量）创建字符串对象时，JVM 会先对这个字符串进行检查，如果字符串常量池中存在相同内容的字符串对象的引用，则将这个引用返回；否则，创建新的字符串对象，然后将这个引用放入字符串常量池，并返回该引用。

除了以上方式之外，还有一种可以在运行期将字符串内容放置到字符串常量池的办法，那就是使用String.intern()方法。

intern的功能很简单：在每次赋值的时候使用 String 的 intern 方法，如果常量池中有相同值，就会重复使用该对象，返回对象引用。

**String.intern()方法在不同版本的JDK中的差异**

- **JDK 6及以前，intern()方法的注释**：当字符串在常量池存在时，则返回常量池中的字符串；当字符串在常量池不存在时，则拷贝一份到常量池中，然后再返回常量池中的字符串。
- **JDK 7及以后，intern()方法的注释**：当字符串在常量池存在时，则返回常量池中的字符串；当字符串在常量池不存在时，则***把堆内存中此对象引用添加到常量池中***，然后再返回此引用。
- **在JDK 6及以前，intern()方法会把首次遇到的字符串实例复制到永久代的字符串常量池中存储，返回的也是永久代里面这个字符串实例的引用**。
- **而JDK 7及以后（以及部分其他虚拟机，例如JRockit）的intern()方法实现就不需要再拷贝字符串的实例到永久代了，既然字符串常量池已经移到Java堆中，那只需要在常量池里记录一下首次出现的实例引用即可。**



#### 字符串池面试题

- 字符串常量池产生时机须知：（以下简称"须知"）

  *(如果对下面所述不胜了解的话，建议先看下上面章节中的"Class常量池"，"运行时常量池"等)*

  1. **JVM编译期遇到的字面量(即代码中所有出现的由双引号扩着的字符串)，一旦javac遇到这个会将其保存在Class常量池中，JVM将会在运行期类初始化阶段就将其加入到字符串常量池中。这个是个自动加入的过程**

     ```java
     //此处共产生三个字面量的字符串("ab", "c", "d")，它们在编译期被存储在Class常量池中，
     //在运行期运行这段代码之前就已经被加入到字符串常量池中了
     String str1 = new StringBuilder("ab").append("c").append("d");
     ```

  2. JVM运行期产生的字符串是**不会自动加入**到字符串常量池中的，此处所指的字符串指的是类似下面代码中`str1`：

     ```java
     //此处共产生三个字面量的字符串("ab", "c", "d")，它们在编译期被标记，在运行这段代码之前就已经被加入到字符串常量池中了
     //此处的str1是运行期才能确定的，运行期产生的字符串是不会自动加入到字符串常量池中的，除非调用String.intern()方法!
     String str1 = new StringBuilder("ab").append("c").append("d");
     ```

  3. JVM运行期产生的字符串通过调用其intern()方法被动加入字符串常量池，此时又得按不同版本的JDK又有行为区别：

     - 在JDK 6及以前，intern()方法会把首次遇到的字符串实例复制到永久代的字符串常量池中存储，返回的也是永久代里面这个字符串实例的引用。

     - 在JDK 7及以后，intern()方法实现就不需要再拷贝字符串的实例到永久代了，既然字符串常量池已经移到Java堆中，那只需要在常量池里记录一下首次出现的实例引用即可。
     
      举个例子：
     
     ```java
         /**
          * String.intern()方法在不同版本JDK中的差异：
          *
          * - 在JDK 6中，intern()方法会把首次遇到的字符串实例复制到永久代的字符串常量池中存储，
          *   返回的也是永久代里面这个字符串实例的引用。
          * - 而JDK 7（以及部分其他虚拟机，例如JRockit）的intern()方法实现就不需要再拷贝字符串的实例到永久代了，
          *   既然字符串常量池已经移到Java堆中，
          *   那只需要在常量池里记录一下首次出现的实例引用即可。
          */
         public static void internDifferenceAtJdks() {
             /**
              * 分析此句代码可知，str1是运行期产生的字符串，
              * 而其中的字面量"计算机"、"软件"却是编译期就可确定其在字符串常量池中
              */
             String str1 = new StringBuilder("计算机").append("软件").toString();
     
             //JDK 6中是false，JDK 7+是true
             System.out.println(str1.intern() == str1);
     
             //编译期优化，等价于：String str1 = new String("java");
             String str2 = new StringBuilder("ja").append("va").toString();
     
             /**
              * 所有版本都输出false，因为"java"这个字符串是个特殊的字符串，
              * 在JVM启动时由sun.misc.Version这个类放入常量池的，已经不是首次出现了，
              * 所以肯定为false
              */
             System.out.println(str2.intern() == str2);
         }
     ```
  
  
  
- 创建字符串对象的方式之一：采用字面量的方式赋值

    当代码中出现双引号形式（字面量）创建字符串对象时，JVM 会先对这个字符串进行检查，如果字符串常量池中存在相同内容的字符串对象的引用，则将这个引用返回；否则，创建新的字符串对象，然后将这个引用放入字符串常量池，并返回该引用。

    ```java
    /**
     * 创建字符串对象的方式之一：采用字面量的方式赋值
     *
     * 当代码中出现双引号形式（字面量）创建字符串对象时，JVM 会先对这个字符串进行检查，
     * 如果字符串常量池中存在相同内容的字符串对象的引用，则将这个引用返回；否则，创建新的字符串对象，
     * 然后将这个引用放入字符串常量池，并返回该引用。
     */
    public static void literalString() {
        String str1 = "abc";
        String str2 = "abc";
        System.out.println(str1 == str2); //true
    }
    ```

- 创建字符串对象的方式之二：采用new关键字新建一个字符串对象

    ```java
    String str = new String("abc");
    ```
    根据上面"须知"，字面量"abc"在初始化阶段就驻存在字符串常量池中了，由String的构造器String(String original)可知，original正是池中的字符串对象引用，同时我们也知道new出来的对象肯定是个独一无二的，在这里进入到构造器可知：str与original共享着底层的字符数组value(它在池中，一开始独属于original)。因此str与池中的对象根本就是两个不同的引用。

- **理解"字符串常量池产生时机须知"、"创建字符串对象的方式之一"、"创建字符串对象的方式之二"是解字符串池及intern()方面面试的前提。**

    

- 面试训练

    ```java
        /**
         * 创建字符串对象的方式之一：采用字面量的方式赋值
         *
         * 当代码中出现双引号形式（字面量）创建字符串对象时，JVM 会先对这个字符串进行检查，
         * 如果字符串常量池中存在相同内容的字符串对象的引用，则将这个引用返回；否则，创建新的字符串对象，
         * 然后将这个引用放入字符串常量池，并返回该引用。
         */
        public static void literalString() {
            String str1 = "abc"; //存在字符串字面量，编译期即可加入字符串常量池
            String str2 = "abc"; //字符串常量池中存在该字面量，直接返回其引用
            System.out.println(str1 == str2); //true
    }
    
        /**
         * 创建字符串对象的方式之二：采用new关键字新建一个字符串对象
         * 
         * 调换str1与str2的什么顺序，也是同样的结果
         */
        public static void newString() {
            //产生两个对象，池中的original，堆中str1
            String str1 = new String("abc");
            String str2 = "abc"; //池中已存在，直接返回引用
            System.out.println(str1 == str2); //false
            System.out.println(str1.intern() == str2); //true
        }
    
        /**
         * 编译时优化
         */
        public static void compileOptimize() {
            String str1 = "a" + "b" + "c"; //声明时，产生编译优化，等价于：String str1 = "abc";
            String str2 = "abc"; //发现池中已经存在"abc"字符串对象，则直接返回其引用给str2
            System.out.println(str1 == str2); //true
        }
    
        /**
         * 这种情况不存在编译期优化
         */
        public static void nocompileOptimize() {
            String str1 = "ab";
            //声明时，产生编译优化，直接在字符串池中创建"abc"字符串对象并将其引用指向str1
            String str2 = "abc";
    
            /**
             * 因为表达式中str1是变量，不会进行编译优化，
             * 其等价于new StringBuilder().append(str1).append("c").toString()，
             * 而toString()方法实际就是new String(...)
             */
            String str3 = str1 + "c";
            System.out.println(str2 == str3); //false
        }
    
        /**
         * JVM编译期遇到的字面量(即代码中所有出现的由双引号扩着的字符串)，
         * 一旦遇到这个，JVM将会在类初始化阶段就将其加入到字符串常量池中。
         * 这个是个自动加入的过程
         */
        public static void literalString1() {
            //发现字面量"abc"并自动加入字符串常量池中，且str1最终是new String(...)得出来的
            //此句等价于String str1 = new String("abc");
            String str1 = new StringBuilder("abc").toString();
            //str1.intern()返回的就是字符串池中的那个对象，str1本身是new出来的，所以两者不相同
            System.out.println(str1.intern() == str1); //false
    
            //区别上面：字符串常量池中存在"hello"和"world"，就是没有与str2字符序列相同的"helloworld"，
            String str2 = new StringBuilder("hello").append("world").toString();
            System.out.println(str2.intern() == str2); //true(JDK 7+),false(JDK 6-)
        }
    
        /**
         * 本例基于JDK 7+
         * 注意与complexExample2对比语句String intern3 = s3.intern()的位置
         */
        public static void complexExample1() {
            //等价于s3 = new StringBuilder().append("hello").append("hello");
            String s3 = new String("hello") + new String("hello");
            String intern3 = s3.intern(); //intern3是字符串常量池中的引用，也即是堆中s3的引用
            String s4 = "hellohello"; //等同再次调了s3.intern()
            System.out.println("s3 == s4 : " + (s3 == s4)); //true
            System.out.println("intern3 == s3 : " + (intern3 == s3)); //true
            System.out.println("intern3 == s4 : " + (intern3 == s4)); //true
        }
    
        /**
         * 本例基于JDK 7+
         * 注意与complexExample1对比语句String intern3 = s3.intern()的位置
         */
        public static void complexExample2() {
            //等价于s3 = new StringBuilder().append("hello").append("hello");
            String s3 = new String("hello") + new String("hello");
            //此时字符串常量池中只有hello
            String s4 = "hellohello"; //字符串常量池中没有hellohello，没有则创建，并将其引用返回给s4
            //与上面示例相比,注意s3.intern()在代码中行位置的变化
            String intern3 = s3.intern(); //返回的就是s4的引用
            System.out.println("s3 == s4 : " + (s3 == s4)); //false
            System.out.println("intern3 == s3 : " + (intern3 == s3)); //false
            System.out.println("intern3 == s4 : " + (intern3 == s4)); //true
        }
    ```



### AbstractStringBuilder

该类是可变字符操作的抽象父类，其子类仅有StringBuffer和StringBuilder两个

```java
abstract class AbstractStringBuilder implements Appendable, CharSequence {

    /**
     * 字符序列存储字段
     */
    char[] value;

    /**
     * 当前实际字符序列的长度，而非value.length
     */
    int count;
    
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    
    /**
     * 核心扩容算法
     * @param minimumCapacity - 期望扩容后的value最小容量
     */
    public void ensureCapacity(int minimumCapacity) {
        if (minimumCapacity > 0)
            ensureCapacityInternal(minimumCapacity);
    }
    
    private void ensureCapacityInternal(int minimumCapacity) {
        // overflow-conscious code
        if (minimumCapacity - value.length > 0) {
            //Arrays.copyOf(...)中返回了新的char[]并赋给value
            value = Arrays.copyOf(value,
                    newCapacity(minimumCapacity));
        }
    }
    
    /**
     * 正常扩容是原容量的2倍 + 2
     */
    private int newCapacity(int minCapacity) {
        // overflow-conscious code
        int newCapacity = (value.length << 1) + 2; //正常扩容是原容量的2倍 + 2
        if (newCapacity - minCapacity < 0) { //如果上面正常扩容仍未达到预期的话则直接使用预期的minCapacity
            newCapacity = minCapacity;
        }
        //
        return (newCapacity <= 0 || MAX_ARRAY_SIZE - newCapacity < 0) //考虑上面右移溢出情况
            ? hugeCapacity(minCapacity)
            : newCapacity;
    }

    private int hugeCapacity(int minCapacity) {
        if (Integer.MAX_VALUE - minCapacity < 0) { // overflow
            throw new OutOfMemoryError();
        }
        return (minCapacity > MAX_ARRAY_SIZE) // minCapacity在[Integer.MAX_VALUE - 8, Integer.MAX_VALUE]之间?
            ? minCapacity : MAX_ARRAY_SIZE;
    }
    
    /**
     * 扩充或缩减当前字符序列的方法
     *
     * 当为缩减字符序列时，旧的字符序列char[] value其实未被清空，只是修改了count值
     */
    public void setLength(int newLength) {
        if (newLength < 0)
            throw new StringIndexOutOfBoundsException(newLength);
        ensureCapacityInternal(newLength); //先扩容(如果需要的话)，不会减容的

        if (count < newLength) { //主动扩容?扩充部分用\u0000字符填充
            Arrays.fill(value, count, newLength, '\0');
        }

        count = newLength;
    }
}
```

**源码阅读要点：**

- 扩容算法：在至少保证最小期望容量的基础上，进行**原容量 * 2 + 2**的扩容机制，**当实际字符数count大于value.length时才触发实际扩容**。容量最大可达Integer.MAX_VALUE - 8
- 很显然，所有append，insert，replace操作都会触发扩容机制。
- 调用setLength(0)来进行清空操作，这个清空是逻辑上的，实际上char[] value里旧的字符序列还在。所以清空最好还得用delete(start, end)来做。



### StringBuffer

StringBuffer是AbstractStringBuilder的子类，是线程同步的。

```java
 public final class StringBuffer
    extends AbstractStringBuilder
    implements java.io.Serializable, CharSequence
{
     /**
      * 对toString()方法做了缓存，当调用修改操作的方法时将会被重置为null
      */
     private transient char[] toStringCache;
    
     public StringBuffer() {
        super(16); //默认容量16
     }
     
     public StringBuffer(String str) {
        super(str.length() + 16);
        append(str);
     }
     
     //扩容算法完全继承自父类
     @Override
     public synchronized void ensureCapacity(int minimumCapacity) {
        super.ensureCapacity(minimumCapacity);
     }
     
     //重写的toString()方法
     @Override
     public synchronized String toString() {
        if (toStringCache == null) { //缓存底层的字符序列
            toStringCache = Arrays.copyOfRange(value, 0, count);
        }
        return new String(toStringCache, true);
     }

}
```



### StringBuilder

StringBuffer是AbstractStringBuilder的子类，是非线程同步的。

```java
public final class StringBuilder
    extends AbstractStringBuilder
    implements java.io.Serializable, CharSequence
{

    public StringBuilder() {
        super(16);
    }
    
    public StringBuilder(int capacity) {
        super(capacity);
    }
    
    public StringBuilder(String str) {
        super(str.length() + 16);
        append(str);
    }
    
    @Override
    public String toString() {
        // Create a copy, don't share the array
        return new String(value, 0, count);
    }

}
```

StringBuilder和StringBuffer差不多，方法基本上都继承自AbstractStringBuilder父类，StringBuilder没有做toString缓存。



### Java中各种关键字

#### transient

在关于java的集合类的学习中，我们发现`ArrayList`类和`Vector`类都是使用数组实现的，但是在定义数组`elementData`这个属性时稍有不同，那就是`ArrayList`使用`transient`关键字

```java
private transient Object[] elementData;  

protected Object[] elementData;  
```

> Java语言的关键字，变量修饰符，如果用transient声明一个实例变量，当对象存储时，它的值不需要维持。这里的对象存储是指，Java的serialization提供的一种持久化对象实例的机制。当一个对象被序列化的时候，transient型变量的值不包括在序列化的表示中，然而非transient型的变量是被包括进去的。使用情况是：当持久化对象时，可能有一个特殊的对象数据成员，我们不想用serialization机制来保存它。为了在一个特定对象的一个域上关闭serialization，可以在这个域前加上关键字transient。

**简单点说，就是被transient修饰的成员变量，在序列化的时候其值会被忽略，在被反序列化后， transient 变量的值被设为初始值， 如 int 型的是 0，对象型的是 null。**



#### instanceof

instanceof 是 Java 的一个二元操作符，类似于 ==，>，< 等操作符。

instanceof 是 Java 的保留关键字。它的作用是测试它左边的对象是否是它右边的类的实例，返回 boolean 的数据类型。

以下实例创建了 displayObjectClass() 方法来演示 Java instanceof 关键字用法：

```java
public static void displayObjectClass(Object o) {
  if (o instanceof Vector)
     System.out.println("对象是 java.util.Vector 类的实例");
  else if (o instanceof ArrayList)
     System.out.println("对象是 java.util.ArrayList 类的实例");
  else
    System.out.println("对象是 " + o.getClass() + " 类的实例");
```



#### volatile

在[再有人问你Java内存模型是什么，就把这篇文章发给他](http://47.103.216.138/archives/2550)中我们曾经介绍过，Java语言为了解决并发编程中存在的原子性、可见性和有序性问题，提供了一系列和并发处理相关的关键字，比如`synchronized`、`volatile`、`final`、`concurren包`等。在[前一篇](http://47.103.216.138/archives/2637)文章中，我们也介绍了`synchronized`的用法及原理。本文，来分析一下另外一个关键字——`volatile`。

本文就围绕`volatile`展开，主要介绍`volatile`的用法、`volatile`的原理，以及`volatile`是如何提供可见性和有序性保障的等。

`volatile`这个关键字，不仅仅在Java语言中有，在很多语言中都有的，而且其用法和语义也都是不尽相同的。尤其在C语言、C++以及Java中，都有`volatile`关键字。都可以用来声明变量或者对象。下面简单来介绍一下Java语言中的`volatile`关键字。

- **volatile用法**

  `volatile`通常被比喻成"轻量级的`synchronized`"，也是Java并发编程中比较重要的一个关键字。和`synchronized`不同，`volatile`是一个变量修饰符，只能用来修饰变量。无法修饰方法及代码块等。

  `volatile`的用法比较简单，只需要在声明一个可能被多线程同时访问的变量时，使用`volatile`修饰就可以了。

- **volatile原理**

  在[再有人问你Java内存模型是什么，就把这篇文章发给他](http://47.103.216.138/archives/2550)中我们曾经介绍过，为了提高处理器的执行速度，在处理器和内存之间增加了多级缓存来提升。但是由于引入了多级缓存，就存在缓存数据不一致问题。

  但是，对于`volatile`变量，当对`volatile`变量进行写操作的时候，JVM会向处理器发送一条lock前缀的指令，将这个缓存中的变量回写到系统主存中。

  但是就算写回到内存，如果其他处理器缓存的值还是旧的，再执行计算操作就会有问题，所以在多处理器下，为了保证各个处理器的缓存是一致的，就会实现`缓存一致性协议`

  **缓存一致性协议**：每个处理器通过嗅探在总线上传播的数据来检查自己缓存的值是不是过期了，当处理器发现自己缓存行对应的内存地址被修改，就会将当前处理器的缓存行设置成无效状态，当处理器要对这个数据进行修改操作的时候，会强制重新从系统内存里把数据读到处理器缓存里。这类协议主流的如：**MESI**

  **所以，如果一个变量被`volatile`所修饰的话，在每次数据变化之后，其值都会被强制刷入主存。而其他处理器的缓存由于遵守了缓存一致性协议（MESI），也会把这个变量的值从主存加载到自己的缓存中。这就保证了一个`volatile`在并发编程中，其值在多个缓存中是可见的。**

- **volatile与可见性**

  可见性是指当多个线程访问同一个变量时，一个线程修改了这个变量的值，其他线程能够立即看得到修改的值。

  我们在[再有人问你Java内存模型是什么，就把这篇文章发给他](http://47.103.216.138/archives/2550)中分析过：Java内存模型规定了所有的变量都存储在主内存中，每条线程还有自己的工作内存，线程的工作内存中保存了该线程中是用到的变量的主内存副本拷贝，线程对变量的所有操作都必须在工作内存中进行，而不能直接读写主内存。不同的线程之间也无法直接访问对方工作内存中的变量，线程间变量的传递均需要自己的工作内存和主存之间进行数据同步进行。所以，就可能出现线程1改了某个变量的值，但是线程2不可见的情况。

  前面的关于`volatile`的原理中介绍过了，Java中的`volatile`关键字提供了一个功能，那就是被其修饰的变量在被修改后可以立即同步到主内存，被其修饰的变量在每次是用之前都从主内存刷新。因此，可以使用`volatile`来保证多线程操作时变量的可见性。

- **volatile与有序性**

  有序性即程序执行的顺序按照代码的先后顺序执行。

  我们在[再有人问你Java内存模型是什么，就把这篇文章发给他](http://47.103.216.138/archives/2550)中分析过：除了引入了时间片以外，由于处理器优化和指令重排等，CPU还可能对输入代码进行乱序执行，比如`load->add->save` 有可能被优化成`load->save->add` 。这就是可能存在有序性问题。

  而`volatile`除了可以保证数据的可见性之外，还有一个强大的功能，那就是他可以禁止指令重排优化等。

  普通的变量仅仅会保证在该方法的执行过程中所依赖的赋值结果的地方都能获得正确的结果，而不能保证变量的赋值操作的顺序与程序代码中的执行顺序一致。

  **volatile可以禁止指令重排，这就保证了代码的程序会严格按照代码的先后顺序执行。**这就保证了有序性。被`volatile`修饰的变量的操作，会严格按照代码顺序执行，`load->add->save` 的执行顺序就是：load、add、save。

- **volatile与原子性**

  原子性是指一个操作是不可中断的，要全部执行完成，要不就都不执行。

  我们在[Java的并发编程中的多线程问题到底是怎么回事儿？](http://47.103.216.138/archives/2618)中分析过：线程是CPU调度的基本单位。CPU有时间片的概念，会根据不同的调度算法进行线程调度。当一个线程获得时间片之后开始执行，在时间片耗尽之后，就会失去CPU使用权。所以在多线程场景下，由于时间片在线程间轮换，就会发生原子性问题。

  在上一篇文章中，我们介绍`synchronized`的时候，提到过，为了保证原子性，需要通过字节码指令`monitorenter`和`monitorexit`，但是`volatile`和这两个指令之间是没有任何关系的。

  **所以，`volatile`是不能保证原子性的。**

  在以下两个场景中可以使用`volatile`来代替`synchronized`：

  > **1、运算结果并不依赖变量的当前值，或者能够确保只有单一的线程会修改变量的值。**
  >
  > **2、变量不需要与其他状态变量共同参与不变约束。**

- **总结与思考**

  我们介绍过了`volatile`关键字和`synchronized`关键字。现在我们知道，**`synchronized`可以保证原子性、有序性和可见性。而`volatile`却只能保证有序性和可见性。**

  

#### synchronized

在[再有人问你Java内存模型是什么，就把这篇文章发给他。](http://www.hollischuang.com/archives/2550)中我们曾经介绍过，Java语言为了解决并发编程中存在的原子性、可见性和有序性问题，提供了一系列和并发处理相关的关键字，比如`synchronized`、`volatile`、`final`、`concurren包`等。

在《深入理解Java虚拟机》中，有这样一段话：

> `synchronized`关键字在需要原子性、可见性和有序性这三种特性的时候都可以作为其中一种解决方案，看起来是“万能”的。的确，大部分并发控制操作都能使用synchronized来完成。

- **synchronized的用法**

  `synchronized`是Java提供的一个并发控制的关键字。主要有两种用法，分别是同步方法和同步代码块。也就是说，`synchronized`既可以修饰方法也可以修饰代码块。

  ```java
  /**
   * @author Hollis 18/08/04.
   */
  public class SynchronizedDemo {
       //同步方法
      public synchronized void doSth(){
          System.out.println("Hello World");
      }
  
      //同步代码块
      public void doSth1(){
          synchronized (SynchronizedDemo.class){
              System.out.println("Hello World");
          }
      }
  }
  ```
  被`synchronized`修饰的代码块及方法，在同一时间，只能被单个线程访问。
  
- **synchronized的实现原理**

  `synchronized`，是Java中用于解决并发情况下数据同步访问的一个很重要的关键字。当我们想要保证一个共享资源在同一时间只会被一个线程访问到时，我们可以在代码中使用`synchronized`关键字对类或者对象加锁。

  对于同步方法，JVM采用`ACC_SYNCHRONIZED`标记符来实现同步。 对于同步代码块。JVM采用`monitorenter`、`monitorexit`两个指令来实现同步。

  在[The Java® Virtual Machine Specification](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-2.html#jvms-2.11.10)中有关于同步方法和同步代码块的实现原理的介绍，我翻译成中文如下：

  > **方法级的同步是隐式的。同步方法的常量池中会有一个`ACC_SYNCHRONIZED`标志。当某个线程要访问某个方法的时候，会检查是否有`ACC_SYNCHRONIZED`，如果有设置，则需要先获得监视器锁，然后开始执行方法，方法执行之后再释放监视器锁。这时如果其他线程来请求执行方法，会因为无法获得监视器锁而被阻断住。值得注意的是，如果在方法执行过程中，发生了异常，并且方法内部并没有处理该异常，那么在异常被抛到方法外面之前监视器锁会被自动释放。**
  >
  > **同步代码块使用`monitorenter`和`monitorexit`两个指令实现。可以把执行`monitorenter`指令理解为加锁，执行`monitorexit`理解为释放锁。 每个对象维护着一个记录着被锁次数的计数器。未被锁定的对象的该计数器为0，当一个线程获得锁（执行`monitorenter`）后，该计数器自增变为 1 ，当同一个线程再次获得该对象的锁的时候，计数器再次自增。当同一个线程释放锁（执行`monitorexit`指令）的时候，计数器再自减。当计数器为0的时候。锁将被释放，其他线程便可以获得锁。**

  **无论是`ACC_SYNCHRONIZED`还是`monitorenter`、`monitorexit`都是基于对象头Mark Word状态处于重量级锁情况下指针指向的一个叫monitor监视器的对象实现的，在Java虚拟机(HotSpot)中，Monitor是基于C++实现的，由ObjectMonitor实现。而ObjectMonitor简直就是AQS的翻版。**
  
  ObjectMonitor类中提供了几个方法，如`enter`、`exit`、`wait`、`notify`、`notifyAll`等。`sychronized`加锁的时候，会调用objectMonitor的enter方法，解锁的时候会调用exit方法。（关于Monitor详见[深入理解多线程（四）—— Moniter的实现原理](http://www.hollischuang.com/archives/2030)）
  
- **synchronized与原子性**

  原子性是指一个操作是不可中断的，要全部执行完成，要不就都不执行。

  我们在[Java的并发编程中的多线程问题到底是怎么回事儿？](http://www.hollischuang.com/archives/2618)中分析过：线程是CPU调度的基本单位。CPU有时间片的概念，会根据不同的调度算法进行线程调度。当一个线程获得时间片之后开始执行，在时间片耗尽之后，就会失去CPU使用权。所以在多线程场景下，由于时间片在线程间轮换，就会发生原子性问题。

  在Java中，为了保证原子性，提供了两个高级的字节码指令`monitorenter`和`monitorexit`。前面中，介绍过，这两个字节码指令，在Java中对应的关键字就是`synchronized`。

  通过`monitorenter`和`monitorexit`指令，可以保证被`synchronized`修饰的代码在同一时间只能被一个线程访问，在锁未释放之前，无法被其他线程访问到。因此，在Java中可以使用`synchronized`来保证方法和代码块内的操作是原子性的。

- **synchronized与可见性**

  可见性是指当多个线程访问同一个变量时，一个线程修改了这个变量的值，其他线程能够立即看得到修改的值。

  我们在[再有人问你Java内存模型是什么，就把这篇文章发给他。](http://www.hollischuang.com/archives/2550)中分析过：Java内存模型规定了所有的变量都存储在主内存中，每条线程还有自己的工作内存，线程的工作内存中保存了该线程中是用到的变量的主内存副本拷贝，线程对变量的所有操作都必须在工作内存中进行，而不能直接读写主内存。不同的线程之间也无法直接访问对方工作内存中的变量，线程间变量的传递均需要自己的工作内存和主存之间进行数据同步进行。所以，就可能出现线程1改了某个变量的值，但是线程2不可见的情况。

  **前面我们介绍过，被`synchronized`修饰的代码，在开始执行时会加锁，执行完成后会进行解锁。而为了保证可见性，有一条规则是这样的：对一个变量解锁之前，必须先把此变量同步回主存中。这样解锁后，后续线程就可以访问到被修改后的值。**

  所以，synchronized关键字锁住的对象，其值是具有可见性的。

- **synchronized与有序性**

  有序性即程序执行的顺序按照代码的先后顺序执行。

  我们在[再有人问你Java内存模型是什么，就把这篇文章发给他。](http://www.hollischuang.com/archives/2550)中分析过：除了引入了时间片以外，由于处理器优化和指令重排等，CPU还可能对输入代码进行乱序执行，比如load->add->save 有可能被优化成load->save->add 。这就是可能存在有序性问题。

  这里需要注意的是，**`synchronized`是无法禁止指令重排和处理器优化的**。也就是说，`synchronized`无法避免上述提到的问题。

  那么，为什么还说`synchronized`也提供了有序性保证呢？

  这就要再把有序性的概念扩展一下了。Java程序中天然的有序性可以总结为一句话：如果在本线程内观察，所有操作都是天然有序的。如果在一个线程中观察另一个线程，所有操作都是无序的。

  以上这句话也是《深入理解Java虚拟机》中的原句，但是怎么理解呢？周志明并没有详细的解释。这里我简单扩展一下，这其实和`as-if-serial语义`有关。

  `as-if-serial`语义的意思指：不管怎么重排序（编译器和处理器为了提高并行度），单线程程序的执行结果都不能被改变。编译器和处理器无论如何优化，都必须遵守`as-if-serial`语义。

  这里不对`as-if-serial语义`详细展开了，简单说就是，`as-if-serial语义`保证了单线程中，指令重排是有一定的限制的，而只要编译器和处理器都遵守了这个语义，那么就可以认为单线程程序是按照顺序执行的。当然，实际上还是有重排的，只不过我们无须关心这种重排的干扰。

  **所以呢，由于`synchronized`修饰的代码，同一时间只能被同一线程访问。那么也就是单线程执行的。所以，可以保证其有序性。**

- **synchronized与锁优化**

  前面介绍了`synchronized`的用法、原理以及对并发编程的作用。是一个很好用的关键字。

  `synchronized`其实是借助Monitor实现的，在加锁时会调用objectMonitor的`enter`方法，解锁的时候会调用`exit`方法。事实上，只有在JDK1.6之前，synchronized的实现才会直接调用ObjectMonitor的`enter`和`exit`，这种锁被称之为重量级锁。

  所以，在JDK1.6中出现对锁进行了很多的优化，进而出现轻量级锁，偏向锁，锁消除，适应性自旋锁，锁粗化(自旋锁在1.4就有，只不过默认的是关闭的，jdk1.6是默认开启的)，这些操作都是为了在线程之间更高效的共享数据 ，解决竞争问题。



#### final

final是Java中的一个关键字，它所表示的是“这部分是无法修改的”。

使用 final 可以定义 ：变量、方法、类。

- 如果将变量设置为final，则不能更改final变量的值(它将是常量)。

- 如果任何方法声明为final，则不能覆盖它。
- 如果把任何一个类声明为final，则不能继承它。



#### static

static表示“静态”的意思，用来修饰成员变量和成员方法，也可以形成静态static代码块

- **静态变量**

  我们用static表示变量的级别，一个类中的静态变量，不属于类的对象或者实例。因为静态变量与所有的对象实例共享，因此他们不具线程安全性。

  通常，静态变量常用final关键来修饰，表示通用资源或可以被所有的对象所使用。如果静态变量未被私有化，可以用“类名.变量名”的方式来使用。

  ```java
  //static variable example
  private static int count;
  public static String str;
  ```

- **静态方法**

  与静态变量一样，静态方法是属于类而不是实例。

  一个静态方法只能使用静态变量和调用静态方法。通常静态方法通常用于想给其他的类使用而不需要创建实例。例如：Collections class(类集合)。

  Java的包装类和实用类包含许多静态方法。main()方法就是Java程序入口点，是静态方法。

  ```java
  //static method example
  public static void setCount(int count) {
      if(count &gt; 0)
      StaticExample.count = count;
  }
  
  //static util method
  public static int addInts(int i, int...js){
      int sum=i;
      for(int x : js) sum+=x;
      return sum;
  }
  ```

  从Java8以上版本开始也可以有接口类型的静态方法了。

- **静态代码块**

  Java的静态块是一组指令在类装载的时候在内存中由Java ClassLoader执行。

  静态块常用于初始化类的静态变量。大多时候还用于在类装载时候创建静态资源。

  Java不允许在静态块中使用非静态变量。一个类中可以有多个静态块，尽管这似乎没有什么用。静态块只在类装载入内存时，执行一次。

- **静态类**

  Java可以嵌套使用静态类，但是静态类不能用于嵌套的顶层。

  静态嵌套类的使用与其他顶层类一样，嵌套只是为了便于项目打包。

- **有关static的面试题**

  1. 为什么Java中不允许在静态方法或静态块中使用非静态变量？
  
     - 笼统性回答：
  
       Java中static修饰的东西(方法、字段)都是属于类的不是属于实例的，所以不能。
  
     - 从JVM角度回答：
  
       在Java中不允许在静态方法或静态块中使用非静态变量完全是JVM类加载过程的这种设计导致的。
  
       JVM类加载过程分为：加载、验证、准备、解析、初始化等几个阶段。
  
       其中准备阶段是正式为类中定义的变量（即静态变量，被static修饰的变量）分配内存并设置类变量初始零值的阶段，例如：
       ```java
       public static int value = 123;
       ```
       那变量value在准备阶段过后的初始值为0而不是123，因为这时尚未开始执行任何Java方法，而把value赋值为123的putstatic指令是程序被编译后，存放于类构造器<clinit>()方法之中，所以把value赋值为123的动作要到类的初始化阶段才会被执行。其中类构造器<clinit>()方法是由编译器自动收集类中的所有类变量的赋值动作和静态语句块（static{}块）中的语句合并产生的。而<init>()方法即是我们熟悉的类构造器方法。
       
       **而作为类加载过程的最后环节初始化阶段就是执行类构造器<clinit>()方法的过程，这个初始化阶段(<clinit>)是先于new类的实例(<init>)前执行的，此时非静态成员变量尚未被初始赋值，如何能出现在先期与它的类初始化阶段?**
  
     
  
  2. 类初始化一题
  
     ```java
     /**
      * <clinit>()方法执行顺序示例
      *
      * ·<clinit>()方法与类的构造函数（即在虚拟机视角中的实例构造器 <init>()方法）不同，
      * 它不需要显式地调用父类构造器，Java虚拟机会保证在子类的<clinit>()方法执行前，
      * 父类的<clinit>()方法已经执行完毕。因此在Java虚拟机中第一个被执行的<clinit>()
      * 方法的类型肯定是java.lang.Object。
      *
      * 由于父类的<clinit>()方法先执行，也就意味着父类中定义的静态语句块要优先于子类的变量赋值操作
      *
      */
     public class ClassInitializationOrderExample {
     
         static class Parent {
     
             public static int A = 1;
     
             static {
                 A = 2;
             }
     
         }
     
         static class Sub extends Parent {
     
             public static int B = A;
     
             public static int C = 3;
     
             static {
                 /**
                  * 此处输出值为2，说明子类与父类中定义的静态变量与静态块从虚拟机的视角的clinit()上看具有执行隔离性的特征
                  * 而且是父类先于子类执行其clinit()方法
                  */
                 System.out.println(B);
             }
     
         }
     
         //下面两句sout语句必须注释其中之一
         public static void main(String[] args) {
             System.out.println(Sub.C); //调用Sub.C触发JVM对Sub类进行类加载
             /**
              * 对于静态字段，只有直接定义这个字段的类才会被初始化，因此通过其子类来引用父类中定义的静态字段，
              * 只会触发父类的初始化而不会触发子类的初始化。
              */
             //System.out.println(Sub.A); //调用Sub.A不会触发JVM对Sub类进行类加载，因为A的定义出现在Parent类中
         }
     
     }
     ```



### 集合类

#### Java常用集合概述

- List , Set, Map都是接口，前两个继承至Collection接口，Map为独立接口
- Set下有HashSet，LinkedHashSet，TreeSet
- List下有ArrayList，Vector，LinkedList
- Map下有HashTable，LinkedHashMap，HashMap，TreeMap
- Collection接口下还有个Queue接口(与List、Set同一级别)，有PriorityQueue类

#### Iterable

- **Iterable简介**

  可迭代集合的标记接口，是java.util.Collection的父接口

- **Iterable源码**

  ```java
  /**
   * 可迭代集合的标记接口，是java.util.Collection的父接口
   * @since 1.5
   */
  public interface Iterable<T> {
      /**
       * 返回一个迭代器
       */
      Iterator<T> iterator();
  
      /**
       * 一个直接迭代的方法
       * @since 1.8
       */
      default void forEach(Consumer<? super T> action) {
          Objects.requireNonNull(action);
          for (T t : this) {
              action.accept(t);
          }
      }
  
      /**
       * 从JDK 8开始新加的一个方法，返回一个Spliterator对象，
       * 
       * Spliterator是Java 8中加入的一个新接口；这个名字代表“可拆分迭代器”（splitable iterator）。和Iterator一样，
       * Spliterator也用于遍历数据源中的元素，但它是为了并行执行而设计的。Java 8已经为集合框架中包含的所有数据结构提供了一
       * 个默认的Spliterator实现。集合实现了Spliterator接口，接口提供了一个spliterator方法。
       *
       * @since 1.8
       */
      default Spliterator<T> spliterator() {
          return Spliterators.spliteratorUnknownSize(iterator(), 0);
      }
  }
  ```



#### Collection

- **Collection简介**

  Collection是java.util.List和java.util.Set的直接父接口

- **Collection源码**

  ```java
  /**
   * @since 1.2
   */
  
  public interface Collection<E> extends Iterable<E> {
      //add
      //remove
      //stream
      ...
  }
  ```

#### ArrayList

- **ArrayList简介**

  `ArrayList` 的底层是基于数组实现的，与Java中的数组相比，它的容量能动态增加。在添加大量元素前，应用程序可以使用`ensureCapacity`操作来增加 `ArrayList` 实例的容量。

  它继承于 `AbstractList`，实现了 `List`, `RandomAccess`, `Cloneable`, `Serializable` 这些接口。

  `ArrayList` 实现了**RandomAccess** 接口， **RandomAccess** 是一个标记接口，表明实现这个这个接口的 `List` 集合是支持快速随机访问的。在 `ArrayList` 中，我们即可以通过元素的序号快速获取元素对象，这就是快速随机访问。

  `ArrayList` 实现了**Cloneable** 接口，即覆盖了函数 `clone()`，能被克隆(浅拷贝，列表元素本身不会被克隆)。

  `ArrayList` 实现**Serializable** 接口，这意味着`ArrayList`支持序列化，能通过序列化去传输。

  和 `Vector` 不同，`ArrayList` 中的操作不是线程安全的！所以，建议在单线程中才使用 `ArrayList`，而在多线程中可以选择 `Vector` 或者 `CopyOnWriteArrayList`。

- **ArrayList核心源码**

  ```java
  package java.util;
  
  import java.util.function.Consumer;
  import java.util.function.Predicate;
  import java.util.function.UnaryOperator;
  
  
  public class ArrayList<E> extends AbstractList<E>
          implements List<E>, RandomAccess, Cloneable, java.io.Serializable
  {
      private static final long serialVersionUID = 8683452581122892189L;
  
      /**
       * 默认初始容量大小
       */
      private static final int DEFAULT_CAPACITY = 10;
  
      /**
       * 空数组（用于空实例）。
       */
      private static final Object[] EMPTY_ELEMENTDATA = {};
  
       //用于默认大小空实例的共享空数组实例。
        //我们把它从EMPTY_ELEMENTDATA数组中区分出来，以知道在添加第一个元素时容量需要增加多少。
      private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
  
      /**
       * 保存ArrayList数据的数组，使用transient在序列化时只序列化elementData[0, size]区间的元素，
       * 后面的null元素丢弃了，这样做的目的是省空间。
       */
      transient Object[] elementData; // non-private to simplify nested class access
  
      /**
       * ArrayList 所包含的元素个数
       */
      private int size;
  
      /**
       * 带初始容量参数的构造函数。（用户自己指定容量）
       */
      public ArrayList(int initialCapacity) {
          if (initialCapacity > 0) {
              //创建initialCapacity大小的数组
              this.elementData = new Object[initialCapacity];
          } else if (initialCapacity == 0) {
              //创建空数组
              this.elementData = EMPTY_ELEMENTDATA;
          } else {
              throw new IllegalArgumentException("Illegal Capacity: "+
                                                 initialCapacity);
          }
      }
  
      /**
       * 默认构造函数，DEFAULTCAPACITY_EMPTY_ELEMENTDATA 为0.初始化为10，
       * 也就是说初始其实是空数组 当添加第一个元素的时候数组容量才变成10
       * 这也是为什么建议在new ArrayList()时尽量加一个初始容量
       */
      public ArrayList() {
          this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
      }
  
      /**
       * 构造一个包含指定集合的元素的列表，按照它们由集合的迭代器返回的顺序。
       */
      public ArrayList(Collection<? extends E> c) {
          //
          elementData = c.toArray();
          //如果指定集合元素个数不为0
          if ((size = elementData.length) != 0) {
              // c.toArray 可能返回的不是Object类型的数组所以加上下面的语句用于判断，
              //这里用到了反射里面的getClass()方法
              if (elementData.getClass() != Object[].class)
                  elementData = Arrays.copyOf(elementData, size, Object[].class);
          } else {
              // 用空数组代替
              this.elementData = EMPTY_ELEMENTDATA;
          }
      }
  
      /**
       * 修改这个ArrayList实例的容量是列表的当前大小。 应用程序可以使用此操作来最小化ArrayList实例的存储。 
       */
      public void trimToSize() {
          modCount++;
          if (size < elementData.length) {
              elementData = (size == 0)
                ? EMPTY_ELEMENTDATA
                : Arrays.copyOf(elementData, size);
          }
      }
      //下面是ArrayList的扩容机制
      //ArrayList的扩容机制提高了性能，如果每次只扩充一个，
      //那么频繁的插入会导致频繁的拷贝，降低性能，而ArrayList的扩容机制避免了这种情况。
      /**
       * 如有必要，增加此ArrayList实例的容量，以确保它至少能容纳元素的数量
       * @param   minCapacity   所需的最小容量
       */
      public void ensureCapacity(int minCapacity) {
          int minExpand = (elementData != DEFAULTCAPACITY_EMPTY_ELEMENTDATA)
              // any size if not default element table
              ? 0
              // larger than default for default empty table. It's already
              // supposed to be at default size.
              : DEFAULT_CAPACITY;
  
          if (minCapacity > minExpand) {
              ensureExplicitCapacity(minCapacity);
          }
      }
      //得到最小扩容量
      private void ensureCapacityInternal(int minCapacity) {
          if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
              // 获取默认的容量和传入参数的较大值
              minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
          }
  
          ensureExplicitCapacity(minCapacity);
      }
      //判断是否需要扩容
      private void ensureExplicitCapacity(int minCapacity) {
          modCount++;
  
          // overflow-conscious code
          if (minCapacity - elementData.length > 0)
              //调用grow方法进行扩容，调用此方法代表已经开始扩容了
              grow(minCapacity);
      }
  
      /**
       * 要分配的最大数组大小
       */
      private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
  
      /**
       * ArrayList扩容的核心方法。
       */
      private void grow(int minCapacity) {
          // oldCapacity为旧容量，newCapacity为新容量
          int oldCapacity = elementData.length;
          //将oldCapacity 右移一位，其效果相当于oldCapacity /2，
          //我们知道位运算的速度远远快于整除运算，整句运算式的结果就是将新容量更新为旧容量的1.5倍，
          int newCapacity = oldCapacity + (oldCapacity >> 1);
          //然后检查新容量是否大于最小需要容量，若还是小于最小需要容量，那么就把最小需要容量当作数组的新容量，
          if (newCapacity - minCapacity < 0)
              newCapacity = minCapacity;
          //再检查新容量是否超出了ArrayList所定义的最大容量，
          //若超出了，则调用hugeCapacity()来比较minCapacity和 MAX_ARRAY_SIZE，
          //如果minCapacity大于MAX_ARRAY_SIZE，则新容量则为Interger.MAX_VALUE，否则，新容量大小则为 MAX_ARRAY_SIZE。
          if (newCapacity - MAX_ARRAY_SIZE > 0)
              newCapacity = hugeCapacity(minCapacity);
          // minCapacity is usually close to size, so this is a win:
          elementData = Arrays.copyOf(elementData, newCapacity);
      }
      //比较minCapacity和 MAX_ARRAY_SIZE
      private static int hugeCapacity(int minCapacity) {
          if (minCapacity < 0) // 上面的扩容导致了int溢出?
              throw new OutOfMemoryError();
          return (minCapacity > MAX_ARRAY_SIZE) ?
              Integer.MAX_VALUE :
              MAX_ARRAY_SIZE;
      }
  
      /**
       *返回此列表中的元素数。 
       */
      public int size() {
          return size;
      }
  
      /**
       * 如果此列表不包含元素，则返回 true 。
       */
      public boolean isEmpty() {
          //注意=和==的区别
          return size == 0;
      }
  
      /**
       * 如果此列表包含指定的元素，则返回true 。
       */
      public boolean contains(Object o) {
          //indexOf()方法：返回此列表中指定元素的首次出现的索引，如果此列表不包含此元素，则为-1 
          return indexOf(o) >= 0;
      }
  
      /**
       *返回此列表中指定元素的首次出现的索引，如果此列表不包含此元素，则为-1 
       */
      public int indexOf(Object o) {
          if (o == null) {
              for (int i = 0; i < size; i++)
                  if (elementData[i]==null)
                      return i;
          } else {
              for (int i = 0; i < size; i++)
                  //equals()方法比较
                  if (o.equals(elementData[i]))
                      return i;
          }
          return -1;
      }
  
      /**
       * 返回此列表中指定元素的最后一次出现的索引，如果此列表不包含元素，则返回-1。.
       */
      public int lastIndexOf(Object o) {
          if (o == null) {
              for (int i = size-1; i >= 0; i--)
                  if (elementData[i]==null)
                      return i;
          } else {
              for (int i = size-1; i >= 0; i--)
                  if (o.equals(elementData[i]))
                      return i;
          }
          return -1;
      }
  
      /**
       * 返回此ArrayList实例的浅拷贝。 （元素本身不被复制。） 
       */
      public Object clone() {
          try {
              ArrayList<?> v = (ArrayList<?>) super.clone();
              //Arrays.copyOf功能是实现数组的复制，返回复制后的数组。参数是被复制的数组和复制的长度
              v.elementData = Arrays.copyOf(elementData, size);
              v.modCount = 0;
              return v;
          } catch (CloneNotSupportedException e) {
              // 这不应该发生，因为我们是可以克隆的
              throw new InternalError(e);
          }
      }
  
      /**
       *以正确的顺序（从第一个到最后一个元素）返回一个包含此列表中所有元素的数组。 
       *返回的数组将是“安全的”，因为该列表不保留对它的引用。 （换句话说，这个方法必须分配一个新的数组）。
       *因此，调用者可以自由地修改返回的数组。 此方法充当基于阵列和基于集合的API之间的桥梁。
       */
      public Object[] toArray() {
          return Arrays.copyOf(elementData, size);
      }
  
      /**
       * 以正确的顺序返回一个包含此列表中所有元素的数组（从第一个到最后一个元素）; 
       *返回的数组的运行时类型是指定数组的运行时类型。 如果列表适合指定的数组，则返回其中。 
       *否则，将为指定数组的运行时类型和此列表的大小分配一个新数组。 
       *如果列表适用于指定的数组，其余空间（即数组的列表数量多于此元素），则紧跟在集合结束后的数组中的元素设置为null 。
       *（这仅在调用者知道列表不包含任何空元素的情况下才能确定列表的长度。） 
       */
      @SuppressWarnings("unchecked")
      public <T> T[] toArray(T[] a) {
          if (a.length < size)
              // 新建一个运行时类型的数组，但是ArrayList数组的内容
              return (T[]) Arrays.copyOf(elementData, size, a.getClass());
              //调用System提供的arraycopy()方法实现数组之间的复制
          System.arraycopy(elementData, 0, a, 0, size);
          if (a.length > size)
              a[size] = null;
          return a;
      }
  
      // Positional Access Operations
  
      @SuppressWarnings("unchecked")
      E elementData(int index) {
          return (E) elementData[index];
      }
  
      /**
       * 返回此列表中指定位置的元素。
       */
      public E get(int index) {
          rangeCheck(index);
  
          return elementData(index);
      }
  
      /**
       * 用指定的元素替换此列表中指定位置的元素。 
       */
      public E set(int index, E element) {
          //对index进行界限检查
          rangeCheck(index);
  
          E oldValue = elementData(index);
          elementData[index] = element;
          //返回原来在这个位置的元素
          return oldValue;
      }
  
      /**
       * 将指定的元素追加到此列表的末尾。 
       */
      public boolean add(E e) {
          ensureCapacityInternal(size + 1);  // Increments modCount!!
          //这里看到ArrayList添加元素的实质就相当于为数组赋值
          elementData[size++] = e;
          return true;
      }
  
      /**
       * 在此列表中的指定位置插入指定的元素。 
       *先调用 rangeCheckForAdd 对index进行界限检查；然后调用 ensureCapacityInternal 方法保证capacity足够大；
       *再将从index开始之后的所有成员后移一个位置；将element插入index位置；最后size加1。
       */
      public void add(int index, E element) {
          rangeCheckForAdd(index);
  
          ensureCapacityInternal(size + 1);  // Increments modCount!!
          //arraycopy()这个实现数组之间复制的方法一定要看一下，下面就用到了arraycopy()方法实现数组自己复制自己
          System.arraycopy(elementData, index, elementData, index + 1,
                           size - index);
          elementData[index] = element;
          size++;
      }
  
      /**
       * 删除该列表中指定位置的元素。 将任何后续元素移动到左侧（从其索引中减去一个元素）。 
       */
      public E remove(int index) {
          rangeCheck(index);
  
          modCount++;
          E oldValue = elementData(index);
  
          int numMoved = size - index - 1;
          if (numMoved > 0)
              System.arraycopy(elementData, index+1, elementData, index,
                               numMoved);
          elementData[--size] = null; // clear to let GC do its work
        //从列表中删除的元素 
          return oldValue;
      }
  
      /**
       * 从列表中删除指定元素的第一个出现（如果存在）。 如果列表不包含该元素，则它不会更改。
       *返回true，如果此列表包含指定的元素
       */
      public boolean remove(Object o) {
          if (o == null) {
              for (int index = 0; index < size; index++)
                  if (elementData[index] == null) {
                      fastRemove(index);
                      return true;
                  }
          } else {
              for (int index = 0; index < size; index++)
                  if (o.equals(elementData[index])) {
                      fastRemove(index);
                      return true;
                  }
          }
          return false;
      }
  
      /*
       * Private remove method that skips bounds checking and does not
       * return the value removed.
       */
      private void fastRemove(int index) {
          modCount++;
          int numMoved = size - index - 1;
          if (numMoved > 0)
              System.arraycopy(elementData, index+1, elementData, index,
                               numMoved);
          elementData[--size] = null; // clear to let GC do its work
      }
  
      /**
       * 从列表中删除所有元素。 
       */
      public void clear() {
          modCount++;
  
          // 把数组中所有的元素的值设为null
          for (int i = 0; i < size; i++)
              elementData[i] = null;
  
          size = 0;
      }
  
      /**
       * 按指定集合的Iterator返回的顺序将指定集合中的所有元素追加到此列表的末尾。
       */
      public boolean addAll(Collection<? extends E> c) {
          Object[] a = c.toArray();
          int numNew = a.length;
          ensureCapacityInternal(size + numNew);  // Increments modCount
          System.arraycopy(a, 0, elementData, size, numNew);
          size += numNew;
          return numNew != 0;
      }
  
      /**
       * 将指定集合中的所有元素插入到此列表中，从指定的位置开始。
       */
      public boolean addAll(int index, Collection<? extends E> c) {
          rangeCheckForAdd(index);
  
          Object[] a = c.toArray();
          int numNew = a.length;
          ensureCapacityInternal(size + numNew);  // Increments modCount
  
          int numMoved = size - index;
          if (numMoved > 0)
              System.arraycopy(elementData, index, elementData, index + numNew,
                               numMoved);
  
          System.arraycopy(a, 0, elementData, index, numNew);
          size += numNew;
          return numNew != 0;
      }
  
      /**
       * 从此列表中删除所有索引为fromIndex （含）和toIndex之间的元素。
       *将任何后续元素移动到左侧（减少其索引）。
       */
      protected void removeRange(int fromIndex, int toIndex) {
          modCount++;
          int numMoved = size - toIndex;
          System.arraycopy(elementData, toIndex, elementData, fromIndex,
                           numMoved);
  
          // clear to let GC do its work
          int newSize = size - (toIndex-fromIndex);
          for (int i = newSize; i < size; i++) {
              elementData[i] = null;
          }
          size = newSize;
      }
  
      /**
       * 检查给定的索引是否在范围内。
       */
      private void rangeCheck(int index) {
          if (index >= size)
              throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
      }
  
      /**
       * add和addAll使用的rangeCheck的一个版本
       */
      private void rangeCheckForAdd(int index) {
          if (index > size || index < 0)
              throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
      }
  
      /**
       * 返回IndexOutOfBoundsException细节信息
       */
      private String outOfBoundsMsg(int index) {
          return "Index: "+index+", Size: "+size;
      }
  
      /**
       * 从此列表中删除指定集合中包含的所有元素。 
       */
      public boolean removeAll(Collection<?> c) {
          Objects.requireNonNull(c);
          //如果此列表被修改则返回true
          return batchRemove(c, false);
      }
  
      /**
       * 仅保留此列表中包含在指定集合中的元素。
       *换句话说，从此列表中删除其中不包含在指定集合中的所有元素。 
       */
      public boolean retainAll(Collection<?> c) {
          Objects.requireNonNull(c);
          return batchRemove(c, true);
      }
  
  
      /**
       * 从列表中的指定位置开始，返回列表中的元素（按正确顺序）的列表迭代器。
       *指定的索引表示初始调用将返回的第一个元素为next 。 初始调用previous将返回指定索引减1的元素。 
       *返回的列表迭代器是fail-fast 。 
       */
      public ListIterator<E> listIterator(int index) {
          if (index < 0 || index > size)
              throw new IndexOutOfBoundsException("Index: "+index);
          return new ListItr(index);
      }
  
      /**
       *返回列表中的列表迭代器（按适当的顺序）。 
       *返回的列表迭代器是fail-fast 。
       */
      public ListIterator<E> listIterator() {
          return new ListItr(0);
      }
  
      /**
       *以正确的顺序返回该列表中的元素的迭代器。 
       *返回的迭代器是fail-fast 。 
       */
      public Iterator<E> iterator() {
          return new Itr();
      }
      
      ...
  }
  ```

- **ArrayList中的IndexOutOfBoundsException和ConcurrentModificationException**

  1. java.lang.IndexOutOfBoundsException
  
     ```java
     /**
      * 通过常规的数组迭代方式遍历List：for(int i = 0; i < len; i++) {...}
      * 则只会检测数组下标是否越界
      * 也即只会报java.lang.IndexOutOfBoundsException
      */
     public static void testIndexOutOfBoundsException2() {
         List<Integer> original = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
         //以下两种方式均会触发java.lang.IndexOutOfBoundsException
         try {
             //删除偶数位上的元素(实现方式1)
             List<Integer> list1 = new ArrayList<>(original);
             for (int i = 0, len = list1.size(); i < len; i++) {
                 if (i % 2 == 0) {
                     //调用ArrayList.remove(int index)
                     list1.remove(i); //java.lang.IndexOutOfBoundsException
                 }
                 System.out.println(list1);
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
         System.out.println("------------------------------------------------");
         try {
             //删除偶数位上的元素(实现方式2)
             List<Integer> list2 = new ArrayList<>(original);
             for(int i = 0, len = list2.size(); i < len; i++){
                 Integer element = list2.get(i); //java.lang.IndexOutOfBoundsException
                 if(i % 2 == 0){
                     //调用ArrayList.remove(Object o)
                     list2.remove(element);
                 }
                 System.out.println(list2);
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
     ```
  
  2. java.util.ConcurrentModificationException
  
     ```java
     /**
      * JDK 5新增的 foreach循环其实是个语法糖，通过反编译得知其相当于
      * for(Iterator it = list.iterator(); it.hasNext();) { Integer i = (Integer)it.next(); ... }
      *
      * 只要是通过Iterator接口迭代集合，则只会进行checkForComodification()检测
      * 产生java.util.ConcurrentModificationException的原因是语句Iterator it = list.iterator();生成了一个迭代器对象，
      * 这个迭代器见java.util.ArrayList$Itr，在new出迭代器的那一刻，迭代器中的expectedModCount固化为ArrayList.modCount，
      * 只有调用Iterator.remove()方法时才会更新ArrayList$Itr.expectedModCount的值与ArrayList.modCount保持同步
      * 而当调用list.remove()时是不会进行上述同步操作的，于此同时在每次调用迭代器ArrayList$Itr.next()方法时都会检测他们两者是否一样,
      * 这样就导致了java.util.ConcurrentModificationException
      *
      * 解决方法就是只能调用Iterator.remove()来删除元素
      */
     public static void testConcurrentModificationException() {
         List<Integer> original = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
     
         //错误的在迭代过程中删除元素的方式
         try {
             List<Integer> list1 = new ArrayList<>(original);
             for (Integer i : list1) {
                 if (i % 2 == 0) {
                     list1.remove(i);
                 }
                 System.out.println(list1);
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
         System.out.println("-----------------------------------------");
         //正确的在迭代过程中删除元素的方式
         try {
             List<Integer> list2 = new ArrayList<>(original);
             for (Iterator<Integer> it  = list2.iterator(); it.hasNext();) {
                 Integer i = it.next();
                 if (i % 2 == 0) {
                     it.remove();
                 }
                 System.out.println(list2);
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
     ```
     
  3. **总结：**
  
     - 在以数组方式迭代List集合的时候只有可能触发java.lang.IndexOutOfBoundsException
  
     - 在以Iterator迭代器迭代集合的时候只有可能触发java.util.ConcurrentModificationException
  
     - 在单线程的情况下，遍历中删除元素请使用Iterator迭代器遍历及使用Iterator.remove()方法删除元素。



#### LinkedList

- **LinkedList简介**

  LinkedList是一个实现了List接口和Deque接口的双端链表。 LinkedList底层的链表结构使它支持高效的插入和删除操作，另外它实现了Deque接口，使得LinkedList类也具有队列的特性; LinkedList不是线程安全的，如果想使LinkedList变成线程安全的，可以调用静态类Collections类中的synchronizedList方法：

  ```java
  List list=Collections.synchronizedList(new LinkedList(...));
  ```

- **LinkedList内部实现**

  ![linked-list-structure.jpg](src\main\java\com\penglecode\xmodule\master4j\java\util\collection\linked-list-structure.jpg)

  看完了图之后，我们再看`LinkedList`类中的一个内部私有类`Node`就很好理解了：

  ```java
  private static class Node<E> {
      E item;//节点值
      Node<E> next;//后继节点
      Node<E> prev;//前驱节点
  
      Node(Node<E> prev, E element, Node<E> next) {
          this.item = element;
          this.next = next;
          this.prev = prev;
      }
  }
  ```
  这个类就代表双端链表的节点Node。这个类有三个属性，分别是前驱节点，本节点的值，后继结点。

- **LinkedList核心源码**
  
  空的构造方法：
  ```java
  public class LinkedList<E>
      extends AbstractSequentialList<E>
      implements List<E>, Deque<E>, Cloneable, java.io.Serializable
  {
      //空的构造方法：
      public LinkedList() {
      }
  
      //用已有的集合创建链表的构造方法：
      public LinkedList(Collection<? extends E> c) {
          this();
          addAll(c);
      }
  
      //add方法：
      public boolean add(E e) {
          linkLast(e);//这里就只调用了这一个方法
          return true;
      }
  
      /**
       * 链接使e作为最后一个元素。
       */
      void linkLast(E e) {
          final Node<E> l = last; //先缓存当前last
          final Node<E> newNode = new Node<>(l, e, null); //当前last作为新节点的prev
          last = newNode;//新增节点肯定是last
          if (l == null) //如果未添加newNode之前列表是空列表?
              first = newNode; //那么newNode即是first也是last
          else
              l.next = newNode;//否则原来的last.next即是当前新增节点newNode
          size++;
          modCount++;
      }
  
      //add(int index,E e)：在指定位置添加元素
      public void add(int index, E element) {
          checkPositionIndex(index); //检查索引是否处于[0-size]之间
  
          if (index == size)//添加在链表尾部
              linkLast(element);
          else//添加在链表中间
              linkBefore(element, node(index));
      }
  
      /**
       * 根据索引index的位置在整个列表的前半部还是后半部来确定遍历顺序以增加遍历效率
       */
      Node<E> node(int index) {
          // assert isElementIndex(index);
  
          if (index < (size >> 1)) { //如果查询的索引index在 前半部?则从first开始顺序遍历
              Node<E> x = first;
              for (int i = 0; i < index; i++)
                  x = x.next;
              return x;
          } else { //否则索引index在后半部，从last开始逆向遍历
              Node<E> x = last;
              for (int i = size - 1; i > index; i--)
                  x = x.prev;
              return x;
          }
      }
  
      //addAll(Collection c )：将集合插入到链表尾部
      public boolean addAll(Collection<? extends E> c) {
          return addAll(size, c);
      }
  
      //addAll(int index, Collection c)： 将集合从指定位置开始插入
      public boolean addAll(int index, Collection<? extends E> c) {
          //1:检查index范围是否在size之内
          checkPositionIndex(index);
  
          //2:toArray()立即通过复制创建集合c的即时快照
          Object[] a = c.toArray();
          int numNew = a.length;
          if (numNew == 0)
              return false;
  
          //3：得到插入位置的前驱节点和后继节点
          Node<E> pred, succ;
          //如果插入位置为尾部，前驱节点为last，后继节点为null
          if (index == size) {
              succ = null;
              pred = last;
          }
          //否则，调用node()方法得到后继节点，再得到前驱节点
          else {
              succ = node(index);
              pred = succ.prev;
          }
  
          // 4：遍历数据将数据插入
          for (Object o : a) {
              @SuppressWarnings("unchecked") E e = (E) o;
              //创建新节点
              Node<E> newNode = new Node<>(pred, e, null);
              //如果插入位置在链表头部
              if (pred == null)
                  first = newNode;
              else
                  pred.next = newNode;
              pred = newNode; //新插入节点newNode作为下一个新插入节点的前驱节点
          }
  
          //如果插入位置在尾部，重置last节点
          if (succ == null) {
              last = pred;
          }
          //否则，将插入的链表与先前链表连接起来
          else {
              pred.next = succ;
              succ.prev = pred;
          }
  
          size += numNew;
          modCount++;
          return true;
      }
  
      //addFirst(E e)： 将元素添加到链表头部
      public void addFirst(E e) {
          linkFirst(e);
      }
  
      /**
       * 链接使e作为最第一个元素。
       */
      private void linkFirst(E e) {
          final Node<E> f = first; //先缓存当前first元素
          final Node<E> newNode = new Node<>(null, e, f);//新增节点，以头节点为后继节点
          first = newNode; //将新增节点作为first
          if (f == null) //如果链表为空，last节点也指向该节点
              last = newNode;
          else //否则，将头节点的前驱指针指向新节点，也就是指向前一个元素
              f.prev = newNode;
          size++;
          modCount++;
      }
  
      //addLast(E e)： 将元素添加到链表尾部，与 add(E e) 方法一样
      public void addLast(E e) {
          linkLast(e);
      }
  
      //get(int index)： 根据指定索引返回数据
      public E get(int index) {
          //检查index范围是否在size之内
          checkElementIndex(index);
          //调用Node(index)去找到index对应的node然后返回它的值
          return node(index).item;
      }
  
      //获取头节点（index=0）数据方法:
      //以下几个从Deque来的几个方法区别： getFirst(),element(),peek(),peekFirst() 这四个获取头结点方法的区别在于对链表为  空时的处理，是抛出异常还是返回null，其中getFirst() 和element() 方法将会在链表为空时，抛出异常。
      public E getFirst() {
          final Node<E> f = first;
          if (f == null)
              throw new NoSuchElementException();
          return f.item;
      }
  
      //获取头节点，与getFirst()一样
      public E element() {
          return getFirst();
      }
  
      //获取头节点
      public E peek() {
          final Node<E> f = first;
          return (f == null) ? null : f.item;
      }
  
      //获取头节点
      public E peekFirst() {
          final Node<E> f = first;
          return (f == null) ? null : f.item;
      }
  
  
      //获取尾节点（index=-1）数据方法:
      //getLast() 方法在链表为空时，会抛出NoSuchElementException，而peekLast() 则不会，只是会返回 null。
      public E getLast() {
          final Node<E> l = last;
          if (l == null)
              throw new NoSuchElementException();
          return l.item;
      }
  
      public E peekLast() {
          final Node<E> l = last;
          return (l == null) ? null : l.item;
      }
  
  
      //根据对象得到索引的方法
      //int indexOf(Object o)： 从头遍历找
      public int indexOf(Object o) {
          int index = 0;
          if (o == null) {
              //从头遍历
              for (Node<E> x = first; x != null; x = x.next) {
                  if (x.item == null)
                      return index;
                  index++;
              }
          } else {
              //从头遍历
              for (Node<E> x = first; x != null; x = x.next) {
                  if (o.equals(x.item))
                      return index;
                  index++;
              }
          }
          return -1;
      }
  
      //int lastIndexOf(Object o)： 从尾遍历找
      public int lastIndexOf(Object o) {
          int index = size;
          if (o == null) {
              //从尾遍历
              for (Node<E> x = last; x != null; x = x.prev) {
                  index--;
                  if (x.item == null)
                      return index;
              }
          } else {
              //从尾遍历
              for (Node<E> x = last; x != null; x = x.prev) {
                  index--;
                  if (o.equals(x.item))
                      return index;
              }
          }
          return -1;
      }
  
      //contains(Object o)： 检查对象o是否存在于链表中
      public boolean contains(Object o) {
          return indexOf(o) != -1;
      }
  
      //删除方法
      //remove() ,removeFirst(),pop(): 删除头节点
      public E pop() {
          return removeFirst();
      }
  
      public E remove() {
          return removeFirst();
      }
      public E removeFirst() {
          final Node<E> f = first;
          if (f == null)
              throw new NoSuchElementException();
          return unlinkFirst(f);
      }
  
      //removeLast(),pollLast(): 删除尾节点
      public E removeLast() {
          final Node<E> l = last;
          if (l == null)
              throw new NoSuchElementException();
          return unlinkLast(l);
      }
  
      public E pollLast() {
          final Node<E> l = last;
          return (l == null) ? null : unlinkLast(l);
      }
  
      //remove(Object o): 删除指定元素
      public boolean remove(Object o) {
          //如果删除对象为null
          if (o == null) {
              //从头开始遍历
              for (Node<E> x = first; x != null; x = x.next) {
                  //找到元素
                  if (x.item == null) {
                     //从链表中移除找到的元素
                      unlink(x);
                      return true;
                  }
              }
          } else {
              //从头开始遍历
              for (Node<E> x = first; x != null; x = x.next) {
                  //找到元素
                  if (o.equals(x.item)) {
                      //从链表中移除找到的元素
                      unlink(x);
                      return true;
                  }
              }
          }
          return false;
      }
  
      //unlink(Node x) 方法：即将指定的Node从列表中删除
      E unlink(Node<E> x) {
          // assert x != null;
          final E element = x.item;
          final Node<E> next = x.next;//得到后继节点
          final Node<E> prev = x.prev;//得到前驱节点
  
          //删除前驱指针
          if (prev == null) {
              first = next;//如果删除的节点是头节点,令头节点指向该节点的后继节点
          } else {
              prev.next = next;//将前驱节点的后继节点指向后继节点
              x.prev = null;
          }
  
          //删除后继指针
          if (next == null) {
              last = prev;//如果删除的节点是尾节点,令尾节点指向该节点的前驱节点
          } else {
              next.prev = prev;
              x.next = null;
          }
  
          x.item = null;
          size--;
          modCount++;
          return element;
      }
  
      //remove(int index)：删除指定位置的元素
      public E remove(int index) {
          //检查index范围
          checkElementIndex(index);
          //将节点删除
          return unlink(node(index));
      }
  }
  ```
  
- **ArrayList和LinkedList的性能对比**

  ```java
  /**
   * 测试ArrayList和LinkedList的add(int index, Object element)性能
   *                          get(index)性能
   *
   * 调用500000次java.util.ArrayList.add(int index, Object element)方法，耗时：20169 ms
   * 调用500000次java.util.ArrayList.get(int index)方法，耗时：13 ms
   *
   * 调用500000次java.util.LinkedList.add(int index, Object element)方法，耗时：73 ms
   * 调用500000次java.util.LinkedList.get(int index)方法，耗时：672503 ms
   * 由此可见LinkedList的按index检索实在是太慢了
   */
  public static void testPerformance(List<String> list, int count) {
      long start = System.currentTimeMillis();
      for (int i = 0; i < count; i++) {
          list.add(0, String.valueOf(i));
      }
      long end = System.currentTimeMillis();
      System.out.println(String.format("调用%s次%s.add(int index, Object element)方法，耗时：%s ms", count, list.getClass().getName(), (end - start)));
  
      Random random = new Random();
      start = System.currentTimeMillis();
      for (int i = 0; i < count; i++) {
          int index = random.nextInt(count);
          list.get(index);
      }
      end = System.currentTimeMillis();
      System.out.println(String.format("调用%s次%s.get(int index)方法，耗时：%s ms", count, list.getClass().getName(), (end - start)));
  }
  
  public static void main(String[] args) {
      int count = 500000;
      List<String> aList = new ArrayList<>(count);
      List<String> lList = new LinkedList<>();
  
      //testPerformance(aList, count);
      testPerformance(lList, count);
  }
  ```



#### Collection和Collections区别

Collection 是一个集合接口。 它提供了对集合对象进行基本操作的通用接口方法。Collection接口在Java 类库中有很多具体的实现。是list，set等的父接口。

Collections 是一个包装类。 它包含有各种有关集合操作的静态多态方法。此类不能实例化，就像一个工具类，服务于Java的Collection框架。

日常开发中，不仅要了解Java中的Collection及其子类的用法，还要了解Collections用法。可以提升很多处理集合类的效率。

#### Set和List区别

List,Set都是继承自Collection接口。都是用来存储一组相同类型的元素的。

List特点：元素有放入顺序，元素可重复 。

有顺序，即先放入的元素排在前面。

Set特点：元素无放入顺序，元素不可重复。

无顺序，即先放入的元素不一定排在前面。 不可重复，即相同元素在set中只会保留一份。所以，有些场景下，set可以用来去重。 不过需要注意的是，set在元素插入时是要有一定的方法来判断元素是否重复的。这个方法很重要，决定了set中可以保存哪些元素。

#### ArrayList和LinkedList和Vector的区别

List主要有ArrayList、LinkedList与Vector几种实现。

这三者都实现了List 接口，使用方式也很相似,主要区别在于因为实现方式的不同,所以对不同的操作具有不同的效率。

ArrayList 是一个可改变大小的数组.当更多的元素加入到ArrayList中时,其大小将会动态地增长.内部的元素可以直接通过get与set方法进行访问,因为ArrayList本质上就是一个数组.

LinkedList 是一个双链表,在添加和删除元素时具有比ArrayList更好的性能.但在get与set方面弱于ArrayList.

当然,这些对比都是指数据量很大或者操作很频繁的情况下的对比,如果数据和运算量很小,那么对比将失去意义.

Vector 和ArrayList类似,但属于强同步类。如果你的程序本身是线程安全的(thread-safe,没有在多个线程之间共享同一个集合/对象),那么使用ArrayList是更好的选择。

Vector和ArrayList在更多元素添加进来时会请求更大的空间。Vector每次请求其大小的双倍空间，而ArrayList每次对size增长50%.

而 LinkedList 还实现了 Queue 接口,该接口比List提供了更多的方法,包括 offer(),peek(),poll()等.

注意: 默认情况下ArrayList的初始容量非常小,所以如果可以预估数据量的话,分配一个较大的初始值属于最佳实践,这样可以减少调整大小的开销。

#### ArrayList使用了transient关键字进行存储优化，而Vector没有，为什么？

`ArrayList`使用了`transient`关键字进行存储优化，而`Vector`没有这样做，为什么？

- **ArrayList**

  ```java
/** 
     * Save the state of the <tt>ArrayList</tt> instance to a stream (that 
     * is, serialize it). 
     * 
     * @serialData The length of the array backing the <tt>ArrayList</tt> 
     *             instance is emitted (int), followed by all of its elements 
     *             (each an <tt>Object</tt>) in the proper order. 
     */  
    private void writeObject(java.io.ObjectOutputStream s)  
        throws java.io.IOException{  
        // Write out element count, and any hidden stuff  
        int expectedModCount = modCount;  
        s.defaultWriteObject();  

        // Write out array length  
        s.writeInt(elementData.length);  

        // Write out all elements in the proper order.  
        for (int i=0; i<size; i++)  
            s.writeObject(elementData[i]);  

        if (modCount != expectedModCount) {  
            throw new ConcurrentModificationException();  
        }  

    }  
  ```

  ArrayList实现了writeObject方法，可以看到只保存了非null的数组位置上的数据。即list的size个数的elementData。需要额外注意的一点是，ArrayList的实现，提供了fast-fail机制，可以提供弱一致性。

- **Vector**

  ```java
   /**
     * Save the state of the {@code Vector} instance to a stream (that
     * is, serialize it).
     * This method performs synchronization to ensure the consistency
     * of the serialized data.
     */
    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException {
        final java.io.ObjectOutputStream.PutField fields = s.putFields();
        final Object[] data;
        synchronized (this) {
            fields.put("capacityIncrement", capacityIncrement);
            fields.put("elementCount", elementCount);
            data = elementData.clone();
        }
        fields.put("elementData", data);
        s.writeFields();
    }
  ```

  Vector也实现了writeObject方法，但方法并没有像ArrayList一样进行优化存储，实现语句是`data = elementData.clone();`
  
  clone()的时候会把null值也拷贝。所以保存相同内容的Vector与ArrayList，Vector的占用的字节比ArrayList要多。
  
  可以测试一下，序列化存储相同内容的Vector与ArrayList，分别到一个文本文件中去。*Vector需要243字节，ArrayList需要135字节* 
  
  分析：
  
  ArrayList是非同步实现的一个单线程下较为高效的数据结构（相比Vector来说）。 ArrayList只通过一个修改记录字段提供弱一致性，主要用在迭代器里。没有同步方法。 即上面提到的Fast-fail机制.ArrayList的存储结构定义为transient，重写writeObject来实现自定义的序列化，优化了存储。
  
  Vector是多线程环境下更为可靠的数据结构，所有方法都实现了同步。
  
- **区别**

  > 同步处理：Vector同步，ArrayList非同步 
  >
  > 扩容机制：Vector缺省情况下扩容为原来一倍的数组长度，ArrayList是0.5倍. 
  >
  > ArrayList: int newCapacity = oldCapacity + (oldCapacity >> 1); ArrayList自动扩大容量为原来的1.5倍（实现的时候，方法会传入一个期望的最小容量，若扩容后容量仍然小于最小容量，那么容量就为传入的最小容量。扩容的时候使用的Arrays.copyOf方法最终调用native方法进行新数组创建和数据拷贝）
  >
  > Vector: int newCapacity = oldCapacity + ((capacityIncrement > 0) ? capacityIncrement : oldCapacity);
  >
  > Vector指定了`initialCapacity，capacityIncrement`来初始化的时候，每次增长`capacityIncrement`



#### SynchronizedList和Vector的区别

Vector是java.util包中的一个类。 SynchronizedList是java.util.Collections中的一个静态内部类。

在多线程的场景中可以直接使用Vector类，也可以使用Collections.synchronizedList(List list)方法来返回一个线程安全的List。

**那么，到底SynchronizedList和Vector有没有区别，为什么java api要提供这两种线程安全的List的实现方式呢？**

首先，我们知道Vector和Arraylist都是List的子类，他们底层的实现都是一样的。所以这里比较如下两个`list1`和`list2`的区别：

```java
List<String> list = new ArrayList<String>();
List list2 =  Collections.synchronizedList(list);
Vector<String> list1 = new Vector<String>();
```

**通过查看两者之间的源码发现：SynchronizedList里面实现的方法几乎都是使用同步代码块包上List的方法。如果该List是ArrayList,那么，SynchronizedList和Vector的一个比较明显区别就是一个使用了同步代码块，一个使用了同步方法。**

SynchronizedList和Vector最主要的区别： **1.SynchronizedList有很好的扩展和兼容功能。他可以将所有的List的子类转成线程安全的类。** **2.使用SynchronizedList的时候，进行遍历时要手动进行同步处理**。 **3.SynchronizedList可以指定锁定的对象。**



#### Set如何保证元素不重复?

在Java的Set体系中，根据实现方式不同主要分为两大类。HashSet和TreeSet。

1、TreeSet 是二叉树实现的，Treeset中的数据是自动排好序的，不允许放入null值 

2、HashSet 是哈希表实现的,HashSet中的数据是无序的，可以放入null，但只能放入一个null，两者中的值都不能重复，就如数据库中唯一约束

在HashSet中，基本的操作都是有HashMap底层实现的，因为HashSet底层是用HashMap存储数据的。当向HashSet中添加元素的时候，首先计算元素的hashcode值，然后通过扰动计算和按位与的方式计算出这个元素的存储位置，如果这个位置位空，就将元素添加进去；如果不为空，则用equals方法比较元素是否相等，相等就不添加，否则找一个空位添加。

TreeSet的底层是TreeMap的keySet()，而TreeMap是基于红黑树实现的，红黑树是一种平衡二叉查找树，它能保证任何一个节点的左右子树的高度差不会超过较矮的那棵的一倍。

TreeMap是按key排序的，元素在插入TreeSet时compareTo()方法要被调用，所以TreeSet中的元素要实现Comparable接口（不是必须要实现Comparable接口的）。TreeSet作为一种Set，它不允许出现重复元素。TreeSet是用compareTo()来判断重复元素的。

下面来个简单示例：

```java
/**
 * java.util.Set不可重复性示例
 *
 * - HashSet是无序的，底层是基于HashMap实现的，其不可重复特性是通过hashcode()方法和equals()方法来保证的
 * - TreeSet是有序的，底层是基于TreeMap实现的，TreeMap是按key排序的，元素在插入TreeSet时compareTo()方法要被调用，
 *   所以TreeSet中的元素要实现Comparable接口，其不可重复特性是通过Comparable.compareTo()方法来实现的
 */
public class SetUnrepeatableExample {

    static class Student1 {

        private Long id;

        private String name;

        public Student1(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Student1{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Student1)) return false;
            Student1 student = (Student1) o;
            return id.equals(student.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

    }

    static class Student2 implements Comparable<Student2> {

        private Long id;

        private String name;

        public Student2(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Student2{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }

        @Override
        public int compareTo(Student2 o) {
            if (o == null) {
                return 1;
            }
            if (this != o) {
                if (id == null) {
                    return -1;
                } else if (o.id == null) {
                    return 1;
                } else {
                    return id.compareTo(o.id);
                }
            }
            return 0;
        }
    }

    /**
     * 测试HashSet的不可重复性
     */
    public static void testHashSetUnrepeatable() {
        Set<Student1> set = new HashSet<>();
        set.add(new Student1(1L, "张三"));
        set.add(new Student1(2L, "李四"));
        set.add(new Student1(3L, "王五"));
        set.add(new Student1(1L, "赵六"));
        //[Student1{id=1, name='张三'}, Student1{id=2, name='李四'}, Student1{id=3, name='王五'}]
        System.out.println(set);
    }
    
    public static void testTreeSetUnrepeatable() {
        Set<Student2> set = new TreeSet<>();
        set.add(new Student2(1L, "张三"));
        set.add(new Student2(2L, "李四"));
        set.add(new Student2(3L, "王五"));
        set.add(new Student2(1L, "赵六"));
        //[Student2{id=1, name='张三'}, Student1{id=2, name='李四'}, Student1{id=3, name='王五'}]
        System.out.println(set);
    }

    public static void main(String[] args) {
        testHashSetUnrepeatable();
        testTreeSetUnrepeatable();
    }
}
```

#### HashMap、HashTable、ConcurrentHashMap区别

- **HashMap和HashTable有何不同？**

  1. 线程安全：

     HashTable 中的方法是同步的，而HashMap中的方法在默认情况下是非同步的。在多线程并发的环境下，可以直接使用HashTable，但是要使用HashMap的话就要自己增加同步处理了。

  2. 继承关系：

     HashTable是基于陈旧的Dictionary类继承来的。HashMap继承的抽象类AbstractMap实现了Map接口。

     ```java
     //@since   1.0
     public class Hashtable<K,V>
         extends Dictionary<K,V>
         implements Map<K,V>, Cloneable, java.io.Serializable {
         ...
     }
     ```
     ```java
     //@since   1.2
     public class HashMap<K,V> extends AbstractMap<K,V>
         implements Map<K,V>, Cloneable, Serializable {
         ...
     }
     ```

  3. 允不允许null值：

     HashTable中，key和value都不允许出现null值，否则会抛出NullPointerException异常。 HashMap中，null可以作为键，这样的键只有一个；可以有一个或多个键所对应的值为null。

  4. 默认初始容量和扩容机制：

     HashTable中的hash数组初始大小是11，增加的方式是 old*2+1。HashMap中hash数组的默认大小是16，而且一定是2的指数倍。原因参考全网把Map中的hash()分析的最透彻的文章，别无二家。-HollisChuang's Blog

  5. 哈希值的使用不同 ：

     HashTable直接使用对象的hashCode。 HashMap重新计算hash值。

  6. 遍历方式的内部实现上不同 ：

     Hashtable、HashMap都使用了 Iterator。而由于历史原因，Hashtable还使用了Enumeration的方式 。 HashMap 实现 Iterator，支持fast-fail，Hashtable的 Iterator 遍历支持fast-fail，用 Enumeration 不支持 fast-fail

- **HashMap 和 ConcurrentHashMap 的区别？**

  ConcurrentHashMap和HashMap的实现方式不一样，虽然都是使用桶数组实现的，但是还是有区别，ConcurrentHashMap对桶数组进行了分段，而HashMap并没有。

  ConcurrentHashMap在每一个分段上都用锁进行了保护。HashMap没有锁机制。所以，前者线程安全的，后者不是线程安全的。

  以上区别基于jdk1.8以前的版本。



#### HashMap的容量、扩容

很多人在通过阅读源码的方式学习Java，这是个很好的方式。而JDK的源码自然是首选。在JDK的众多类中，我觉得HashMap及其相关的类是设计的比较好的。很多人读过HashMap的代码，不知道你们有没有和我一样，觉得HashMap中关于容量相关的参数定义的太多了，傻傻分不清楚。

其实，这篇文章介绍的内容比较简单，只要认真的看看HashMap的原理还是可以理解的，单独写一篇文章的原因是因为我后面还有几篇关于HashMap源码分析的文章，这些概念不熟悉的话阅读后面的文章会很吃力。

先来看一下，HashMap中都定义了哪些成员变量。

![paramInMap](http://www.hollischuang.com/wp-content/uploads/2018/05/paramInMap.png)

上面是一张HashMap中主要的成员变量的图，其中有一个是我们本文主要关注的： `size`、`loadFactor`、`threshold`、`DEFAULT_LOAD_FACTOR`和`DEFAULT_INITIAL_CAPACITY`。

我们先来简单解释一下这些参数的含义，然后再分析他们的作用。

HashMap类中有以下主要成员变量：

- transient int size;
  - 记录了Map中KV对的个数
- loadFactor
  - 装载因子，用来衡量HashMap满的程度。loadFactor的默认值为0.75f（`static final float DEFAULT_LOAD_FACTOR = 0.75f;`）。
- int threshold;
  - 临界值，当实际KV个数超过threshold时，HashMap会将容量扩容，threshold＝容量*装载因子
- 除了以上这些重要成员变量外，HashMap中还有一个和他们紧密相关的概念：capacity
  - 容量，如果不指定，默认容量是16(`static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;`)

可能看完了你还是有点蒙，size和capacity之间有啥关系？为啥要定义这两个变量。loadFactor和threshold又是干啥的？

- **size和capacity**

  HashMap中的size和capacity之间的区别其实解释起来也挺简单的。我们知道，HashMap就像一个“桶”，那么capacity就是这个桶“当前”最多可以装多少元素，而size表示这个桶已经装了多少元素。来看下以下代码：

  ```java
      Map<String, String> map = new HashMap<String, String>();
      map.put("hollis", "hollischuang");
  
      Class<?> mapType = map.getClass();
      Method capacity = mapType.getDeclaredMethod("capacity");
      capacity.setAccessible(true);
      System.out.println("capacity : " + capacity.invoke(map));
  
      Field size = mapType.getDeclaredField("size");
      size.setAccessible(true);
      System.out.println("size : " + size.get(map));
  ```
  
  
  默认情况下，一个HashMap的容量（capacity）是16，设计成16的好处我在《[全网把Map中的hash()分析的最透彻的文章，别无二家。](http://www.hollischuang.com/archives/2091)》中也简单介绍过，主要是可以使用按位与替代取模来提升hash的效率。
  
  为什么我刚刚说capacity就是这个桶“当前”最多可以装多少元素呢？当前怎么理解呢。其实，HashMap是具有扩容机制的。在一个HashMap第一次初始化的时候，默认情况下他的容量是16，当达到扩容条件的时候，就需要进行扩容了，会从16扩容成32，而且总是成2的倍数或者说总体成2的指数倍扩容。
  
  我们知道，HashMap的重载的构造函数中，有一个是支持传入initialCapacity的，那么我们尝试着设置一下，看结果如何。
  
  ```java
      Map<String, String> map = new HashMap<String, String>(1);
  
      Class<?> mapType = map.getClass();
      Method capacity = mapType.getDeclaredMethod("capacity");
      capacity.setAccessible(true);
      System.out.println("capacity : " + capacity.invoke(map));
  
      Map<String, String> map = new HashMap<String, String>(7);
  
      Class<?> mapType = map.getClass();
      Method capacity = mapType.getDeclaredMethod("capacity");
      capacity.setAccessible(true);
      System.out.println("capacity : " + capacity.invoke(map));
  
  
      Map<String, String> map = new HashMap<String, String>(9);
  
      Class<?> mapType = map.getClass();
      Method capacity = mapType.getDeclaredMethod("capacity");
      capacity.setAccessible(true);
      System.out.println("capacity : " + capacity.invoke(map));
  ```
  分别执行以上3段代码，分别输出：**capacity : 2、capacity : 8、capacity : 16**。
  
  也就是说，默认情况下HashMap的容量是16，但是，如果用户通过构造函数指定了一个数字作为容量，那么Hash会选择大于该数字的第一个2的幂作为容量。(1->1、7->8、9->16)
  
- **loadFactor和threshold**

  前面我们提到过，HashMap有扩容机制，就是当达到扩容条件时会进行扩容，从16扩容到32、64、128...

  那么，这个扩容条件指的是什么呢？

  其实，HashMap的扩容条件就是当HashMap中的元素个数（size）超过临界值（threshold）时就会自动扩容。

  在HashMap中，threshold = loadFactor * capacity。

  loadFactor是装载因子，表示HashMap满的程度，默认值为0.75f，设置成0.75有一个好处，那就是0.75正好是3/4，而capacity又是2的幂。所以，两个数的乘积都是整数。

  对于一个默认的HashMap来说，默认情况下，当其size大于12(16*0.75)时就会触发扩容。

  验证代码如下：

  ```java
  public static void resizeTest1() {
        Map<String,String> env = System.getenv();
        HashMap<String,Object> map = new HashMap<>();
        for(Map.Entry<String,String> entry : env.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
            print(map);
        }
    }
  
    private static void print(HashMap<String,Object> map) {
        int size = map.size();
        int threshold = ReflectionUtils.getFieldValue(map, "threshold");
        float loadFactor = ReflectionUtils.getFieldValue(map, "loadFactor");
        Object[] table = ReflectionUtils.getFieldValue(map, "table");
        Method capacityMethod = ReflectionUtils.findMethod(HashMap.class, "capacity");
        capacityMethod.setAccessible(true);
        int capacity = (int) ReflectionUtils.invokeMethod(capacityMethod, map);
        System.out.println(String.format("size = %s, threshold = %s, capacity = %s, table.length = %s, loadFactor = %s", size, threshold, capacity, table == null ? 0 : table.length, loadFactor));
    }
  
    public static void main(String[] args) {
        resizeTest1();
    }
  ```
  输出：
  ```shell
  size = 1, threshold = 12, capacity = 16, table.length = 16, loadFactor = 0.75
  size = 2, threshold = 12, capacity = 16, table.length = 16, loadFactor = 0.75
  size = 3, threshold = 12, capacity = 16, table.length = 16, loadFactor = 0.75
  size = 4, threshold = 12, capacity = 16, table.length = 16, loadFactor = 0.75
  size = 5, threshold = 12, capacity = 16, table.length = 16, loadFactor = 0.75
  size = 6, threshold = 12, capacity = 16, table.length = 16, loadFactor = 0.75
  size = 7, threshold = 12, capacity = 16, table.length = 16, loadFactor = 0.75
  size = 8, threshold = 12, capacity = 16, table.length = 16, loadFactor = 0.75
  size = 9, threshold = 12, capacity = 16, table.length = 16, loadFactor = 0.75
  size = 10, threshold = 12, capacity = 16, table.length = 16, loadFactor = 0.75
  size = 11, threshold = 12, capacity = 16, table.length = 16, loadFactor = 0.75
  size = 12, threshold = 12, capacity = 16, table.length = 16, loadFactor = 0.75
  size = 13, threshold = 24, capacity = 32, table.length = 32, loadFactor = 0.75
  size = 14, threshold = 24, capacity = 32, table.length = 32, loadFactor = 0.75
  size = 15, threshold = 24, capacity = 32, table.length = 32, loadFactor = 0.75
  size = 16, threshold = 24, capacity = 32, table.length = 32, loadFactor = 0.75
  size = 17, threshold = 24, capacity = 32, table.length = 32, loadFactor = 0.75
  size = 18, threshold = 24, capacity = 32, table.length = 32, loadFactor = 0.75
  size = 19, threshold = 24, capacity = 32, table.length = 32, loadFactor = 0.75
  size = 20, threshold = 24, capacity = 32, table.length = 32, loadFactor = 0.75
  size = 21, threshold = 24, capacity = 32, table.length = 32, loadFactor = 0.75
  size = 22, threshold = 24, capacity = 32, table.length = 32, loadFactor = 0.75
  size = 23, threshold = 24, capacity = 32, table.length = 32, loadFactor = 0.75
  size = 24, threshold = 24, capacity = 32, table.length = 32, loadFactor = 0.75
  size = 25, threshold = 48, capacity = 64, table.length = 64, loadFactor = 0.75
  size = 26, threshold = 48, capacity = 64, table.length = 64, loadFactor = 0.75
  size = 27, threshold = 48, capacity = 64, table.length = 64, loadFactor = 0.75
  size = 28, threshold = 48, capacity = 64, table.length = 64, loadFactor = 0.75
  size = 29, threshold = 48, capacity = 64, table.length = 64, loadFactor = 0.75
  size = 30, threshold = 48, capacity = 64, table.length = 64, loadFactor = 0.75
  size = 31, threshold = 48, capacity = 64, table.length = 64, loadFactor = 0.75
  size = 32, threshold = 48, capacity = 64, table.length = 64, loadFactor = 0.75
  size = 33, threshold = 48, capacity = 64, table.length = 64, loadFactor = 0.75
  size = 34, threshold = 48, capacity = 64, table.length = 64, loadFactor = 0.75
  size = 35, threshold = 48, capacity = 64, table.length = 64, loadFactor = 0.75
  size = 36, threshold = 48, capacity = 64, table.length = 64, loadFactor = 0.75
  size = 37, threshold = 48, capacity = 64, table.length = 64, loadFactor = 0.75
  size = 38, threshold = 48, capacity = 64, table.length = 64, loadFactor = 0.75
  size = 39, threshold = 48, capacity = 64, table.length = 64, loadFactor = 0.75
  size = 40, threshold = 48, capacity = 64, table.length = 64, loadFactor = 0.75
  size = 41, threshold = 48, capacity = 64, table.length = 64, loadFactor = 0.75
  size = 42, threshold = 48, capacity = 64, table.length = 64, loadFactor = 0.75
  size = 43, threshold = 48, capacity = 64, table.length = 64, loadFactor = 0.75
  size = 44, threshold = 48, capacity = 64, table.length = 64, loadFactor = 0.75
  size = 45, threshold = 48, capacity = 64, table.length = 64, loadFactor = 0.75
  size = 46, threshold = 48, capacity = 64, table.length = 64, loadFactor = 0.75
  size = 47, threshold = 48, capacity = 64, table.length = 64, loadFactor = 0.75
  size = 48, threshold = 48, capacity = 64, table.length = 64, loadFactor = 0.75
  size = 49, threshold = 96, capacity = 128, table.length = 128, loadFactor = 0.75
  size = 50, threshold = 96, capacity = 128, table.length = 128, loadFactor = 0.75
  size = 51, threshold = 96, capacity = 128, table.length = 128, loadFactor = 0.75
  size = 52, threshold = 96, capacity = 128, table.length = 128, loadFactor = 0.75
  size = 53, threshold = 96, capacity = 128, table.length = 128, loadFactor = 0.75
  size = 54, threshold = 96, capacity = 128, table.length = 128, loadFactor = 0.75
  size = 55, threshold = 96, capacity = 128, table.length = 128, loadFactor = 0.75
  size = 56, threshold = 96, capacity = 128, table.length = 128, loadFactor = 0.75
  size = 57, threshold = 96, capacity = 128, table.length = 128, loadFactor = 0.75
  size = 58, threshold = 96, capacity = 128, table.length = 128, loadFactor = 0.75
  size = 59, threshold = 96, capacity = 128, table.length = 128, loadFactor = 0.75
  size = 60, threshold = 96, capacity = 128, table.length = 128, loadFactor = 0.75
  ```
  当HashMap中的元素个数达到13的时候，capacity就从16扩容到32了。

  HashMap中还提供了一个支持传入initialCapacity,loadFactor两个参数的方法，来初始化容量和装载因子。不过，一般不建议修改loadFactor的值。

- **总结**

  HashMap中size表示当前共有多少个KV对，capacity表示当前HashMap的容量是多少，默认值是16，每次扩容都是成倍的。loadFactor是装载因子，当Map中元素个数超过`loadFactor* capacity`(也即`threshold`)的值时，会触发扩容。`loadFactor* capacity`可以用threshold表示。

  

#### 为什么HashMap的默认负载因子设置成0.75

HashMap是一种K-V结构，为了提升其查询及插入的速度，底层采用了链表的数组这种数据结构实现的。

但是因为在计算元素所在的位置的时候，需要使用hash算法，而HashMap采用的hash算法就是链地址法。这种方法有两个极端。

如果HashMap中哈希冲突概率高，那么HashMap就会退化成链表（不是真的退化，而是操作上像是直接操作链表），而我们知道，链表最大的缺点就是查询速度比较慢，他需要从表头开始逐一遍历。

所以，为了避免HashMap发生大量的哈希冲突，所以需要在适当的时候对其进行扩容。

而扩容的条件是元素个数达到临界值时。HashMap中临界值的计算方法：

```
临界值（threshold） = 负载因子（loadFactor） * 容量（capacity）
```

其中负载因子表示一个数组可以达到的最大的满的程度。这个值不宜太大，也不宜太小。

loadFactory太大，比如等于1，那么就会有很高的哈希冲突的概率，会大大降低查询速度。

**当负载因子是1.0的时候，也就意味着，只有当数组的16个值（默认容量情况下）全部填充了，才会发生扩容。这就带来了很大的问题，因为Hash冲突时避免不了的。当负载因子是1.0的时候，意味着会出现大量的Hash的冲突，底层的红黑树变得异常复杂。对于查询效率极其不利。这种情况就是牺牲了时间来保证空间的利用率。**

loadFactory太小，比如等于0.5，那么频繁扩容没，就会大大浪费空间。

所以，这个值需要介于0.5和1之间。根据数学公式推算。这个值在log(2)的时候比较合理。

另外，为了提升扩容效率，HashMap的容量（capacity）有一个固定的要求，那就是一定是2的幂。

所以，如果loadFactor是3/4的话，那么和capacity的乘积结果就可以是一个整数。

所以，一般情况下，我们不建议修改loadFactory的值，除非特殊原因。

比如我明确的知道我的Map只存5个kv，并且永远不会改变，那么可以考虑指定loadFactory。

但是其实我也不建议这样用。我们完全可以通过指定capacity达到这样的目的。



#### 为什么建议设置HashMap的初始容量，设置多少合适

当HashMap内部维护的哈希表的容量达到75%时（默认情况下），会触发扩容及rehash，而rehash的过程是比较耗费时间的。所以初始化容量要设置成expectedSize/0.75 + 1的话，可以有效的减少冲突也可以减小误差。（大家结合这个公式，好好理解下这句话）

**所以，我们可以认为，当我们明确知道HashMap中元素的个数的时候，把默认容量设置成expectedSize / 0.75F + 1 是一个在性能上相对好的选择，但是，同时也会牺牲些内存。**



#### HashMap相关的面试题大全

https://www.toutiao.com/i6736721544115913220/