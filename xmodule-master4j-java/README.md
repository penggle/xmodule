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
  从JVM的角度来说，方法重载是JVM"动态分配"的体现，即JVM在运行期根据引用变量的实际类型来选择方法版本。
  
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
  > 2、重载遵循所谓“编译期绑定”，即在编译时根据**参数变量的类型**判断应该调用哪个方法。
  >
  > 3、重写遵循所谓“运行期绑定”，即在运行的时候，根据**引用变量所指向的实际对象的类型**来调用方法。
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

在Java的方法中，在传递原始类型的时候无疑是值传递。在传递对象类型的时候是引用传递也是值传递，只不过是把对象的引用当做值传递给方法，传递的是对象的引用地址，而不是对象的拷贝，Java会将对象的引用地址的拷贝传递给被调函数的形式参数。



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

- **自动拆装箱带来的问题**

  当然，自动拆装箱是一个很好的功能，大大节省了开发人员的精力，不再需要关心到底什么时候需要拆装箱。但是，他也会引入一些问题。

  > 包装对象的数值比较，不能简单的使用`==`，虽然-128到127之间的数字可以，但是这个范围之外还是需要使用`equals`比较。
  >
  > 前面提到，有些场景会进行自动拆装箱，同时也说过，由于自动拆箱，如果包装类对象为null，那么自动拆箱时就有可能抛出NPE。
  >
  > 如果一个for循环中有大量拆装箱操作，会影响性能。

  

