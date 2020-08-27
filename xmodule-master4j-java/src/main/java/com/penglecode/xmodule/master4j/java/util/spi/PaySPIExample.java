package com.penglecode.xmodule.master4j.java.util.spi;

import java.util.HashMap;
import java.util.ServiceLoader;

/**
 * IPay接口的SPI示例
 *
 * 1、定义一组接口 (这里是IPay接口)，并写出接口的一个或多个实现(例如这里的AliPay和WeiXinPay)。
 *
 * 2、在 src/main/resources/ 下建立 /META-INF/services 目录， 新增一个以接口命名的文件(这里是com.penglecode.xmodule.master4j.java.spi.IPay)，
 * 内容是要应用的实现类（这里是com.penglecode.xmodule.master4j.java.spi.AliPay和com.penglecode.xmodule.master4j.java.spi.WeiXinPay，每行一个类）
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/25 17:49
 */
public class PaySPIExample {

    public static void main(String[] args) {
        ServiceLoader<IPay> pays = ServiceLoader.load(IPay.class);
        for (IPay pay : pays) {
            pay.pay(new HashMap<>());
        }
    }

}
