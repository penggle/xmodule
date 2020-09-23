package com.penglecode.xmodule.master4j.spring.beans.cyclicdependency;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/17 10:56
 */
public class FooBean {

    private BarBean bar;

    public BarBean getBar() {
        return bar;
    }

    public void setBar(BarBean bar) {
        this.bar = bar;
    }
}
