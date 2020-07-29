package com.penglecode.xmodule.master4j.java.basics.overriding;

import java.io.EOFException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * 重写
 * 重写指的是在Java的子类与父类中有两个名称、参数列表都相同的方法的情况。由于他们具有相同的方法签名，所以子类中的新方法将覆盖父类中原有的方法。
 * 从JVM的角度来说，方法重载是JVM"动态分配"的体现，即JVM在运行期根据引用变量的实际类型来选择方法版本。
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/27 17:54
 */
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
