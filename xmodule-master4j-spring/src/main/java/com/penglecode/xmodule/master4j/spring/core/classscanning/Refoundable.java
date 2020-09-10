package com.penglecode.xmodule.master4j.spring.core.classscanning;

import java.util.Map;

/**
 * 可退款的
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/6 21:48
 */
public interface Refoundable {

    /**
     * 退款接口
     * @param parameter
     * @return
     */
    public boolean refound(Map<String,Object> parameter);

}
