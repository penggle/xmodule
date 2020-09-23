package com.penglecode.xmodule.master4j.spring.beans.cyclicdependency;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/17 10:57
 */
public class BarBean {

    private FooBean foo;

    public FooBean getFoo() {
        return foo;
    }

    public void setFoo(FooBean foo) {
        this.foo = foo;
    }

}
